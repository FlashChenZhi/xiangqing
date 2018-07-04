package com.asrs.business.msgProc;

import com.asrs.AsrsJobCenter;
import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobStatusDetail;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.ReasonCode;
import com.asrs.business.msgProc.msg35ProServiceImpl.*;
import com.asrs.business.msgProc.msg35ProcService.Msg35ProcService;
import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.*;
import com.asrs.message.*;
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
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import com.thread.blocks.*;
import com.util.common.Const;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.jboss.logging.Logger;

import java.rmi.Naming;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.hibernate.exception.LockAcquisitionException;
import static com.asrs.AsrsJobCenter._wcsproxy;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg35Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message35 message35 = new Message35(msg.DataString);
        message35.setPlcName(msg.PlcName);
        Do(message35);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;

    public static void main(String[] args) throws Exception {
        Transaction.begin();
        Message35 msg35 = new Message35("1309005307030000000000520000" + "000");
        Msg35Proc proc = new Msg35Proc();
//        AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey("1899");
//        Location location = Location.getByLocationNo(asrsJob.getToLocation());
//        proc.putawayFinish(asrsJob, location);
        Transaction.commit();
        proc.Do(msg35);
//        MessageProxy proxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);
//        Message05 m51 = new Message05();
//        m51.setPlcName("BL01");
//        m51.McKey = "7777";
//        m51.BlockNo = "0003";
//        m51.Response = "0";
//        proxy.addSndMsg(m51);
    }

    public void Do(Message35 message35) {

        System.out.println(message35.getID() + message35.getPlcName());

        try {
            Transaction.begin();

            Message05 m5 = new Message05();
            m5.setPlcName(message35.getPlcName());
            m5.McKey = message35.McKey;
            m5.BlockNo = message35.MachineNo;
            m5.Response = "0";
            m5.setPlcName(message35.getPlcName());



            Query wcsQ = HibernateUtil.getCurrentSession().createQuery("from WcsMessage where dock=:dock and msgType=:msgType and mcKey=:mckey");
            wcsQ.setParameter("dock", message35.Dock);
            wcsQ.setParameter("msgType", WcsMessage.MSGTYPE_35);
            wcsQ.setParameter("mckey", message35.McKey);
            List<WcsMessage> msg35List = wcsQ.list();
            if (msg35List.isEmpty()) {
                WcsMessage.save35(message35);
                AsrsJob aj = AsrsJob.getAsrsJobByMcKey(message35.McKey);
                Block block = Block.getByBlockNo(message35.MachineNo);
                Msg35ProcService msg35ProcService=null;

                if (aj != null) {
                    if (AsrsJobType.PUTAWAY.equals(aj.getType())) {
                        msg35ProcService = new Msg35ProPutawayServiceImpl(message35,aj,block);

                    } else if (AsrsJobType.RETRIEVAL.equals(aj.getType())) {
                        msg35ProcService = new Msg35ProRetrievalServiceImpl(message35,aj,block);

                    } else if (AsrsJobType.RECHARGED.equals(aj.getType())) {
                        msg35ProcService = new Msg35ProcRechargedServiceImpl(message35,aj,block);

                    } else if (AsrsJobType.RECHARGEDOVER.equals(aj.getType())) {
                        msg35ProcService = new Msg35ProcRechargedOverServiceImpl(message35,aj,block);

                    } else if (AsrsJobType.LOCATIONTOLOCATION.equals(aj.getType())) {
                        msg35ProcService = new Msg35ProcLTLServiceImpl(message35,aj,block);

                    } else if (AsrsJobType.ST2ST.equals(aj.getType())) {
                        msg35ProcService = new Msg35ProcST2STServiceImpl(message35,aj,block);

                    } else if (AsrsJobType.CHANGELEVEL.equals(aj.getType())) {
                        msg35ProcService = new Msg35ProcChangeLevelServiceImpl(message35,aj,block);

                    }
                } else if (message35.McKey.equals("9999")) {
                    if (StringUtils.isEmpty(block.getMcKey()) && StringUtils.isEmpty(block.getReservedMcKey())) {
                        msg35ProcService = new Msg35Proc9999ServiceImpl(message35,aj,block);
                    }
                }

                if(msg35ProcService!=null){
                    if (block instanceof Srm) {
                        msg35ProcService.srm35Proc();

                    } else if (block instanceof MCar) {
                        msg35ProcService.mCar35Proc();

                    } else if (block instanceof SCar) {
                        msg35ProcService.sCar35Proc();

                    } else if (block instanceof StationBlock) {
                        msg35ProcService.station35Proc();

                    } else if (block instanceof Conveyor) {
                        msg35ProcService.converyor35Proc();

                    }else if(block instanceof Lift){
                        msg35ProcService.lift35Proc();

                    }
                }

                block.setWaitingResponse(false);

                if (block instanceof Conveyor) {
                    Conveyor conveyor = (Conveyor) block;
                    conveyor.setMantWaiting(false);
                }else if(block instanceof MCar){
                    MCar mCar = (MCar) block;
                    if(!message35.isMove()){
                        mCar.setCheckLocation(false);
                    }else{
                        mCar.setCheckLocation(true);
                    }
                }else if(block instanceof Lift){
                    Lift lift=(Lift)block;
                    if(!message35.isMove()){
                        lift.setDock(null);
                    }
                }

            }

            wcsQ = HibernateUtil.getCurrentSession().createQuery("from WcsMessage where dock=:dock and msgType=:msgType and mcKey=:mckey");
            wcsQ.setParameter("dock", message35.Dock);
            wcsQ.setParameter("msgType", WcsMessage.MSGTYPE_03);
            wcsQ.setParameter("mckey", message35.McKey);
            List<WcsMessage> msg03List = wcsQ.list();
            for(WcsMessage msg03 : msg03List){
                msg03.setReceived(true);
            }

            Transaction.commit();

            MessageProxy _wcsproxy = (MessageProxy) Naming.lookup(Const.WCSPROXY);

            _wcsproxy.addSndMsg(m5);

        } catch (LockAcquisitionException e) {
            Transaction.rollback();
            e.printStackTrace();
            LogWriter.error(LoggerType.ERROR, LogWriter.getStackTrace(e));
            LogWriter.error(LoggerType.ERROR,String.format("msg35proc LockAcquisitionException, mckey=%s,machineNo=%s,cycleOrder=%s", message35.McKey,message35.MachineNo,message35.CycleOrder));
            try {
                Random random = new Random();
                Thread.sleep(random.nextInt(10)*300);
                //发生死锁重做
                Do(message35);
            }catch (Exception e1){
                e1.printStackTrace();
            }
        } catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
            LogWriter.error(LoggerType.ERROR, LogWriter.getStackTrace(e));
        }
    }







}
