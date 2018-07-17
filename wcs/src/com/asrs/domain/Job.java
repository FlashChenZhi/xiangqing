package com.asrs.domain;

import com.util.hibernate.HibernateUtil;
import com.asrs.domain.JobDetail;
import com.asrs.domain.Location;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Author: Zhouyue
 * Date: 2010-11-12
 * Time: 12:19:30
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "JOB")
public class Job {
    public static final String __CONTAINER = "container";

    public static final String __FROMLOCATION = "fromLocation";
    public static final String __TOLOCATION = "toLocation";
    public static final String __TYPE = "type";
    public static final String __ASRSJOB = "asrsJob";
    public static final String __FROMSTATION = "_fromStation";
    public static final String __TOSTATION = "toStation";
    public static final String __ID = "id";

    private Logger logger = Logger.getLogger(Job.class);

    private int _id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    private String _fromStation;

    @Column(name = "FROMSTATION")
    @Basic
    public String getFromStation() {
        return _fromStation;
    }

    public void setFromStation(String fromStation) {
        _fromStation = fromStation;
    }



    private BigDecimal qty;

    @Basic
    @Column(name = "QTY")
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    private String mcKey;

    @Column(name = "mckey")
    @Basic
    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    private String _toStation;


    @Column(name = "TOSTATION")
    @Basic
    public String getToStation() {
        return _toStation;
    }

    public void setToStation(String toStation) {
        _toStation = toStation;
    }

    private Date _createDate;

    @Column(name = "CREATEDATE")
    @Basic
    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        _createDate = createDate;
    }

    private String _createUser;

    @Column(name = "CREATEUSER")
    @Basic
    public String getCreateUser() {
        return _createUser;
    }

    public void setCreateUser(String createUser) {
        _createUser = createUser;
    }

    private String _type;

    @Column(name = "TYPE")
    @Basic
    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }

    private String _status;

    @Column(name = "STATUS")
    @Basic
    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    private String skuCode;

    private String lotNum;

    @Basic
    @Column(name = "SKU_CODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "LOTNUM")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (_id != job._id) return false;
        if (_createDate != null ? !_createDate.equals(job._createDate) : job._createDate != null) return false;
        if (_createUser != null ? !_createUser.equals(job._createUser) : job._createUser != null) return false;
        if (_fromLocation != null ? !_fromLocation.equals(job._fromLocation) : job._fromLocation != null) return false;
        if (_fromStation != null ? !_fromStation.equals(job._fromStation) : job._fromStation != null) return false;
        if (_jobDetails != null ? !_jobDetails.equals(job._jobDetails) : job._jobDetails != null) return false;
        if (_status != null ? !_status.equals(job._status) : job._status != null) return false;
        if (_toLocation != null ? !_toLocation.equals(job._toLocation) : job._toLocation != null) return false;
        if (_toStation != null ? !_toStation.equals(job._toStation) : job._toStation != null) return false;
        if (_type != null ? !_type.equals(job._type) : job._type != null) return false;
        if (logger != null ? !logger.equals(job.logger) : job.logger != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = logger != null ? logger.hashCode() : 0;
        result = 31 * result + _id;
        result = 31 * result + (_fromStation != null ? _fromStation.hashCode() : 0);
        result = 31 * result + (_toStation != null ? _toStation.hashCode() : 0);
        result = 31 * result + (_createDate != null ? _createDate.hashCode() : 0);
        result = 31 * result + (_createUser != null ? _createUser.hashCode() : 0);
        result = 31 * result + (_type != null ? _type.hashCode() : 0);
        result = 31 * result + (_status != null ? _status.hashCode() : 0);
        result = 31 * result + (_toLocation != null ? _toLocation.hashCode() : 0);
        result = 31 * result + (_fromLocation != null ? _fromLocation.hashCode() : 0);
        result = 31 * result + (_jobDetails != null ? _jobDetails.hashCode() : 0);
        return result;
    }

    private Location _toLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TOLOCATIONID", referencedColumnName = "ID")
    public Location getToLocation() {
        return _toLocation;
    }

    public void setToLocation(Location toLocation) {
        _toLocation = toLocation;
    }

    private Location _fromLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FROMLOCATIONID", referencedColumnName = "ID")
    public Location getFromLocation() {
        return _fromLocation;
    }

    public void setFromLocation(Location fromLocation) {
        _fromLocation = fromLocation;
    }

    private String container;

    @Basic
    @Column(name = "CONTAINER")
    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    private Collection<JobDetail> _jobDetails = new ArrayList<JobDetail>();

    @OneToMany(mappedBy = "job")
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.DELETE
    })
    public Collection<JobDetail> getJobDetails() {
        return _jobDetails;
    }

    private void setJobDetails(Collection<JobDetail> jobDetails) {
        _jobDetails = jobDetails;
    }

    public void addJobDetail(JobDetail jobDetail) {
        _jobDetails.add(jobDetail);
        jobDetail.setJob(this);
    }

    private String orderNo;

    @Basic
    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public void asrsDone() {
//        Session session = HibernateUtil.getCurrentSession();
//
//        writeLog();
//
//        this.getAsrsJob().writeLog();
//
//        boolean picking = false;
//        if (this.getType().equals(JobType.PUTAWAY)) {
//            this.getToLocation().setEmpty(false);
//            this.getToLocation().setReserved(false);
//            this.getContainer().setLocation(this.getToLocation());
//            if(this.getContainer().getPutawayDate() == null) {
//                this.getContainer().setPutawayDate(new Date());
//                ContainerInfo ci = ContainerInfo.getByBarcode(this.getContainer().getBarcode());
//                if(ci != null){
////                    ci.setProcessed(false);
//                    ci.setStatus(ContainerInfoStatus.STORAGE);
//                }
//            }
//            for (In inventory : this.getContainer().getInventories()) {
//                inventory.setAvailableQty(inventory.getQty());
//                this.getToLocation().setInvHash(inventory.getInvHash());
//            }
//        } else if (this.getType().equals(JobType.RETRIEVAL)) {
//            this.getFromLocation().setEmpty(true);
//            this.getFromLocation().setReserved(false);
//            this.getFromLocation().getContainers().remove(this.getContainer());
//            this.getFromLocation().setInvHash(-1);
//
//            for(JobDetail jd : this.getJobDetails()){
//                JobPlan jp = jd.getJobPlan();
//                if(jp != null){
//                    jp.setDoneQty(jp.getDoneQty() + jd.getQty());
//                }
//            }
//            for(In inventory : this.getContainer().getInventories()){
//                if(inventory.getAvailableQty() != 0){
//                    picking = true;
//                    break;
//                }
//            }
//        } else if (this.getType().equals(JobType.CYCLECOUNT)) {
//            this.getFromLocation().setReserved(false);
//            this.getFromLocation().setCyclecounting(false);
//            this.getContainer().setReserved(false);
//        }
//
//        //delete job
//        session.delete(this);
//
//        if (this.getType().equals(JobType.RETRIEVAL)) {
//            if(!picking) {
//                session.delete(this.getContainer());
//            }else {
//                for (In inventory : this.getContainer().getInventories()) {
//                    inventory.setQty(inventory.getAvailableQty());
//                    inventory.setAvailableQty(0);
//                }
//                this.getContainer().setLocation(Location.getByLocationNo(Const.PICKBACK_AREA));
//                this.getContainer().setReserved(false);
//            }
//        }
    }

    /*public void writeLog() {
        Session session = HibernateUtil.getCurrentSession();

        JobLog jl = new JobLog();

        session.save(jl);

        jl.setContainer(this.getContainer());
        jl.setCreateDate(new Date());
        jl.setCreateUser(this.getCreateUser());
        if (this.getFromLocation() != null) {
            jl.setFromLocation(this.getFromLocation().getLocationNo());
        }
        jl.setFromStation(this.getFromStation());
        if (this.getToLocation() != null) {
            jl.setToLocation(this.getToLocation().getLocationNo());
        }
        jl.setToStation(this.getToStation());
        jl.setType(this.getType());

    }*/

    public void asrsCancel() {
//        Session session = HibernateUtil.getCurrentSession();
//
//        this.getAsrsJob().writeLog();
//
//        if (this.getType().equals(JobType.PUTAWAY)) {
//            if (this.getToLocation() != null) {
//                this.getToLocation().setReserved(false);
//            }
//            this.getContainer().setReserved(false);
//            if(Const.PUTAWAY_AREA.equals(this.getContainer().getLocation().getLocationNo())) {
//                HibernateUtil.getCurrentSession().delete(this.getContainer());
//            }
//        } else if (this.getType().equals(JobType.RETRIEVAL)) {
//            this.getFromLocation().setReserved(false);
//            this.getContainer().setReserved(false);
//            for(In inventory : this.getContainer().getInventories()){
//                inventory.setAvailableQty(inventory.getQty());
//            }
//            for(JobDetail jd : this.getJobDetails()){
//                JobPlan jp = jd.getJobPlan();
//                if(jp != null){
//                    jp.setAllocateQty(jp.getAllocateQty() - jd.getQty() - jp.getCancelQty());
//                    jp.setCancelQty(0);
//                }
//            }
//        } else if (this.getType().equals(JobType.CYCLECOUNT)) {
//            this.getFromLocation().setReserved(false);
//            this.getFromLocation().setCyclecounting(false);
//        }
//
//        session.delete(this);
    }

    public static Job getById(int id) {
        return (Job) HibernateUtil.getCurrentSession().get(Job.class, id);
    }

    public static Job getByMcKey(String mcKey) {
        Session session = HibernateUtil.getCurrentSession();
        Query q = session.createQuery(" from Job j where j.mcKey = :mcKey")
                .setString("mcKey", mcKey);
        return (Job) q.uniqueResult();
    }


    public void pickingCancel() {
//        Session session = HibernateUtil.getCurrentSession();
//
//        this.getFromLocation().setReserved(false);
//        this.getFromLocation().setEmpty(true);
//        this.getFromLocation().setCyclecounting(false);
//        this.getFromLocation().getContainers().remove(this.getContainer());
//        this.getContainer().setReserved(false);
//        this.writeLog();
//
//        this.getAsrsJob().writeLog();
//
//
//        if(JobType.CYCLECOUNT.equals(this.getType())) {
//            this.getContainer().setLocation(Location.getByLocationNo(Const.CYCLECOUNT_AREA));
//        }else{
//            this.getContainer().setLocation(Location.getByLocationNo(Const.PICKBACK_AREA));
//            for(In inventory : this.getContainer().getInventories()){
//                inventory.setQty(inventory.getAvailableQty());
//            }
//        }
//        for(In inventory : this.getContainer().getInventories()){
//            inventory.setAvailableQty(0);
//        }
//
//        session.delete(this);

    }

    public static Job getByContainer(String fromLpnID) {
        Session session = HibernateUtil.getCurrentSession();
        Query q = session.createQuery(" from Job j where j.container = :container")
                .setString("container", fromLpnID);
        return (Job) q.uniqueResult();

    }
}
