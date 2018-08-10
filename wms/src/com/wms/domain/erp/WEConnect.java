package com.wms.domain.erp;

import com.util.hibernate.HibernateUtil;
import com.wms.domain.Sku;
import org.hibernate.Query;

import javax.persistence.*;

/**
 * @Author: ed_chen
 * @Date: Create in 11:12 2018/8/10
 * @Description:
 * @Modified By:
 */
@Entity
@Table(name = "ERPCONNECT")
public class WEConnect {
    private int id;
    private boolean isConnect;

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
    @Column(name = "ISCONNECT")
    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public static WEConnect getById(int id) {

        Query query = HibernateUtil.getCurrentSession().createQuery("from WEConnect where id =:id");
        query.setParameter("id",id);
        query.setMaxResults(1);
        return (WEConnect) query.uniqueResult();
    }
}
