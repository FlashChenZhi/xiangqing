package com.wms.domain;

import javax.persistence.*;

/**
 * Author: Zhouyue
 * Date: 2010-6-29
 * Time: 11:43:10
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "WCS")
public class Wcs {
    private String _wcsName;

    @Column(name = "WCSNAME")
    @Id
    public String getWcsName() {
        return _wcsName;
    }

    public void setWcsName(String wcsName) {
        _wcsName = wcsName;
    }

    private String _ipAddress;

    @Column(name = "IPADDRESS")
    @Basic
    public String getIpAddress() {
        return _ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        _ipAddress = ipAddress;
    }

    private int _port;

    @Column(name = "PORT")
    @Basic
    public int getPort() {
        return _port;
    }

    public void setPort(int port) {
        _port = port;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Wcs plc = (Wcs) o;

        if (_port != plc._port)
            return false;
        if (_wcsName != null ? !_wcsName.equals(plc._wcsName) : plc._wcsName != null)
            return false;
        if (_ipAddress != null ? !_ipAddress.equals(plc._ipAddress) : plc._ipAddress != null)
            return false;
        if (_status != null ? !_status.equals(plc._status) : plc._status != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _wcsName != null ? _wcsName.hashCode() : 0;
        result = 31 * result + (_ipAddress != null ? _ipAddress.hashCode() : 0);
        result = 31 * result + _port;
        result = 31 * result + (_status != null ? _status.hashCode() : 0);
        return result;
    }
}
