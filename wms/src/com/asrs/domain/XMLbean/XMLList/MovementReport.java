package com.asrs.domain.XMLbean.XMLList;

import com.asrs.business.consts.RetrievalOrderStatus;
import com.util.common.Const;

import com.wms.domain.*;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.MovementReportDA;
import com.asrs.domain.XMLbean.XMLProcess;
import com.asrs.business.consts.ReasonCode;
import com.asrs.domain.consts.xmlbean.XMLConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.*;
import org.hibernate.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-5
 * Time: 下午3:18
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "MovementReport")
public class MovementReport extends XMLProcess {
    @XStreamAlias("version")
    @XStreamAsAttribute
    private String version = XMLConstant.COM_VERSION;

    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private MovementReportDA dataArea;

    @OneToOne(targetEntity = ControlArea.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "ControlAreaID", updatable = true)
    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    @OneToOne(targetEntity = MovementReportDA.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "MovementReportDAID", updatable = true)
    public MovementReportDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(MovementReportDA dataArea) {
        this.dataArea = dataArea;
    }

    @XStreamOmitField
    private int id;


    @Id
    @SequenceGenerator(name = "sequenceGenerator", sequenceName = "MOVEMENTREPORT_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "sequenceGenerator", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void execute() {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            String mcKey = controlArea.getRefId().getReferenceId();
            Job j = Job.getByMcKey(mcKey);
            if(j != null) {
                if (dataArea.getReasonCode().equals(ReasonCode.PUTAWAYFINISHED)) {
                    Location l = j.getToLocation();
                    l.setReserved(false);
                    l.setEmpty(false);
                    session.update(l);

                    Container container = null;

                    Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView where palletCode=:palletNo");
                    query.setParameter("palletNo", j.getContainer());
                    List<InventoryView> views = query.list();


                    container = Container.getByBarcode(j.getContainer());

                    if (container == null) {
                        container = new Container();
                        container.setBarcode(j.getContainer());
                        container.setLocation(l);
                        container.setCreateDate(new Date());
                        container.setCreateUser("sys");
                        container.setReserved(false);
                        HibernateUtil.getCurrentSession().save(container);
                    }
                    InventoryLog inventoryLog = new InventoryLog();

                    inventoryLog.setType(InventoryLog.TYPE_IN);
                    for (InventoryView view : views) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                        if (view != null) {
                            Inventory inventory = new Inventory();

                            inventory.setWhCode(view.getWhCode());
                            inventory.setSkuName(view.getSkuName());
                            inventory.setLotNum(view.getLotNum());
                            inventory.setQty(view.getQty());
                            inventory.setSkuCode(view.getSkuCode());
                            inventory.setContainer(container);
                            inventory.setStoreDate(sdf.format(new Date()));
                            inventory.setStoreTime(sdf2.format(new Date()));

                            j.getJobDetails().iterator().next().setInventory(inventory);

                            session.save(inventory);
                            inventoryLog.setQty(inventory.getQty());
                            inventoryLog.setSkuCode(inventory.getSkuCode());
                            inventoryLog.setWhCode(inventory.getWhCode());
                            inventoryLog.setToLocation(container.getLocation().getLocationNo());
                            inventoryLog.setLotNum(inventory.getLotNum());
                            inventoryLog.setSkuName(inventory.getSkuName());

                            session.delete(view);
                        }

                    }
                    inventoryLog.setContainer(container.getBarcode());
                    inventoryLog.setCreateDate(new Date());
                    session.save(inventoryLog);


                } else if (dataArea.getReasonCode().equals(ReasonCode.RETRIEVALFINISHED)) {
//                Container c = j.getContainer();
//                Location location = j.getFromLocation();
//                location.setEmpty(true);
//                location.setRetrievalRestricted(false);
//                session.update(location);
//                session.delete(c);
//
//                //// TODO: 放在后台
//                WebService.finishOrder(j);


                    Location fromLocation = j.getFromLocation();

                    for(Container container : fromLocation.getContainers()) {
                        List<Inventory> inventorySet = new ArrayList<>(container.getInventories());
                        for (Inventory inventory : inventorySet) {
                            if(j.getOrderNo()!=null && !"dingdian".equals(j.getOrderNo())){
                                //获取订单
                                RetrievalOrder retrievalOrder =RetrievalOrder.getByOrderNo(j.getOrderNo());
                                boolean flag=true;
                                for(RetrievalOrderDetail retrievalOrderDetail: retrievalOrder.getRetrievalOrderDetailSet()){
                                    //获取订单详情
                                    if(retrievalOrderDetail.getItemCode().equals(inventory.getSkuCode()) && retrievalOrderDetail.getBatch().equals(inventory.getLotNum())){
                                        //若订单详情的商品代码和批次与库存相同
                                        int completeNum=retrievalOrderDetail.getCompleteNum();
                                        retrievalOrderDetail.setCompleteNum(completeNum+inventory.getQty().intValue());
                                    }
                                    if(retrievalOrderDetail.getCompleteNum()<retrievalOrderDetail.getQty().intValue()){
                                        flag=false;
                                    }
                                    HibernateUtil.getCurrentSession().saveOrUpdate(retrievalOrderDetail);
                                }
                                if(flag){
                                    retrievalOrder.setStatus(RetrievalOrderStatus.OVER);
                                }
                                HibernateUtil.getCurrentSession().saveOrUpdate(retrievalOrder);
                            }
                            HibernateUtil.getCurrentSession().delete(inventory);
                        }
                        HibernateUtil.getCurrentSession().delete(container);
                    }

                    fromLocation.setReserved(false);
                    fromLocation.setEmpty(true);
                    fromLocation.setRetrievalRestricted(false);

                } else if (dataArea.getReasonCode().equals(ReasonCode.LOCATIONTOLOCATION)) {

                    /*Location fromLocation = j.getFromLocation();
                    fromLocation.setReserved(false);
                    fromLocation.setEmpty(true);

                    Location toLocation = j.getToLocation();
                    toLocation.setEmpty(false);
                    toLocation.setReserved(false);

                    for(Container container : fromLocation.getContainers()) {

                        List<Inventory> inventorySet = new ArrayList<>(container.getInventories());
                        for (Inventory inventory : inventorySet) {
                            Transfer.transfer(toLocation, inventory);
                        }


                        container.setLocation(toLocation);
                    }*/
                }
                session.delete(j);
                j.writeLog();
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }

    }
}
