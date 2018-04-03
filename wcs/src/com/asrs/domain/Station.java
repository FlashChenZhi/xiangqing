package com.asrs.domain;

import com.asrs.business.consts.StationMode;
import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Author: Zhouyue
 * Date: 2010-6-29
 * Time: 11:43:22
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "STATION")
public class Station
{
    public static final String __STATIONNO = "stationNo";

    public static final String __PROXYSTATION = "proxyStation";

    private String _stationNo;

    @Column(name = "STATIONNO")
    @Id
    public String getStationNo()
    {
        return _stationNo;
    }

    public void setStationNo(String stationNo)
    {
        _stationNo = stationNo;
    }

    private String _name;

    @Column(name = "NAME")
    @Basic
    public String getName()
    {
        return _name;
    }

    public void setName(String name)
    {
        _name = name;
    }

    private String _proxyStation;

    @Column(name = "PROXYSTATION")
    @Basic
    public String getProxyStation()
    {
        return _proxyStation;
    }

    public void setProxyStation(String proxyStation)
    {
        _proxyStation = proxyStation;
    }


    private BigInteger _aisleNo;

    @Column(name = "AISLENO")
    @Basic
    public BigInteger getAisleNo()
    {
        return _aisleNo;
    }

    public void setAisleNo(BigInteger aisleNo)
    {
        _aisleNo = aisleNo;
    }

    private BigInteger _buffer;

    @Column(name = "BUFFER")
    @Basic
    public BigInteger getBuffer()
    {
        return _buffer;
    }

    public void setBuffer(BigInteger buffer)
    {
        _buffer = buffer;
    }

    private String _mode;

    @Column(name = "`MODE`")
    @Basic
    public String getMode()
    {
        return _mode;
    }

    public void setMode(String mode)
    {
        _mode = mode;
    }

    private String _type;

    @Column(name = "TYPE")
    @Basic
    public String getType()
    {
        return _type;
    }

    public void setType(String type)
    {
        _type = type;
    }

    private Boolean _arrival;

    @Column(name = "ARRIVAL")
    @Basic
    public Boolean getArrival()
    {
        return _arrival;
    }

    public void setArrival(Boolean arrival)
    {
        _arrival = arrival;
    }

    private Date _modeChangeTime;

    @Column(name = "MODECHANGETIME")
    @Basic
    public Date getModeChangeTime()
    {
        return _modeChangeTime;
    }

    public void setModeChangeTime(Date modeChangeTime)
    {
        _modeChangeTime = modeChangeTime;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Station station = (Station) o;

        if (_aisleNo != null ? !_aisleNo.equals(station._aisleNo) : station._aisleNo != null)
        {
            return false;
        }
        if (_buffer != null ? !_buffer.equals(station._buffer) : station._buffer != null)
        {
            return false;
        }
        if (_mode != null ? !_mode.equals(station._mode) : station._mode != null)
        {
            return false;
        }
        if (_name != null ? !_name.equals(station._name) : station._name != null)
        {
            return false;
        }
        if (_stationNo != null ? !_stationNo.equals(station._stationNo) : station._stationNo != null)
        {
            return false;
        }
        if (_type != null ? !_type.equals(station._type) : station._type != null)
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = _stationNo != null ? _stationNo.hashCode() : 0;
        result = 31 * result + (_name != null ? _name.hashCode() : 0);
        result = 31 * result + (_aisleNo != null ? _aisleNo.hashCode() : 0);
        result = 31 * result + (_buffer != null ? _buffer.hashCode() : 0);
        result = 31 * result + (_mode != null ? _mode.hashCode() : 0);
        result = 31 * result + (_type != null ? _type.hashCode() : 0);
        return result;
    }

    private String _oldMode;

    @Column(name = "OLDMODE")
    @Basic
    public String getOldMode()
    {
        return _oldMode;
    }

    public void setOldMode(String mode)
    {
        _oldMode = mode;
    }

    public void modeBack()
    {
        this._mode = _oldMode;
    }

    public static String getProxy(String stationNo)
    {
        Session session = HibernateUtil.getCurrentSession();
        Station st = (Station) session.get(Station.class, stationNo);
        if (st != null)
        {
            return st.getProxyStation();
        }
        else
        {
            return stationNo;
        }
    }

    public static List<Station> getStationsByProxySt(String proxySt)
    {
        Session session = HibernateUtil.getCurrentSession();
        Query q = session.createQuery("from Station s where s.proxyStation = :proxy or s.stationNo = :proxy");
        q.setString("proxy", proxySt);
        return q.list();
    }

    public static List<Station> getAllModeStations()
    {
        Session session = HibernateUtil.getCurrentSession();
        Query q = session.createQuery("from Station s where s.type=:type").setString("type", "3");
        return q.list();
    }

    public static void setArrival(String stationNo, Boolean isArrival)
    {
        Station station = (Station) HibernateUtil.getCurrentSession().get(Station.class, stationNo);
        if (station != null)
        {
            station.setArrival(isArrival);
        }
    }

    public static Boolean getArrival(String stationNo)
    {
        Station station = (Station) HibernateUtil.getCurrentSession().get(Station.class, stationNo);
        if (station != null)
        {
            return station.getArrival();
        }
        return false;
    }

    public static void updateMode(String stationNo, String completionMode)
    {
        Session session = HibernateUtil.getCurrentSession();
        Station station = (Station) session.get(Station.class, stationNo);
        if (station != null)
        {
            station.setMode(completionMode);
        }
    }

    public static String getMode(String stationNo)
    {
        if (StringUtils.isBlank(stationNo))
        {
            return StationMode.UNKNOWN;
        }

        Session session = HibernateUtil.getCurrentSession();
        Station station = (Station) session.get(Station.class, stationNo);
        if (station != null)
        {
            return station.getMode();
        }
        else
        {
            return StationMode.UNKNOWN;
        }
    }

    public static Station getStation(String stationNo)
    {
        if (StringUtils.isBlank(stationNo))
        {
            return null;
        }

        Session session = HibernateUtil.getCurrentSession();
        Station station = (Station) session.get(Station.class, stationNo);
        return station;
    }
}
