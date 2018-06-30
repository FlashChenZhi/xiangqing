package com.asrs.thread;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.TransportType;
import com.asrs.domain.XMLMessage.XMLMessage;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.FromLocation;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.asrs.domain.XMLbean.XMLList.TransportOrder;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.asrs.xml.util.XMLUtil;
import com.util.common.DateFormat;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Container;
import com.wms.domain.Job;
import com.wms.domain.Location;
import com.wms.domain.Station;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */
public class RetrievalThread implements Runnable{

    @Override
    public void run() {
        while (true){

            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();
                Query query = session.createQuery("from Station s where s.mode = :mode")
                        .setString("mode", AsrsJobType.RETRIEVAL);
                List<Station> stations = query.list();
                for(Station station : stations) {
                    /*//查询抽检移库任务
                    query = HibernateUtil.getCurrentSession().createQuery("from Job  where sendReport=false and type=:tp  order by fromLocation.position asc, fromLocation.bay asc,fromLocation.level asc,fromLocation.seq2 asc")
                            .setParameter("tp", AsrsJobType.LOCATIONTOLOCATION);

                    List<Job> ST2STjobList =  query.list();
                    if (ST2STjobList.size()!=0) {
                        for(Job job:ST2STjobList){
                            createYiKuJob(job, TransportType.LOCATIONTOLOCATION);
                            job.setSendReport(true);
                        }
                    }
                    //查询抽检出库任务
                    query = HibernateUtil.getCurrentSession().createQuery("from Job  where sendReport=false and type=:tp and toStation = :station order by fromLocation.position asc, fromLocation.bay asc,fromLocation.level asc,fromLocation.seq2 asc")
                            .setParameter("tp", AsrsJobType.CHECKOUTSTORAGE)
                            .setString("station",station.getStationNo()).setMaxResults(1);
                    Job job = (Job) query.uniqueResult();
                    if (job != null) {
                        createOutJob(job, TransportType.CHECKOUTSTORAGE);
                        job.setSendReport(true);
                    }

                    //查询抽检移库回库任务
                    query = HibernateUtil.getCurrentSession().createQuery("from Job  where sendReport=false and type=:tp  order by fromLocation.position asc, fromLocation.bay asc,fromLocation.level desc,fromLocation.seq2 asc")
                            .setParameter("tp", AsrsJobType.BACK_PUTAWAY);

                    List<Job> ST2STjobList2 =  query.list();
                    if (ST2STjobList2.size()!=0) {
                        System.out.println("进入回库任务");
                        for(Job job2:ST2STjobList2){
                            createYiKuJob(job2, TransportType.BACK_PUTAWAY);
                            job2.setSendReport(true);
                        }
                    }*/

                    //查询出库任务
                    query = HibernateUtil.getCurrentSession().createQuery("from Job  where sendReport=false and type=:tp and toStation = :station order by fromLocation.position asc, fromLocation.bay asc,fromLocation.level asc,fromLocation.seq desc")
                            .setParameter("tp", AsrsJobType.RETRIEVAL)
                            .setString("station",station.getStationNo()).setMaxResults(1);

                    Job job = (Job) query.uniqueResult();
                    if (job != null) {
                        createOutJob(job, TransportType.RETRIEVAL);
                        job.setSendReport(true);
                    }
                }

                Transaction.commit();

            } catch (Exception e) {
                Transaction.rollback();
                e.printStackTrace();
            } finally {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private static void createOutJob(Job job, String asrsJobType) throws Exception {
        Location location = job.getFromLocation();
        location.setRetrievalRestricted(true);
        //获取容器Container
        Container container = Container.getByBarcode(job.getContainer().trim());
        container.setReserved(true);
        HibernateUtil.getCurrentSession().update(container);

        //创建FromLocation对象
        FromLocation fromLocation = new FromLocation();
        List<String> locations = new ArrayList<>();
        locations.add(location.getBank() + "");
        locations.add(location.getBay() + "");
        locations.add(location.getLevel() + "");
        fromLocation.setRack(locations);

        fromLocation.setMHA(job.getFromStation());

        ToLocation toLocation = new ToLocation();
        toLocation.setMHA(job.getToStation());

        StUnit stUnit = new StUnit();

        stUnit.setStUnitID(container.getBarcode());
        //创建TransportOrderDA对象
        TransportOrderDA transportOrderDA = new TransportOrderDA();
        transportOrderDA.setTransportType(asrsJobType);//transportOrderType 类型
        transportOrderDA.setFromLocation(fromLocation);
        transportOrderDA.setToLocation(toLocation);
        transportOrderDA.setStUnit(stUnit);

        //创建sender对象
        Sender sender = new Sender();
        sender.setDivision(XMLConstant.COM_DIVISION);

        RefId refId = new RefId();
        refId.setReferenceId(job.getMcKey());

        //创建ControlArea对象
        ControlArea controlArea = new ControlArea();
        controlArea.setRefId(refId);
        controlArea.setCreationDateTime(DateFormat.format(new Date(), DateFormat.YYYYMMDDHHMMSS));
        controlArea.setSender(sender);

        //wms发送给wcs的运输命令
        TransportOrder transportOrder = new TransportOrder();
        transportOrder.setDataArea(transportOrderDA);
        transportOrder.setControlArea(controlArea);

        Envelope envelope = new Envelope();
        envelope.setTransportOrder(transportOrder);

        XMLUtil.sendEnvelope(envelope);
        /*XMLMessage xmlMessage = new XMLMessage();
        xmlMessage.setRecv("WCS");
        xmlMessage.setStatus("1");
        xmlMessage.setMessageInfo(XMLUtil.getSendXML(envelope));

        HibernateUtil.getCurrentSession().save(xmlMessage);*/

    }

    private static void createYiKuJob(Job job, String asrsJobType) throws Exception {
        System.out.println("进入生成移库任务！");
        Location location = job.getFromLocation();
        location.setRetrievalRestricted(true);
        //到达货位要不要设置入库属性为true
        Location gotToLocation = job.getToLocation();

        //获取容器Container
        Container container = Container.getByBarcode(job.getContainer());
        container.setReserved(true);
        HibernateUtil.getCurrentSession().update(container);

        //创建FromLocation对象
        FromLocation fromLocation = new FromLocation();

        List<String> locations = new ArrayList<>();
        locations.add(location.getBank() + "");
        locations.add(location.getBay() + "");
        locations.add(location.getLevel() + "");
        fromLocation.setRack(locations);

        fromLocation.setMHA(job.getFromStation());
        //创建toLocation对象（移库与出库不同）
        ToLocation toLocation = new ToLocation();

        toLocation.setMHA(job.getToStation());

        List<String> locations2 = new ArrayList<>();
        locations2.add(gotToLocation.getBank() + "");
        locations2.add(gotToLocation.getBay() + "");
        locations2.add(gotToLocation.getLevel() + "");
        toLocation.setRack(locations2);

        StUnit stUnit = new StUnit();

        stUnit.setStUnitID(container.getBarcode());
        //创建TransportOrderDA对象
        TransportOrderDA transportOrderDA = new TransportOrderDA();
        transportOrderDA.setTransportType(asrsJobType);//transportOrderType 类型
        transportOrderDA.setFromLocation(fromLocation);
        transportOrderDA.setToLocation(toLocation);
        transportOrderDA.setStUnit(stUnit);

        //创建sender对象
        Sender sender = new Sender();
        sender.setDivision(XMLConstant.COM_DIVISION);

        RefId refId = new RefId();
        refId.setReferenceId(job.getMcKey());

        //创建ControlArea对象
        ControlArea controlArea = new ControlArea();
        controlArea.setRefId(refId);
        controlArea.setCreationDateTime(DateFormat.format(new Date(), DateFormat.YYYYMMDDHHMMSS));
        controlArea.setSender(sender);

        //wms发送给wcs的运输命令
        TransportOrder transportOrder = new TransportOrder();
        transportOrder.setDataArea(transportOrderDA);
        transportOrder.setControlArea(controlArea);

        Envelope envelope = new Envelope();
        envelope.setTransportOrder(transportOrder);
        XMLUtil.sendEnvelope(envelope);
        /*XMLMessage xmlMessage = new XMLMessage();
        xmlMessage.setRecv("WCS");
        xmlMessage.setStatus("1");
        xmlMessage.setMessageInfo(XMLUtil.getSendXML(envelope));

        HibernateUtil.getCurrentSession().save(xmlMessage);*/
        System.out.println("完成生成移库任务！");
    }

}
