package com.asrs.domain;


import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Author: Zhouyue
 * Date: 2010-11-12
 * Time: 12:19:31
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "LOCATION")
public class Location {
    public static final String __LOCATIONNO = "locationNo";

    public static final String __AISLE = "aisle";


    public static final String __ID = "id";


    private static int bigCurrAisle = 0;

    private static int smallCurrAisle = 0;

    private int _id;

    public static final String MISS = "0";
    public static final String LEFT = "1";
    public static final String RIGHT = "2";

    private Double width;
    private Double height;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    private String _locationNo;

    @Basic
    @Column(name = "LOCATIONNO")
    public String getLocationNo() {
        return _locationNo;
    }

    public void setLocationNo(String locationNo) {
        _locationNo = locationNo;
    }
    private String _wmslocationNo;

    @Basic
    @Column(name = "WMSLOCATIONNO")
    public String getWmsLocationNo() {
        return _wmslocationNo;
    }

    public void setWmsLocationNo(String wmslocationNo) {
        _wmslocationNo = wmslocationNo;
    }

    private int _aisle;

    @Basic
    @Column(name = "AISLE")
    //. 通道，走道；侧廊
    public int getAisle() {
        return _aisle;
    }

    public void setAisle(int aisle) {
        _aisle = aisle;
    }

    private int _bank;

    @Basic
    @Column(name = "BANK")
    public int getBank() {
        return _bank;
    }

    public void setBank(int bank) {
        _bank = bank;
    }

    private boolean _empty;

    @Basic
    @Column(name = "EMPTY")
    public boolean getEmpty() {
        return _empty;
    }

    public void setEmpty(boolean empty) {
        _empty = empty;
    }
    private int _bay;

    @Basic
    @Column(name = "BAY")
    public int getBay() {
        return _bay;
    }

    public void setBay(int bay) {
        _bay = bay;
    }

    private int _level;

    @Basic
    @Column(name = "LEV")
    public int getLevel() {
        return _level;
    }

    public void setLevel(int level) {
        _level = level;
    }

    private int _version = 0;

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return _version;
    }

    public void setVersion(int version) {
        _version = version;
    }

    private String position;

    @Basic
    @Column(name = "POSITION")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    private String outPosition;

    @Basic
    @Column(name = "OUTPOSITION")
    public String getOutPosition() {
        return outPosition;
    }

    public void setOutPosition(String outPosition) {
        this.outPosition = outPosition;
    }

    private int _seq;

    @Basic
    @Column(name = "SEQ")
    public int getSeq() {
        return _seq;
    }

    public void setSeq(int seq) {
        _seq = seq;
    }

    private boolean _reserved;

    @Basic
    @Column(name = "RESERVED")
    //保留的
    public boolean getReserved() {
        return _reserved;
    }

    public void setReserved(boolean reserved) {
        _reserved = reserved;
    }


    private String actualArea;

    @Basic
    @Column(name = "AREA")
    public String getActualArea() {
        return actualArea;
    }

    public void setActualArea(String actureArea) {
        this.actualArea = actureArea;
    }

    private Boolean fullFlag;

    @Basic
    @Column(name = "FULLFLAG")
    public Boolean getFullFlag() {
        return fullFlag;
    }

    public void setFullFlag(Boolean fullFlag) {
        this.fullFlag = fullFlag;
    }

    @Basic
    @Column(name = "WIDTH")
    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    @Basic
    @Column(name = "HEIGHT")
    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    private Collection<ScarChargeLocation> _scarChargeLocation = new ArrayList<ScarChargeLocation>();

    @OneToMany(mappedBy = "chargeLocation")
    public Collection<ScarChargeLocation> get_scarChargeLocation() {
        return _scarChargeLocation;
    }

    public void set_scarChargeLocation(Collection<ScarChargeLocation> _scarChargeLocation) {
        this._scarChargeLocation = _scarChargeLocation;
    }

    public static Location getByLocationNo(String locationNo) {
        Session session = HibernateUtil.getCurrentSession();

        Query q = session.createQuery(" from Location l where l.locationNo = :locationNo")
                .setString("locationNo", locationNo);
        return (Location) q.uniqueResult();
    }

    public static Location getById(int id) {
        Session session = HibernateUtil.getCurrentSession();

        return (Location) session.get(Location.class, id);
    }

    /**
     * @param i  　　bank
     * @param i1 bay
     * @param i2 level
     * @return
     */
    public static Location getByBankBayLevel(int i, int i1, int i2,String position) {

        Query q = HibernateUtil.getCurrentSession().createQuery("from Location  l where l.bank=:b and l.bay =:ba and l.level=:lv and l.position=:po ")
                .setParameter("b", i).setParameter("ba", i1).setParameter("lv", i2).setParameter("po",position).setMaxResults(1);

        Location location = (Location) q.uniqueResult();
        if(location == null){
            q = HibernateUtil.getCurrentSession().createQuery("from Location  l where l.bank=:b and l.bay =:ba and l.level=:lv and l.outPosition=:po ")
                    .setParameter("b", i).setParameter("ba", i1).setParameter("lv", i2).setParameter("po",position).setMaxResults(1);

            location = (Location) q.uniqueResult();
        }
        return location;
    }
}
