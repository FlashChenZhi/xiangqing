package com.wms.domain.erp;

import com.util.hibernate.HibernateERPUtil;
import org.hibernate.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 9:43 2018/7/24
 * @Description:入库表
 * @Modified By:
 */
@Entity
@Table(name = "WEINSTOCK")
public class WEInStock {
    private int id;
    private String barcode;//堆垛码
    private int line;//生产线
    private Date createTime;//创建时间
    private String batch;//批次
    private int skuCode;//商品id
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

    @Column(name = "LINE")
    @Basic
    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Column(name = "CREATETIME")
    @Basic
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "BATCH")
    @Basic
    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    @Column(name = "WARE_ID")
    @Basic
    public int getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(int skuCode) {
        this.skuCode = skuCode;
    }

    @Column(name = "STATUS")
    @Basic
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static List<WEInStock> findUnReadWEInStock(){
        Query query = HibernateERPUtil.getCurrentSession().createQuery("from WEInStock where status=0");
        List<WEInStock> weInStocks=query.list();
        return  weInStocks;
    }
}
