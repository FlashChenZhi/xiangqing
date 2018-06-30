package com.asrs.business.msgProc.msg35ProServiceImpl;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.ReasonCode;
import com.asrs.business.msgProc.msg35ProcService.Msg35ProcService;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.InMessage;
import com.asrs.domain.Location;
import com.asrs.domain.XMLMessage;
import com.asrs.message.Message35;
import com.asrs.message.MsgException;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Receiver;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.domain.XMLbean.XMLList.MovementReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.thread.blocks.*;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 22:48 2018/6/21
 * @Description:
 * @Modified By:
 */
public class Msg35ProPutawayServiceImpl implements Msg35ProcService {

    private Message35 message35;
    private AsrsJob aj;
    private Block block;

    public Msg35ProPutawayServiceImpl(Message35 message35, AsrsJob aj, Block block) {
        this.message35 = message35;
        this.aj = aj;
        this.block = block;
    }


    @Override
    public void sCar35Proc() throws Exception {
        SCar sCar = (SCar) block;
        Location location = Location.getByLocationNo(aj.getToLocation());
        if (message35.isUnloadGoods()) {
            //卸货
            sCar.clearMckeyAndReservMckey();
            sCar.setOnMCar(null);
            //入库完成，发送消息给wms
            putawayFinish(aj, location);

            sCar.setBank(location.getBank());
            if (aj.getStatus().equals(AsrsJobStatus.DONE)) {
                // aj.delete();
            } else {
                aj.setStatus(AsrsJobStatus.DONE);
            }

            sCar.clearMckeyAndReservMckey();

        } else if (message35.isOnCar()) {
            //上车
            sCar.setOnMCar(message35.Station);
            sCar.setBank(0);

            /*Block block1 = Block.getByBlockNo(message35.Station);
            if (block1 instanceof Srm) {
                Srm srm = (Srm) block1;
                sCar.setOnMCar(srm.getBlockNo());
                if (StringUtils.isNotEmpty(srm.getMcKey())) {
                    sCar.generateReserveMckey(message35.McKey);
                }
            }*/
        } else if (message35.isOffCarCarryGoods()) {
            //载货下车
            sCar.setOnMCar(null);
            sCar.generateMckey(message35.McKey);
            sCar.setBank(Integer.parseInt(message35.Bank));
        }
    }

    @Override
    public void srm35Proc() throws Exception {
        Srm srm = (Srm) block;
        if (message35.isMove()) {
            //升降机移动
            /**
             * 收到的是升降机移动的35
             *   判断提升机是移动到站台还是移动到库位列
             *   设置提升机的dock，area，设置小车的bay，lev，area
             *   设置提升机的checkLocation为true
             */
            if (message35.Station.equals("0000")) {
                srm.setDock(null);
                srm.setBay(Integer.parseInt(message35.Bay));
                Location toLoc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), srm.getBay(), srm.getLevel(), srm.getPosition());
                srm.setActualArea(toLoc.getActualArea());
                if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                    sCar.setBay(srm.getBay());
                    sCar.setLevel(srm.getLevel());
                    sCar.setActualArea(srm.getActualArea());
                }
            } else {
                srm.setDock(message35.Station);
            }
            srm.setCheckLocation(true);
        } else if (message35.isMoveCarryGoods()) {
            /**
             * 收到的是移载取货的35
             *   将mckey放入提升机的Mckey中
             *   将提升机的reservedMcky清空
             */
            srm.generateMckey(message35.McKey);
            aj.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
        } else if (message35.isMoveUnloadGoods()) {
            /**
             * 收到的是移载卸货的35
             *   清空reservedMckey和Mckey
             */
            srm.clearMckeyAndReservMckey();
        } else if (message35.isLoadCar()) {
            /**
             * 收到的是接子车的35
             *   将提升机的scarBlockNo设为接到的小车
             *   若小车的mckey不为空，将提升机的mckey置为小车的mckey，将reservedMckey置为空
             */
            SCar sCar = (SCar) Block.getByBlockNo(message35.Station);
            srm.setsCarBlockNo(sCar.getBlockNo());

            if (StringUtils.isNotBlank(sCar.getMcKey())) {
                srm.generateMckey(sCar.getMcKey());
            }

        } else if (message35.isUnLoadCar()) {
            /**
             * 收到的是卸子车的35
             *   将提升机的scarBlockNo设为null 清除提升机的mckey和resercedMckey
             */
            SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
            srm.setsCarBlockNo(null);
            // mCar.setReservedMcKey(null);
            srm.clearMckeyAndReservMckey();
        }
    }

    @Override
    public void mCar35Proc() throws Exception {
        MCar mCar = (MCar) block;
        if (message35.isMove()) {
            //移动
            if (message35.Station.equals("0000")) {
                mCar.setDock(null);
                mCar.setBay(Integer.parseInt(message35.Bay));
                Location toLoc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), mCar.getBay(), mCar.getLevel(), mCar.getPosition());
                mCar.setActualArea(toLoc.getActualArea());
                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                    sCar.setBay(mCar.getBay());
                    sCar.setLevel(mCar.getLevel());
                    sCar.setActualArea(mCar.getActualArea());
                }
            } else {
                mCar.setDock(message35.Station);
                mCar.setBay(0);

                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                    sCar.setBay(mCar.getBay());
                    sCar.setLevel(mCar.getLevel());
                    sCar.setActualArea(mCar.getActualArea());
                }
            }
            mCar.setCheckLocation(true);
        } else if (message35.isMoveCarryGoods()) {
            //移载取货
            mCar.generateMckey(message35.McKey);
            aj.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
        } else if (message35.isMoveUnloadGoods()) {
            //移载卸货
            mCar.clearMckeyAndReservMckey();
        } else if (message35.isLoadCar()) {
            //接子车
            SCar sCar = (SCar) Block.getByBlockNo(message35.Station);
            mCar.setsCarBlockNo(sCar.getBlockNo());

            if (StringUtils.isNotBlank(sCar.getMcKey())) {
                mCar.generateMckey(sCar.getMcKey());
            }

        } else if (message35.isUnLoadCar()) {
            //卸子车
            SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
            mCar.setsCarBlockNo(null);
            // mCar.setReservedMcKey(null);
            mCar.clearMckeyAndReservMckey();
        }
    }

    @Override
    public void lift35Proc() throws Exception {
        Lift lift = (Lift) block;
        if(message35.isMove()){
            lift.setDock(message35.Station);
            Dock dock =lift.getDockClass(message35.Station);
            lift.setLevel(dock.getLevel());
        }else if(message35.isMoveCarryGoods()){
            lift.generateMckey(message35.McKey);
            lift.setDock(null);
        }else if(message35.isMoveUnloadGoods()){
            lift.clearMckeyAndReservMckey();
            lift.setDock(null);
        }
    }

    @Override
    public void converyor35Proc() throws Exception {
        Conveyor conveyor = (Conveyor) block;
        if (message35.isMoveUnloadGoods()) {
            conveyor.clearMckeyAndReservMckey();
        } else if (message35.isMoveCarryGoods()) {
            conveyor.generateMckey(message35.McKey);
        }
    }

    @Override
    public void station35Proc() throws Exception {
        StationBlock station = (StationBlock) block;
        if (message35.isMoveUnloadGoods()) {
            //移载卸货
            station.setMcKey(null);
            InMessage.error(station.getStationNo(), "");
        }
        //入库站台是否要加移载取货设置mckey，
        // 看1301，1302，1303，这三个站台我们系统是把他们看成什么（站台or传送带）
        /*if (message35.isMoveCarryGoods()) {
        //移载取货
        station.setMcKey(message35.McKey);
        }*/
    }


    private void putawayFinish(AsrsJob aj, Location location) throws Exception {

        //创建ControlArea控制域对象
        Sender sd = new Sender();
        sd.setDivision(XMLConstant.COM_DIVISION);

        Receiver receiver = new Receiver();
        receiver.setDivision(XMLConstant.WMS_DIVISION);

        RefId ri = new RefId();
        ri.setReferenceId(aj.getWmsMckey());

        ControlArea ca = new ControlArea();
        ca.setSender(sd);
        ca.setReceiver(receiver);
        ca.setRefId(ri);
        ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        FromLocation fromLocation = new FromLocation();
        List<String> list = new ArrayList<>(3);
        fromLocation.setMHA("");
        list.add("");
        list.add("");
        list.add("");
        fromLocation.setRack(list);

        ToLocation toLocation = new ToLocation();
        // TODO: 2017/5/1 修改货位
        toLocation.setMHA(aj.getFromStation());
        List<String> rack = new ArrayList<>(3);
        rack.add(String.valueOf(location.getBank()));
        rack.add(String.valueOf(location.getBay()));
        rack.add(String.valueOf(location.getLevel()));
        toLocation.setRack(rack);

        //创建MovementReportDA数据域对象
        MovementReportDA mrd = new MovementReportDA();
        mrd.setFromLocation(fromLocation);
        mrd.setStUnitId(aj.getBarcode());
        mrd.setReasonCode(ReasonCode.PUTAWAYFINISHED);
        mrd.setToLocation(toLocation);
        //创建MovementReport响应核心对象
        MovementReport mr = new MovementReport();
        mr.setControlArea(ca);
        mr.setDataArea(mrd);
        //将MovementReport发送给wms
        Envelope el = new Envelope();
        el.setMovementReport(mr);

        XMLMessage xmlMessage = new XMLMessage();
        xmlMessage.setStatus("1");
        xmlMessage.setRecv("WMS");
        xmlMessage.setMessageInfo(XMLUtil.getSendXML(el));
        HibernateUtil.getCurrentSession().save(xmlMessage);
        XMLUtil.sendEnvelope(el);
    }

}
