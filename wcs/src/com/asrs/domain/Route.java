package com.asrs.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;

/**
 * Author: Zhouyue
 * Date: 2010-6-29
 * Time: 11:43:20
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "ROUTE")
public class Route {
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

    private String _fromStation;

    @Column(name = "FROMSTATION")
    @Basic
    public String getFromStation() {
        return _fromStation;
    }

    public void setFromStation(String fromStation) {
        _fromStation = fromStation;
    }

    private String _toStation;

    @Column(name = "TOSTATION")
    @Basic
    public String getToStation() {
        return _toStation;
    }

    public void setToStation(String toStation) {
        _toStation = toStation;
    }

    private String _status;

    @Column(name = "STATUS")
    @Basic
    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    private String _type;

    @Column(name = "TYPE")
    @Basic
    public String getType() {
        return _type;
    }

    public void setType(String type) {
        _type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (_id != route._id) return false;
        if (_fromStation != null ? !_fromStation.equals(route._fromStation) : route._fromStation != null) return false;
        if (_status != null ? !_status.equals(route._status) : route._status != null) return false;
        if (_toStation != null ? !_toStation.equals(route._toStation) : route._toStation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_fromStation != null ? _fromStation.hashCode() : 0);
        result = 31 * result + (_toStation != null ? _toStation.hashCode() : 0);
        result = 31 * result + (_status != null ? _status.hashCode() : 0);
        return result;
    }

    public static Route getRoute(String fromSt, String toSt) {
        Session session = HibernateUtil.getCurrentSession();
        Query q = session.createQuery("from Route r where r.fromStation = :from and r.toStation = :to");
        Route r = (Route) q.setString("from", fromSt).setString("to", toSt).uniqueResult();
        return r;
    }

    public Boolean RouteOK() {
//            for (AsrsMachine mac : this.getAsrsMachines())
//            {
//                  if (mac.getStatus().equals(Message30._Status.Disconnected))
//                  {
//                        return false;
//                  }
//            }
//            return true;
        return this.getStatus().equals("0");
    }
}
