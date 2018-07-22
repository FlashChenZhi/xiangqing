package com.asrs.domain;

import com.thread.blocks.Lift;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
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

    private boolean receved;

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

    @Basic
    @Column(name = "STATUS")
    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Basic
    @Column(name = "RECEVED")
    public boolean isReceved() {
        return receved;
    }

    public void setReceved(boolean receved) {
        this.receved = receved;
    }

    @Transient
    public static List<ScarChargeLocation> getAbleChargeLocationBySCarBlockNo(String scarBlockNo) {
        Session session = HibernateUtil.getCurrentSession();

        Query query = session.createQuery("from ScarChargeLocation s where s.scarBlockNo=:scarBlockNo " +
                "and s.status=true and s.receved=false ");
        query.setParameter("scarBlockNo", scarBlockNo);
        List<ScarChargeLocation> scarChargeLocations =query.list();
        List<ScarChargeLocation> scarChargeLocations2=new ArrayList<>();
        for(int i=0;i<scarChargeLocations.size();i++){
            ScarChargeLocation scarChargeLocation=scarChargeLocations.get(i);
            boolean flag = isScarChargeLocationReceved(scarChargeLocation.getChargeLocation());
            if(flag){
                scarChargeLocations2.add(scarChargeLocation);
            }
        }
        return scarChargeLocations2;
    }

    @Transient
    public static ScarChargeLocation getReservedChargeLocationBySCarBlockNo(String scarBlockNo) {
        Session session = HibernateUtil.getCurrentSession();

        Query query = session.createQuery("from ScarChargeLocation s where s.scarBlockNo=:scarBlockNo " +
                "and s.status=true and s.receved=true ");
        query.setParameter("scarBlockNo", scarBlockNo);
        return (ScarChargeLocation)query.uniqueResult();
    }

    @Transient
    public static boolean isScarChargeLocationReceved (Location location) {
        Session session = HibernateUtil.getCurrentSession();

        Query query = session.createQuery("select count(*) as count from ScarChargeLocation s where s.chargeLocation.id=:id " +
                "and s.status=true and s.receved=true ");
        query.setParameter("id", location.getId());
        long count = (long)query.uniqueResult();
        boolean flag = count==0?true:false;
        return flag;
    }
}
