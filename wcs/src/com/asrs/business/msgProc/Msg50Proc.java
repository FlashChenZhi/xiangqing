
package com.asrs.business.msgProc;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.*;
import com.asrs.message.Message50;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Receiver;
import com.domain.XMLbean.XMLList.ControlArea.RefId;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.LoadUnitAtIdDA;
import com.domain.XMLbean.XMLList.DataArea.*;
import com.domain.XMLbean.XMLList.LoadUnitAtID;
import com.domain.consts.xmlbean.XMLConstant;
import com.thread.blocks.*;
import com.util.common.LogWriter;
import com.util.common.LoggerType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg50Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message50 message50 = new Message50(msg.DataString);
        message50.setPlcName(msg.PlcName);
        Do(message50);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;

    public static void main(String[] args) throws Exception {
        Msg50Proc proc = new Msg50Proc();
        Message50 m50 = new Message50("1120510002000000000000000000000000");
        //002050019085413101199992OPWJ03007_____10000000069
        proc.Do(m50);
//        String barcode = "2OPWJ03007_____";
//        System.out.println(barcode.substring(1).replaceAll("_",""));

    }

    public void Do(Message50 message50) {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            for (Map.Entry<String, Message50.Block> entry : message50.MachineNos.entrySet()) {
                String blockNo = entry.getKey();
                StationBlock block1 = StationBlock.getByStationNo(blockNo);
                if (block1 instanceof StationBlock) {
                    Station station = Station.getStation(((StationBlock) block1).getStationNo());
                    if (AsrsJobType.PUTAWAY.equals(station.getMode()) && "1".equals(entry.getValue().Load)) {
                        if (StringUtils.isEmpty(block1.getMcKey())) {
                            Configuration configuration = Configuration.getConfig(Configuration.KEY_RUNMODEL);
                            if (configuration.getValue().equals(Configuration.MODEL_ONLINE)) {
                                //有子车电量不足
//                            List<SCar> sCars = HibernateUtil.getCurrentSession().createQuery("from SCar where power<30 and wareHouse=:po").setParameter("po", block1.getWareHouse()).list();
//                            List<AsrsJob> chargeJob = HibernateUtil.getCurrentSession().createQuery("from AsrsJob where type=:tp and wareHouse=:wh").setParameter("tp", AsrsJobType.RECHARGED)
//                                    .setParameter("wh", block1.getWareHouse()).list();

                                //if (sCars.isEmpty() && chargeJob.isEmpty()) {
                                for (Map.Entry<Integer, Map<String, String>> entry1 : entry.getValue().McKeysAndBarcodes.entrySet()) {
                                    for (Map.Entry<String, String> entry2 : entry1.getValue().entrySet()) {
                                        if (entry2.getValue().indexOf("???") == -1) {
                                            Sender sender = new Sender();
                                            sender.setDivision(XMLConstant.COM_DIVISION);
                                            Receiver receiver = new Receiver();
                                            receiver.setDivision(XMLConstant.WMS_DIVISION);

                                            ControlArea controlArea = new ControlArea();
                                            controlArea.setSender(sender);
                                            controlArea.setReceiver(receiver);
                                            controlArea.setCreationDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                                            RefId refId = new RefId();
                                            refId.setId(999999);
                                            controlArea.setRefId(refId);

                                            XMLLocation xmlLocation = new XMLLocation();
                                            xmlLocation.setMHA(station.getStationNo());

                                            List<String> list = new ArrayList<>(3);
                                            list.add("");
                                            list.add("");
                                            list.add("");
                                            xmlLocation.setRack(list);

                                            LoadUnitAtIdDA loadUnitAtIdDA = new LoadUnitAtIdDA();
                                            loadUnitAtIdDA.setXMLLocation(xmlLocation);
                                            loadUnitAtIdDA.setScanDate(entry2.getValue());
                                            loadUnitAtIdDA.setLoadType("00");
                                            loadUnitAtIdDA.setWeight(entry.getValue().weight);

                                            LoadUnitAtID loadUnitAtID = new LoadUnitAtID();
                                            loadUnitAtID.setControlArea(controlArea);
                                            loadUnitAtID.setDataArea(loadUnitAtIdDA);

                                            Envelope envelope = new Envelope();
                                            envelope.setLoadUnitAtID(loadUnitAtID);
                                            XMLMessage xmlMessage = new XMLMessage();
                                            xmlMessage.setStatus("1");
                                            xmlMessage.setRecv("WMS");
                                            xmlMessage.setMessageInfo(XMLUtil.getSendXML(envelope));
                                            HibernateUtil.getCurrentSession().save(xmlMessage);
                                            XMLUtil.sendEnvelope(envelope);

                                            block1.setLoad("1");
                                        } else {
                                            SystemLog.error(station.getStationNo() + "NoRead");
                                            InMessage.error(blockNo,"NoRead");
                                        }
                                    }
                                }

//                            } else {
//                                SystemLog.error("子车存在充电任务");
//                                InMessage.error(blockNo,"子车存在充电任务");
//                            }
                            } else {


                                Query q = HibernateUtil.getCurrentSession().createQuery("from AsrsJobTest where fromStation=:station order by id asc").setMaxResults(1);
                                q.setParameter("station", station.getStationNo());
                                AsrsJobTest test = (AsrsJobTest) q.uniqueResult();

                                if (test != null) {
                                    AsrsJob asrsJob = new AsrsJob();
                                    asrsJob.setType("01");
                                    asrsJob.setFromStation(block1.getBlockNo());
                                    asrsJob.setToStation(test.getToStation());
                                    asrsJob.setToLocation(test.getToLocation());
                                    asrsJob.setFromLocation(test.getFromLocation());
                                    asrsJob.setMcKey(StringUtils.leftPad(HibernateUtil.nextSeq("seq_mckey") + "", 4, "0"));
                                    asrsJob.setStatus("1");
                                    asrsJob.setStatusDetail("0");
                                    asrsJob.setWareHouse(block1.getWareHouse());
                                    block1.setMcKey(asrsJob.getMcKey());
                                    HibernateUtil.getCurrentSession().save(asrsJob);
                                    HibernateUtil.getCurrentSession().delete(test);

                                }
                            }
                        }else{
                            System.out.println("入库站台有mckey");
                        }
                    } else if (AsrsJobType.RETRIEVAL.equals(station.getMode()) && "0".equals(entry.getValue().Load)) {

                        for (Map.Entry<Integer, Map<String, String>> entry1 : entry.getValue().McKeysAndBarcodes.entrySet()) {
                            Map<String, String> mapValue = entry1.getValue();
                            for (Map.Entry<String, String> entry2 : mapValue.entrySet()) {
                                String mckey = entry2.getKey();
                                AsrsJob asrsJob = AsrsJob.getAsrsJobByMcKey(mckey);
                                if (asrsJob != null) {
                                    if (asrsJob.getStatus().equals(AsrsJobStatus.DONE)) {
//                                        asrsJob.delete();
                                        block1.setMcKey(null);
                                        block1.setOutLoad(false);
                                    } else {
                                        asrsJob.setStatus(AsrsJobStatus.DONE);
                                        block1.setMcKey(null);
                                        block1.setOutLoad(false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Transaction.commit();
        } catch (Exception e) {
            LogWriter.error(LoggerType.ERROR,"Msg50错误："+e.getMessage());
            e.printStackTrace();
            Transaction.rollback();
        }
    }

}


