package com.wms.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by van on 2018/1/15.
 */
@Entity
@Table(name = "INVENTORY_LOG")
public class InventoryLog {

    public static final String COL_ID = "id";
    public static final String COL_SKUCODE = "skuCode";
    public static final String COL_FROMLOCATION = "fromLocation";
    public static final String COL_TYPE = "type";
    public static final String COL_TOLOCATION ="toLocation";
    public static final String COL_WHCODE = "whCode";
    public static final String COL_CREATEDATE = "createDate";
    public static final String COL_CONTAINER = "container";
    public static final String COL_ORDERNO = "orderNo";

    private int id;
    private String skuCode;
    private String skuName;
    private BigDecimal qty;
    private Date createDate;
    private String fromLocation;
    private String toLocation;
    private String lotNum;
    private String whCode;
    private String type;
    private String container;
    private String orderNo;
    private BigDecimal volumn;

    public static final String TYPE_IN = "01";
    public static final String TYPE_OUT = "02";
    public static final String TYPE_LTOL = "03";
    public static final String TYPE_STOS = "04";
    public static final String TYPE_FROZEN = "05";
    public static final String TYPE_REALEASE = "06";


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
    @Column(name = "SKU_CODE")
    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    @Basic
    @Column(name = "QTY")
    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    @Basic
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "FROM_LOCATION")
    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    @Basic
    @Column(name = "TO_LOCATION")
    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    @Basic
    @Column(name = "LOT_NUM")
    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum;
    }

    @Basic
    @Column(name = "WH_CODE")
    public String getWhCode() {
        return whCode;
    }

    public void setWhCode(String whCode) {
        this.whCode = whCode;
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
    @Column(name = "CONTAINER")
    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    @Basic
    @Column(name = "ORDERNO")
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Basic
    @Column(name = "SKU_NAME")
    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    @Basic
    @Column(name = "VOLUMN")
    public BigDecimal getVolumn() {
        return volumn;
    }

    public void setVolumn(BigDecimal volumn) {
        this.volumn = volumn;
    }
}
