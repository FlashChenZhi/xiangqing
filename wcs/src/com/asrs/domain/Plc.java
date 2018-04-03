package com.asrs.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Date;

/**
 * Author: Zhouyue
 * Date: 2010-6-29
 * Time: 11:43:10
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "PLC")
public class Plc
{
    private String _plcName;

    @Column(name = "PLCNAME")
    @Id
    public String getPlcName()
    {
        return _plcName;
    }

    public void setPlcName(String plcName)
    {
        _plcName = plcName;
    }

    private String _ipAddress;

    @Column(name = "IPADDRESS")
    @Basic
    public String getIpAddress()
    {
        return _ipAddress;
    }

    public void setIpAddress(String ipAddress)
    {
        _ipAddress = ipAddress;
    }

    private int _port;

    @Column(name = "PORT")
    @Basic
    public int getPort()
    {
        return _port;
    }

    public void setPort(int port)
    {
        _port = port;
    }

    private String _status;

    @Column(name = "STATUS")
    @Basic
    public String getStatus()
    {
        return _status;
    }

    public void setStatus(String status)
    {
        _status = status;
    }

    private Date _lastHeartBeatTime;

    @Column(name = "LASTHEARTBEATTIME")
    @Basic
    public Date getLastHeartBeatTime() {
        return _lastHeartBeatTime;
    }

    public void setLastHeartBeatTime(Date lastHeartBeatTime) {
        _lastHeartBeatTime = lastHeartBeatTime;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Plc plc = (Plc) o;

        if (_port != plc._port)
            return false;
        if (_plcName != null ? !_plcName.equals(plc._plcName) : plc._plcName != null)
            return false;
        if (_ipAddress != null ? !_ipAddress.equals(plc._ipAddress) : plc._ipAddress != null)
            return false;
        if (_status != null ? !_status.equals(plc._status) : plc._status != null)
            return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = _plcName != null ? _plcName.hashCode() : 0;
        result = 31 * result + (_ipAddress != null ? _ipAddress.hashCode() : 0);
        result = 31 * result + _port;
        result = 31 * result + (_status != null ? _status.hashCode() : 0);
        return result;
    }

    public static Plc getPlcByPlcName(String plcName)
    {
        Session session = HibernateUtil.getCurrentSession();

        Plc plc = (Plc) session.createQuery("from Plc p where p.plcName = :plcName")
                .setString("plcName",plcName).setMaxResults(1).uniqueResult();
        return plc;
    }
}
