package com.wms.domain.erp;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: ed_chen
 * @Date: Create in 9:51 2018/7/24
 * @Description:入库完成表
 * @Modified By:
 */
@Entity
@Table(name = "WEINSTOCKOVER")
public class WEInStockOver {
    private int id;
    private String barcode;//堆垛码
    private String newLocationNo;//最新位置
    private String createTime;//创建时间
    private String locationNo;//位置
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

    @Column(name = "NEWLOCATIONNO")
    @Basic
    public String getNewLocationNo() {
        return newLocationNo;
    }

    public void setNewLocationNo(String newLocationNo) {
        this.newLocationNo = newLocationNo;
    }

    @Column(name = "LOCATIONNO")
    @Basic
    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    @Column(name = "CREATETIME")
    @Basic
    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
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
