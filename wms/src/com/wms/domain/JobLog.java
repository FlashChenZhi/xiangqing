package com.wms.domain;

import com.util.hibernate.HibernateERPUtil;
import com.util.hibernate.HibernateUtil;
import com.wms.domain.erp.Truck;
import org.hibernate.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by van on 2018/1/14.
 */
@Entity
@Table(name = "JOBLOG")
public class JobLog {
    public static final String __TYPE = "type";
    public static final String __CONTAINER = "container";
    public static final String __FROMLOCATIONNO = "fromLocation";
    public static final String __TOLOCATIONNO = "toLocation";
    public static final String __FROMSTATION = "fromStation";
    public static final String __TOSTATION = "toStation";
    public static final String __CREATEDATE = "createDate";
    public static final String __ORDERNO = "orderNo";

    private int id;
    private String mckey;
    private String orderNo;
    private String fromLocation;
    private String toLocation;
    private String container;
    private String type;
    private String fromStation;
    private String toStation;
    private Date createDate;
    private String createUser;
    private String status;
    private String skuCode;
    private String skuName;
    private BigDecimal _qty;
    private String lotNum;

    public static final String COL_ID = "id";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SKUCODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "SKUNAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
    @Basic
    @Column(name = "LOTNUM")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    @Column(name = "QTY")
    @Basic
    public BigDecimal getQty() {
        return _qty;
    }

    public void setQty(BigDecimal qty) {
        _qty = qty;
    }

    @Basic
    @Column(name = "MCKEY")
    public String getMckey() {
        return mckey;
    }

    public void setMckey(String mckey) {
        this.mckey = mckey;
    }

    @Basic
    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Basic
    @Column(name = "FROMLOCATION")
    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    @Basic
    @Column(name = "TOLOCATION")
    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    @Basic
    @Column(name = "CONTAINER")
    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    @Basic
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "FROMSTATION")
    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    @Basic
    @Column(name = "CREATEDATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "CREATEUSER")
    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    @Basic
    @Column(name = "TOSTATION")
    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static List<JobLog> findUnReadJobLog(){
        Query query = HibernateUtil.getCurrentSession().createQuery("from JobLog where status='0' ");
        List<JobLog> jobLogList=query.list();
        return  jobLogList;
    }
}
