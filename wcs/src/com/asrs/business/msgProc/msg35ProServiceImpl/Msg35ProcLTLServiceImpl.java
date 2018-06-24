package com.asrs.business.msgProc.msg35ProServiceImpl;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.ReasonCode;
import com.asrs.business.msgProc.msg35ProcService.Msg35ProcService;
import com.asrs.domain.AsrsJob;
import com.asrs.domain.Location;
import com.asrs.domain.XMLMessage;
import com.asrs.message.Message35;
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
import com.thread.blocks.Block;
import com.thread.blocks.MCar;
import com.thread.blocks.SCar;
import com.thread.blocks.Srm;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 23:27 2018/6/21
 * @Description:
 * @Modified By:
 */
public class Msg35ProcLTLServiceImpl implements Msg35ProcService {
    private Message35 message35;
    private AsrsJob aj;
    private Block block;

    public Msg35ProcLTLServiceImpl(Message35 message35, AsrsJob aj, Block block) {
        this.message35 = message35;
        this.aj = aj;
        this.block = block;
    }

    @Override
    public void sCar35Proc() throws Exception {
        SCar sCar = (SCar) block;
        if (message35.isUnloadGoods()) {
            sCar.setOnMCar(null);
            sCar.setBank(Integer.parseInt(message35.Bank));
            sCar.setLoad("0");
            sCar.clearMckeyAndReservMckey();
            transferFinish(aj);
            if (aj.getStatus().equals(AsrsJobStatus.DONE)) {
//                                    aj.delete();
            } else {
                aj.setStatus(AsrsJobStatus.DONE);
            }

        } else if (message35.isPickingUpGoods()) {
            sCar.generateMckey(message35.McKey);
            //sCar.setOnMCar(message35.Station);
            //sCar.setBank(0);
            sCar.setLoad("1");
            Location fromLocation = Location.getByLocationNo(aj.getFromLocation());
            fromLocation.setReserved(false);
            fromLocation.setEmpty(true);
            aj.setStatus(AsrsJobStatus.PICKING);
        } else if (message35.isOffCar()) {
            sCar.setBank(Integer.parseInt(message35.Bank));
            sCar.setOnMCar(null);
            sCar.generateMckey(message35.McKey);

        }else if (message35.isOnCarCarryGoods()) {
            sCar.setOnMCar(message35.Station);
        }else if (message35.isOffCarCarryGoods()) {
            sCar.setOnMCar(null);
        }
    }

    @Override
    public void srm35Proc() throws Exception {
        Srm srm = (Srm) block;
        if (message35.isMove()) {
            srm.setLevel(Integer.parseInt(message35.Level));
            srm.setBay(Integer.parseInt(message35.Bay));
            srm.setDock(message35.Station);
            srm.setCheckLocation(true);
            Location location = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), srm.getPosition());
            srm.setActualArea(location.getActualArea());
            if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                sCar.setLevel(srm.getLevel());
                sCar.setBay(srm.getBay());
                sCar.setActualArea(location.getActualArea());
            }
        } else if (message35.isUnLoadCar()) {
            srm.setsCarBlockNo(null);
            if (StringUtils.isNotBlank(srm.getMcKey())) {
                srm.clearMckeyAndReservMckey();
            }
        } else if (message35.isLoadCar()) {
            srm.setsCarBlockNo(message35.Station);
            srm.generateMckey(message35.McKey);
        }
    }

    @Override
    public void mCar35Proc() throws Exception {
        MCar mCar = (MCar) block;
        if (message35.isMove()) {
            /*srm.setLevel(Integer.parseInt(message35.Level));
            srm.setBay(Integer.parseInt(message35.Bay));
            srm.setDock(message35.Station);
            srm.setCheckLocation(true);
            Location location = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), srm.getPosition());
            srm.setActualArea(location.getActualArea());
            if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                sCar.setLevel(srm.getLevel());
                sCar.setBay(srm.getBay());
                sCar.setActualArea(location.getActualArea());
            }*/
            mCar.setDock(null);
            mCar.setBay(Integer.parseInt(message35.Bay));
            Location toLoc = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), mCar.getBay(), mCar.getLevel(), mCar.getPosition());
            mCar.setActualArea(toLoc.getActualArea());
            mCar.setCheckLocation(true);
            if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                sCar.setBay(mCar.getBay());
                sCar.setLevel(mCar.getLevel());
                sCar.setActualArea(mCar.getActualArea());
            }
        } else if (message35.isUnLoadCar()) {
            mCar.setsCarBlockNo(null);
            if (StringUtils.isNotBlank(mCar.getMcKey())) {
                mCar.clearMckeyAndReservMckey();
            }
        } else if (message35.isLoadCar()) {
            if(message35.McKey.equals("9999")){
                mCar.clearMckeyAndReservMckey();
            }else{
                mCar.setsCarBlockNo(message35.Station);
                SCar sCar = (SCar) SCar.getByBlockNo(message35.Station);
                if(sCar.getLoad().equals("1"))
                    mCar.generateMckey(message35.McKey);

            }
        }
    }

    @Override
    public void lift35Proc() throws Exception {

    }

    @Override
    public void converyor35Proc() throws Exception {

    }

    @Override
    public void station35Proc() throws Exception {

    }


    private void transferFinish(AsrsJob asrsJob) throws Exception {
        Sender sd = new Sender();
        sd.setDivision(XMLConstant.COM_DIVISION);

        Receiver receiver = new Receiver();
        receiver.setDivision(XMLConstant.WMS_DIVISION);
        RefId ri = new RefId();
        ri.setReferenceId(asrsJob.getMcKey());

        ControlArea ca = new ControlArea();
        ca.setSender(sd);
        ca.setReceiver(receiver);
        ca.setRefId(ri);
        ca.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        Location location = Location.getByLocationNo(asrsJob.getFromLocation());
        FromLocation fromLocation = new FromLocation();
        fromLocation.setMHA(asrsJob.getFromStation());
        List<String> rack = new ArrayList<>(3);
        rack.add(String.valueOf(location.getBank()));
        rack.add(String.valueOf(location.getBay()));
        rack.add(String.valueOf(location.getLevel()));
        fromLocation.setRack(rack);

        ToLocation toLocation = new ToLocation();
        List<String> list = new ArrayList<>(3);
        list.add("");
        list.add("");
        list.add("");
        toLocation.setMHA(asrsJob.getFromStation());
        toLocation.setRack(list);


        //创建MovementReportDA数据域对象
        MovementReportDA mrd = new MovementReportDA();
        mrd.setReasonCode(ReasonCode.LOCATIONTOLOCATION);
        mrd.setStUnitId(asrsJob.getBarcode());
        mrd.setFromLocation(fromLocation);
        mrd.setToLocation(toLocation);
        //创建MovementReport响应核心对象
        MovementReport mr = new MovementReport();
        mr.setControlArea(ca);
        mr.setDataArea(mrd);
        //将MovementReport发送给wms
        Envelope el = new Envelope();
        el.setMovementReport(mr);

        XMLMessage xmlMessage = new XMLMessage();
        xmlMessage.setRecv("WMS");
        xmlMessage.setStatus("1");
        xmlMessage.setMessageInfo(XMLUtil.getSendXML(el));
//        HibernateUtil.getCurrentSession().save(xmlMessage);
        XMLUtil.sendEnvelope(el);
    }
}
