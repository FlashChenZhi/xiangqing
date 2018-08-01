package com.wms.domain.erp;

import com.util.hibernate.HibernateERPUtil;
import org.hibernate.Query;

import javax.persistence.*;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 15:51 2018/7/25
 * @Description:基础数据表
 * @Modified By:
 */
@Entity
@Table(name = "WESKU")
public class WESku {
    private int id;
    private String name;//品种名称
    private String unit;//单位
    private String models;//规格
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
    @Column(name = "NAME")
    @Basic
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "UNIT")
    @Basic
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    @Column(name = "MODELS")
    @Basic
    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    @Column(name = "STATUS")
    @Basic
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static List<WESku> findUnReadWESku(){
        Query query = HibernateERPUtil.getCurrentSession().createQuery("from WESku where status=0");
        List<WESku> weSkuList=query.list();
        return  weSkuList;
    }

}
