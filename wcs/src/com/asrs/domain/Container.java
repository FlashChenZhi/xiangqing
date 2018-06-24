package com.asrs.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Author: Zhouyue
 * Date: 2010-11-12
 * Time: 12:19:28
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "CONTAINER")
public class Container {
    public static final String __BARCODE = "barcode";

    public static final String __LOCATION = "location";

    public static final String __RESERVED = "reserved";

    public static final String __ID = "id";

    public static final String __PUTAWAYDATE = "putawayDate";


    private int _id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    private Date _createDate;

    @Column(name = "CREATEDATE")
    @Basic
    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        _createDate = createDate;
    }

    private String _barcode;

    @Column(name = "BARCODE")
    @Basic
    public String getBarcode() {
        return _barcode;
    }

    public void setBarcode(String barcode) {
        _barcode = barcode;
    }

    private boolean _reserved;

    @Column(name = "RESERVED")
    @Basic

    public boolean isReserved() {
        return _reserved;
    }

    public void setReserved(boolean reserved) {
        _reserved = reserved;
    }

    private String _createUser;

    @Column(name = "CREATEUSER")
    @Basic
    public String getCreateUser() {
        return _createUser;
    }

    public void setCreateUser(String createUser) {
        _createUser = createUser;
    }

    private Date _putawayDate;

    @Column(name = "PUTAWAYDATE")
    @Basic
    public Date getPutawayDate() {
        return _putawayDate;
    }

    public void setPutawayDate(Date putawayDate) {
        _putawayDate = putawayDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Container container = (Container) o;

        if (_id != container._id) return false;
        if (_reserved != container._reserved) return false;
        if (_barcode != null ? !_barcode.equals(container._barcode) : container._barcode != null) return false;
        if (_createDate != null ? !_createDate.equals(container._createDate) : container._createDate != null)
            return false;
        if (_inventories != null ? !_inventories.equals(container._inventories) : container._inventories != null)
            return false;
        if (_location != null ? !_location.equals(container._location) : container._location != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_createDate != null ? _createDate.hashCode() : 0);
        result = 31 * result + (_barcode != null ? _barcode.hashCode() : 0);
        result = 31 * result + (_reserved ? 1 : 0);
        result = 31 * result + (_location != null ? _location.hashCode() : 0);
        result = 31 * result + (_inventories != null ? _inventories.hashCode() : 0);
        return result;
    }

    private Location _location;

    @ManyToOne
    public
    @JoinColumn(name = "LOCATIONID", referencedColumnName = "ID")
    Location getLocation() {
        return _location;
    }

    public void setLocation(Location location) {
        _location = location;
    }

    private Collection<Inventory> _inventories = new ArrayList<Inventory>();

    @OneToMany(mappedBy = "container")
    @Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE,
            org.hibernate.annotations.CascadeType.DELETE})
    public Collection<Inventory> getInventories() {
        return _inventories;
    }

    private void setInventories(Collection<Inventory> inventories) {
        _inventories = inventories;
    }

    public void addInventory(Inventory inventory) {
        _inventories.add(inventory);
        inventory.setContainer(this);
    }

    public static Container getByBarcode(String palletNo) {
        Query q = HibernateUtil.getCurrentSession().createQuery("from Container c where c.barcode = :barcode")
                .setString("barcode", palletNo);


        return (Container) q.uniqueResult();
    }

    public static Container getById(int id) {
        Session session = HibernateUtil.getCurrentSession();

        return (Container) session.get(Container.class, id);
    }
}
