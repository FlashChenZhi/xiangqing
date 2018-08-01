package com.wms.domain.blocks;

import com.util.hibernate.HibernateERPUtil;
import org.hibernate.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 15:54 2018/7/25
 * @Description:出库表
 * @Modified By:
 */
@Entity
@Table(name = "EOUTSTOCK")
public class EOutStock {
    private int id;
    private int wareId;//商品id
    private int wareNum;//数量（桶为单位）
    private String person;//订单录入员
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
    @Column(name = "PERSON")
    @Basic
    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
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

    public static List<EOutStock> findUnReadWEOutStock(){
        Query query = HibernateERPUtil.getCurrentSession().createQuery("from WEOutStock where status=0");
        List<EOutStock> weOutStockList=query.list();
        return  weOutStockList;
    }
}
