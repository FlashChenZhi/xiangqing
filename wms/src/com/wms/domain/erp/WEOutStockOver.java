package com.wms.domain.erp;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: ed_chen
 * @Date: Create in 16:03 2018/7/25
 * @Description:
 * @Modified By:
 */
@Entity
@Table(name = "WEINSTOCKOUTOVER")
public class WEOutStockOver {
    private int id;
    private String barcode;//堆垛码
    private String orderNo;//出库单号（由于ERP不会给与WMS出库单号所以由WMS自身的出库单号来）
    private int wareId;//品号id
    private int wareNum;//数量（桶为单位）
    private String locationNo;//库存位置
    private String outType;//出库类型
    private int truckId;//车辆ID
    private Date createTime;//创建时间
    private int status;//读取状态（0未读，1已读）

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "BARCODE")
    @Basic
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    @Column(name = "ORDERNO")
    @Basic
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Column(name = "WARE_ID")
    @Basic
    public int getWareId() {
        return wareId;
    }

    public void setWareId(int wareId) {
        this.wareId = wareId;
    }

    @Column(name = "WARE_NUM")
    @Basic
    public int getWareNum() {
        return wareNum;
    }

    public void setWareNum(int wareNum) {
        this.wareNum = wareNum;
    }

    @Column(name = "LOCATIONNO")
    @Basic
    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    @Column(name = "OUTTYPE")
    @Basic
    public String getOutType() {
        return outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    @Column(name = "TRUCK_ID")
    @Basic
    public int getTruckId() {
        return truckId;
    }

    public void setTruckId(int truckId) {
        this.truckId = truckId;
    }

    @Column(name = "CREATETIME")
    @Basic
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "STATUS")
    @Basic
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
