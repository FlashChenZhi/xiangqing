package com.asrs.domain.XMLbean.XMLList;

import com.util.common.Const;
import com.yili.NoticeReceivingPutAway;
import com.yili.RetreivalFinish;
import com.yili.Transfer;
import com.yili.WebService;
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

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
                    if(Const.EMPTY_PALLET.equals(j.getSkuCode())){
                        Container container = new Container();
                        container.setLocation(l);
                        container.setBarcode(j.getContainer());
                        l.setEmpty(false);
                        HibernateUtil.getCurrentSession().save(container);

                        Inventory inventory = new Inventory();
                        inventory.setSkuCode(j.getSkuCode());
                        inventory.setLotNum(j.getLotNum());
                        inventory.setQty(j.getQty());
                        inventory.setContainer(container);

                        HibernateUtil.getCurrentSession().save(inventory);
                    }else {
                        NoticeReceivingPutAway.notice(j.getContainer(), l);
                    }
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
                    RetreivalFinish.finish(j);

                    Location fromLocation = j.getFromLocation();

                    for(Container container : fromLocation.getContainers()) {
                        List<Inventory> inventorySet = new ArrayList<>(container.getInventories());
                        for (Inventory inventory : inventorySet) {
                            HibernateUtil.getCurrentSession().delete(inventory);
                        }
                        HibernateUtil.getCurrentSession().delete(container);
                    }

                    fromLocation.setReserved(false);
                    fromLocation.setEmpty(true);


                } else if (dataArea.getReasonCode().equals(ReasonCode.LOCATIONTOLOCATION)) {

                    Location fromLocation = j.getFromLocation();
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
                    }
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
