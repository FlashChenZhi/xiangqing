package com.wms.domain.erp;

import com.util.hibernate.HibernateERPUtil;
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
@Table(name = "TRUCK")
public class Truck {

    private int id;
    private String mark;//车牌号
    private int isDel;//是否删除（ 0 不删除，1 删除）
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
    @Column(name = "STATUS")
    @Basic
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static List<Truck> findUnReadTruck(){
        Query query = HibernateERPUtil.getCurrentSession().createQuery("from Truck where status=0");
        List<Truck> trucks=query.list();
        return  trucks;
    }
}
