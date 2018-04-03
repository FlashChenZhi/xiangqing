package com.asrs.thread;

import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.domain.XMLbean.Envelope;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.ControlArea.RefId;
import com.asrs.domain.XMLbean.XMLList.ControlArea.Sender;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.TransportOrderDA;
import com.asrs.domain.XMLbean.XMLList.DataArea.StUnit;
import com.asrs.domain.XMLbean.XMLList.DataArea.ToLocation;
import com.asrs.domain.XMLbean.XMLList.TransportOrder;
import com.asrs.business.consts.TransportType;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.asrs.xml.util.XMLUtil;
import com.util.common.DateFormat;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Inventory;
import com.wms.domain.Job;
import com.wms.domain.Location;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.Date;

/**
 * Created by Administrator on 2016/12/20.
 */
public class PutawayThread implements Runnable {

    @Override
    public void run() {
//        while (true) {
//            try {
//                Transaction.begin();
//                Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(Job.class);
//                criteria.add(Restrictions.eq("status", AsrsJobStatus.ARRIVAL));
//                criteria.setMaxResults(1);
//                Job job = (Job) criteria.uniqueResult();
//                if(job != null){
//                    job.setStatus(AsrsJobStatus.RUNNING);
//
//                    In inv = job.getContainer().getInventories().iterator().next();
//                    String skuCode = inv.getSku().getSkuCode();
//                    Location newLocation = Location.getEmptyLocation(skuCode);
//
//                    newLocation.setReserved(true);
//                    job.setToLocation(newLocation);
//
//                    //开始
//                    //创建ControlArea控制域对象
//                    ControlArea ca = new ControlArea();
//                    Sender sd = new Sender();
//                    sd.setDivision(XMLConstant.COM_DIVISION);
//                    ca.setSender(sd);
//
//                    RefId ri = new RefId();
//                    ri.setReferenceId(job.getMcKey());
//                    ca.setRefId(ri);
//                    ca.setCreationDateTime(new DateFormat().format(new Date()));
//
//                    //创建TransportOrderDA数据域对象
//                    TransportOrderDA toa = new TransportOrderDA();
//
//                    toa.setTransportType(TransportType.PUTAWAY);
//                    ToLocation toLocation = new ToLocation();
//                    toLocation.setMHA(job.getFromStation());
//                    toLocation.setRack(newLocation.getLocationNo());
//                    toLocation.setX(String.valueOf(newLocation.getBank()));
//                    toLocation.setY(String.valueOf(newLocation.getBay()));
//                    toLocation.setZ(String.valueOf(newLocation.getLevel()));
//                    toa.setToLocation(toLocation);
//                    StUnit su = new StUnit();
//                    su.setStUnitID(job.getContainer().getBarcode());
//                    toa.setStUnit(su);
//                    toa.setToLocation(toLocation);
//
//                    //创建TransportOrder核心对象
//                    TransportOrder to = new TransportOrder();
//                    to.setControlArea(ca);
//                    to.setDataArea(toa);
//                    //结束
//                    Envelope el = new Envelope();
//                    el.setTransportOrder(to);
//                    XMLUtil.sendEnvelope(el);
//                }
//                Thread.sleep(100);
//                Transaction.commit();
//
//            } catch (Exception e) {
//                Transaction.rollback();
//                e.printStackTrace();
//            }
//        }
    }
}
