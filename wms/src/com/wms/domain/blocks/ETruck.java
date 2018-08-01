package com.wms.domain.blocks;

import com.util.hibernate.HibernateERPUtil;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;

import javax.persistence.*;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 22:17 2018/7/22
 * @Description:车辆信息表
 * @Modified By:
 */
@Entity
@Table(name = "ETRUCK")
public class ETruck {

    private int id;
    private String mark;//车牌号
    private int isDel;//是否删除（ 0 不删除，1 删除）
    private int eId;//erpID

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "MARK")
    @Basic
    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Column(name = "IS_DEL")
    @Basic
    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    @Column(name = "EID")
    @Basic
    public int geteId() {
        return eId;
    }

    public void seteId(int eId) {
        this.eId = eId;
    }

    public static ETruck findETruckByMark(String mark){
        Query query = HibernateUtil.getCurrentSession().createQuery("from ETruck where mark=:mark");
        query.setParameter("mark", mark);
        query.setMaxResults(1);
        ETruck truck=(ETruck) query.uniqueResult();
        return  truck;
    }

    public static ETruck findETruckByEid(int eId){
        Query query = HibernateUtil.getCurrentSession().createQuery("from ETruck where eId=:eId");
        query.setParameter("eId", eId);
        query.setMaxResults(1);
        ETruck truck=(ETruck) query.uniqueResult();
        return  truck;
    }
}
