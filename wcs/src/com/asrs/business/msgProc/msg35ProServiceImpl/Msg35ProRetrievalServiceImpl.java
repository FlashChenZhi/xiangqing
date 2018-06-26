package com.asrs.business.msgProc.msg35ProServiceImpl;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.asrs.business.msgProc.msg35ProcService.Msg35ProcService;
import com.asrs.domain.*;
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
import com.thread.blocks.*;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 23:14 2018/6/21
 * @Description:
 * @Modified By:
 */
public class Msg35ProRetrievalServiceImpl implements Msg35ProcService {
    private Message35 message35;
    private AsrsJob aj;
    private Block block;

    public Msg35ProRetrievalServiceImpl(Message35 message35, AsrsJob aj, Block block) {
        this.message35 = message35;
        this.aj = aj;
        this.block = block;
    }

    @Override
    public void sCar35Proc() throws Exception {
        SCar sCar = (SCar) block;
        if (message35.isPickingUpGoods()) {
            sCar.setLoad("1");
            aj.setStatus(AsrsJobStatus.PICKING);
            /*if("0051".equals(aj.getToStation())||"0053".equals(aj.getToStation())){
                Location location=Location.getByLocationNo(aj.getFromLocation());
                location.setReserved(false);
                location.setEmpty(true);
            }*/
        } else if (message35.isOnCar()) {

            sCar.setOnMCar(message35.Station);
            sCar.setBank(0);

        } else if (message35.isOffCar()) {

            sCar.setBank(Integer.parseInt(message35.Bank));
            sCar.setOnMCar(null);
            sCar.setLoad("0");
            sCar.generateMckey(message35.McKey);
            Location location = Location.getByLocationNo(aj.getFromLocation());
            sCar.setActualArea(location.getActualArea());

        } else if (message35.isOnCarCarryGoods()) {
            MCar srm = MCar.getMCarByGroupNo(sCar.getGroupNo());
            sCar.setLoad("0");
            sCar.setOnMCar(srm.getBlockNo());
            sCar.setBank(0);
            sCar.clearMckeyAndReservMckey();
            Query query = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type=:ajType and status=:st and statusDetail=:detail and fromStation=:frs order by generateTime asc ");
            query.setParameter("ajType", AsrsJobType.RETRIEVAL);
            query.setParameter("detail", AsrsJobStatusDetail.WAITING);
            query.setParameter("st", AsrsJobStatus.RUNNING);
            query.setParameter("frs", srm.getBlockNo());

            query.setMaxResults(1);
            AsrsJob asrsJob = (AsrsJob) query.uniqueResult();
            if (asrsJob != null) {
                Location location = Location.getByLocationNo(asrsJob.getFromLocation());
                if(location.getBay() == sCar.getBay() && location.getLevel() == sCar.getLevel()) {
                    sCar.setReservedMcKey(asrsJob.getMcKey());
                    asrsJob.setStatusDetail(AsrsJobStatusDetail.ACCEPTED);
                    asrsJob.setStatus(AsrsJobStatus.ACCEPT);
                }
            }
        }
    }

    @Override
    public void srm35Proc() throws Exception {
        Srm srm = (Srm) block;
        if (message35.isMoveUnloadGoods()) {
            srm.setMcKey(null);
        } else if (message35.isUnLoadCar()) {

            srm.setsCarBlockNo(null);

        } else if (message35.isMove()) {

            srm.setCheckLocation(true);
            srm.setLevel(Integer.parseInt(message35.Level));

            Location location = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), srm.getPosition());

            if (location != null) {
                srm.setActualArea(location.getActualArea());
            }

            if (message35.Bay.equals("00")) {
                srm.setDock(message35.Station);
                srm.setBay(0);
                if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                    sCar.setBay(srm.getBay());
                    sCar.setActualArea(srm.getActualArea());
                    sCar.setLevel(srm.getLevel());
                }

            } else {
                srm.setDock(null);
                srm.setBay(Integer.parseInt(message35.Bay));
                if (StringUtils.isNotBlank(srm.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(srm.getsCarBlockNo());
                    sCar.setBay(srm.getBay());
                    sCar.setLevel(srm.getLevel());
                    sCar.setActualArea(srm.getActualArea());
                }
            }

        } else if (message35.isLoadCar()) {
            SCar sCar = (SCar) Block.getByBlockNo(message35.Station);
            srm.setsCarBlockNo(sCar.getBlockNo());


            if (StringUtils.isEmpty(sCar.getMcKey()) && StringUtils.isEmpty(sCar.getReservedMcKey())) {
                if (StringUtils.isEmpty(sCar.getOnMCar())) {
                    //子车不在母车上，子车有排列层，查找货位是否是aj的源货位，如果一样，取货上车，如果不是，简单接车
                    Location sLocation = Location.getByBankBayLevel(sCar.getBank(), sCar.getBay(), sCar.getLevel(), sCar.getPosition());
                    if (sLocation.getLocationNo().equals(aj.getFromLocation())) {
                        srm.generateMckey(message35.McKey);
                    } else {

                    }

                } else {
                    //如果订单状态不是1，表明子车已经领取过任务了，子车领取过任务后，子车没有任务号后，表示已经做完任务
                    if (!aj.getStatus().equals(AsrsJobStatus.RUNNING)) {
                        srm.generateMckey(message35.McKey);
                    }
                }
            }

            if (StringUtils.isNotEmpty(sCar.getMcKey())) {
                srm.generateMckey(message35.McKey);
            } else if (StringUtils.isNotEmpty(sCar.getReservedMcKey())) {

                if (StringUtils.isEmpty(sCar.getOnMCar())) {
                    //子车不在母车上，子车有排列层，查找货位是否是aj的源货位，如果一样，取货上车，如果不是，简单接车
                    Location sLocation = Location.getByBankBayLevel(sCar.getBank(), sCar.getBay(), sCar.getLevel(), sCar.getPosition());
                    if (sLocation.getLocationNo().equals(aj.getFromLocation())) {
                        srm.generateMckey(message35.McKey);
                    } else {

                    }

                } else {
                    //子车在堆垛机，子车有reservedmckey，子车的任务和堆垛机的任务不一样
                    if (!message35.McKey.equals(sCar.getReservedMcKey())) {
                        //堆垛机上任务和子车任务不同
                        srm.generateMckey(message35.McKey);
                    }
                }
            }
        }
    }

    @Override
    public void mCar35Proc() throws Exception {
        MCar mCar = (MCar) block;
        if (message35.isMoveUnloadGoods()) {
            mCar.setMcKey(null);
        } else if (message35.isUnLoadCar()) {

            mCar.setsCarBlockNo(null);

        } else if (message35.isMove()) {

            mCar.setCheckLocation(true);
            //mCar.setLevel(Integer.parseInt(message35.Level));

            Location location = Location.getByBankBayLevel(Integer.parseInt(message35.Bank), Integer.parseInt(message35.Bay), Integer.parseInt(message35.Level), mCar.getPosition());

            if (location != null) {
                mCar.setActualArea(location.getActualArea());
            }

            if (message35.Bay.equals("00")) {
                mCar.setDock(message35.Station);
                mCar.setBay(0);
                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                    sCar.setBay(mCar.getBay());
                    sCar.setActualArea(mCar.getActualArea());
                    sCar.setLevel(mCar.getLevel());
                }

            } else {
                mCar.setDock(null);
                mCar.setBay(Integer.parseInt(message35.Bay));
                if (StringUtils.isNotBlank(mCar.getsCarBlockNo())) {
                    SCar sCar = (SCar) Block.getByBlockNo(mCar.getsCarBlockNo());
                    sCar.setBay(mCar.getBay());
                    sCar.setLevel(mCar.getLevel());
                    sCar.setActualArea(mCar.getActualArea());
                }
            }

        } else if (message35.isLoadCar()) {
            SCar sCar = (SCar) Block.getByBlockNo(message35.Station);
            mCar.setsCarBlockNo(sCar.getBlockNo());


            if (StringUtils.isEmpty(sCar.getMcKey()) && StringUtils.isEmpty(sCar.getReservedMcKey())) {
                if (StringUtils.isEmpty(sCar.getOnMCar())) {
                    //子车不在母车上，子车有排列层，查找货位是否是aj的源货位，如果一样，取货上车，如果不是，简单接车
                    Location sLocation = Location.getByBankBayLevel(sCar.getBank(), sCar.getBay(), sCar.getLevel(), sCar.getPosition());
                    if (sLocation.getLocationNo().equals(aj.getFromLocation())) {
                        mCar.generateMckey(message35.McKey);
                    } else {

                    }

                } else {
                    //如果订单状态不是1，表明子车已经领取过任务了，子车领取过任务后，子车没有任务号后，表示已经做完任务
                    if (!aj.getStatus().equals(AsrsJobStatus.RUNNING)) {
                        mCar.generateMckey(message35.McKey);
                    }
                }
            }

            if (StringUtils.isNotEmpty(sCar.getMcKey())) {
                mCar.generateMckey(message35.McKey);
            } else if (StringUtils.isNotEmpty(sCar.getReservedMcKey())) {

                if (StringUtils.isEmpty(sCar.getOnMCar())) {
                    //子车不在母车上，子车有排列层，查找货位是否是aj的源货位，如果一样，取货上车，如果不是，简单接车
                    Location sLocation = Location.getByBankBayLevel(sCar.getBank(), sCar.getBay(), sCar.getLevel(), sCar.getPosition());
                    if (sLocation.getLocationNo().equals(aj.getFromLocation())) {
                        mCar.generateMckey(message35.McKey);
                    } else {

                    }

                } else {
                    //子车在堆垛机，子车有reservedmckey，子车的任务和堆垛机的任务不一样
                    if (!message35.McKey.equals(sCar.getReservedMcKey())) {
                        //堆垛机上任务和子车任务不同
                        mCar.generateMckey(message35.McKey);
                    }
                }
            }
        }
    }

    @Override
    public void lift35Proc() throws Exception {
        Lift lift = (Lift) block;
        if (message35.isMove()) {
            lift.setDock(message35.Station);
            if (StringUtils.isBlank(lift.getMcKey())) {
                lift.setReservedMcKey(message35.McKey);
            }
        } else if (message35.isMoveCarryGoods()) {
            lift.generateMckey(message35.McKey);
        } else if (message35.isMoveUnloadGoods()) {
            lift.clearMckeyAndReservMckey();
        }
    }

    @Override
    public void converyor35Proc() throws Exception {
        Conveyor conveyor = (Conveyor) block;
        if (message35.isMoveCarryGoods()) {
            conveyor.generateMckey(message35.McKey);
        } else if (message35.isMoveUnloadGoods()) {
            conveyor.clearMckeyAndReservMckey();
        }
    }

    @Override
    public void station35Proc() throws Exception {
        Session session = HibernateUtil.getCurrentSession();
        StationBlock stationBlock = (StationBlock)block;
        if (message35.isMoveCarryGoods()) {
            block.setMcKey(message35.McKey);
            /*Location fromLocation = Location.getByLocationNo(aj.getFromLocation());
            if(!fromLocation.getEmpty()){
                fromLocation.setReserved(false);
                fromLocation.setEmpty(true);
            }*/
            AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(message35.McKey);
            if(asrsJob.getToStation().equals(stationBlock.getBlockNo())){
                /*LedMessage ledMessage = LedMessage.getByLedNo(stationBlock.getStationNo());
                if(ledMessage != null&&!ledMessage.isProcessed()) {
                    Query query1 =session.createQuery("from Inventory i where i.container.barcode=:barcode")
                            .setString("barcode", aj.getBarcode())
                            .setMaxResults(1);
                    Inventory inventory = (Inventory) query1.uniqueResult();
                    if (StringUtils.isNotBlank(stationBlock.getStationNo()) && inventory != null *//*&& !aj.getMcKey().equals(ledMessage.getMcKey())*//*) {
                        String skuName=inventory.getSkuCode();
                        if(StringUtils.isNotBlank(inventory.getSkuCode())){
                            Sku sku = Sku.getByCode(inventory.getSkuCode());
                            if(sku!=null){
                                skuName=sku.getSkuName();
                            }
                        }
                        Query query2 = session.createQuery("from Job where mcKey=:mcKey").setMaxResults(1);
                        Job job= (Job) query2.setString("mcKey",aj.getMcKey()).uniqueResult();
                        String p=stationBlock.getStationNo();
                        if(job!=null){
                            p=job.getFinalLoc();
                        }

                        Qty qty=(Qty) session.createQuery("from Qty where skuCode=:skuCode").setString("skuCode",inventory.getSkuCode()).setMaxResults(1).uniqueResult();
                        double count=inventory.getQty().intValue();
                        if(qty!=null&&!"ktp".equals(inventory.getSkuCode())) {
                            float i = inventory.getQty().intValue();
                            float j = qty.getQty().intValue();
                            float qqq = i / j;
                            count = Math.ceil(qqq);
                        }
                        LedMessage.show(stationBlock.getStationNo(), p, skuName, inventory.getLotNum(), aj.getBarcode()+"  "+count);
                        ledMessage.setMcKey(aj.getMcKey());
                    }
                }*/
                retrievalFinish(aj);
            }

        }else if (message35.isMoveUnloadGoods()) {
            block.clearMckeyAndReservMckey();
        }
    }

    private void retrievalFinish(AsrsJob asrsJob) throws Exception {
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
        toLocation.setMHA("");
        toLocation.setRack(list);


        //创建MovementReportDA数据域对象
        MovementReportDA mrd = new MovementReportDA();
        mrd.setReasonCode(ReasonCode.RETRIEVALFINISHED);
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

        XMLUtil.sendEnvelope(el);
        /*XMLMessage xmlMessage = new XMLMessage();
        xmlMessage.setRecv("WMS");
        xmlMessage.setStatus("1");
        xmlMessage.setMessageInfo(XMLUtil.getSendXML(el));
//        HibernateUtil.getCurrentSession().save(xmlMessage);
        XMLUtil.sendEnvelope(el);*/

    }

}
