package com.asrs.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;

/**
 * @Author: ed_chen
 * @Date: Create in 22:35 2018/6/22
 * @Description:
 * @Modified By:
 */
@Entity
@Table(name = "SCARCHARGELOCATION")
public class ScarChargeLocation {

    private int _id;

    private Location chargeLocation;

    private String scarBlockNo;

    private boolean status;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    @ManyToOne
    @JoinColumn(name = "CHARGELOCATION", referencedColumnName = "ID")
    public Location getChargeLocation() {
        return chargeLocation;
    }

    public void setChargeLocation(Location chargeLocation) {
        this.chargeLocation = chargeLocation;
    }
    @Basic
    @Column(name = "SCARBLOCKNO")
    public String getScarBlockNo() {
        return scarBlockNo;
    }


    public void setScarBlockNo(String scarBlockNo) {
        this.scarBlockNo = scarBlockNo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    @Transient
    public static List<ScarChargeLocation> getBySCarBlockNo(String scarBlockNo) {
        Session session = HibernateUtil.getCurrentSession();

        Query query = session.createQuery("from ScarChargeLocation s where scarBlockNo=:scarBlockNo and status=true ");
        query.setParameter("scarBlockNo", scarBlockNo);
        return query.list();
    }
}
