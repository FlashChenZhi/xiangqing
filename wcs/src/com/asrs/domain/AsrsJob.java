package com.asrs.domain;

import org.hibernate.*;
import com.util.hibernate.HibernateUtil;
import org.hibernate.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Author: Zhouyue
 * Date: 2010-6-29
 * Time: 11:43:14
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "ASRSJOB")
public class AsrsJob {
    public static final String __FROMSTATION = "fromStation";

    public static final String __TOSTATION = "toStation";

    public static final String __TYPE = "type";

    public static final String __STATUS = "status";

    public static final String __STATUSDETAIL = "statusDetail";
    public static final String __ID = "id";
    public static final String _MCKEY = "mcKey";
    public static final String _WMSMCKEY = "wmsMckey";
    public static final String _GENERATETIME = "generateTime";
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

    private String _mcKey;

    @Column(name = "MCKEY")
    @Basic
    public String getMcKey() {
        return _mcKey;
    }

    public void setMcKey(String mcKey) {
        _mcKey = mcKey;
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

    private String _fromLocation;

    @Column(name = "FROMLOCATION")
    @Basic
    public String getFromLocation() {
        return _fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        _fromLocation = fromLocation;
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

    private String _toLocation;

    @Column(name = "TOLOCATION")
    @Basic
    public String getToLocation() {
        return _toLocation;
    }

    public void setToLocation(String toLocation) {
        _toLocation = toLocation;
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

    private String _status;

    @Column(name = "STATUS")
    @Basic
    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        _status = status;
    }

    private String _statusDetail;

    @Column(name = "STATUSDETAIL")
    @Basic
    public String getStatusDetail() {
        return _statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        _statusDetail = statusDetail;
    }

    private int _priority;

    @Column(name = "PRIORITY")
    @Basic
    public int getPriority() {
        return _priority;
    }

    public void setPriority(int priority) {
        _priority = priority;
    }

    private Date _generateTime;

    @Column(name = "GENERATETIME")
    @Basic
    public Date getGenerateTime() {
        return _generateTime;
    }

    public void setGenerateTime(Date generateTime) {
        _generateTime = generateTime;
    }

    private Date _startTime;

    @Column(name = "STARTTIME")
    @Basic
    public Date getStartTime() {
        return _startTime;
    }

    public void setStartTime(Date startTime) {
        _startTime = startTime;
    }

    private Date _doneTime;

    @Column(name = "DONETIME")
    @Basic
    public Date getDoneTime() {
        return _doneTime;
    }

    public void setDoneTime(Date doneTime) {
        _doneTime = doneTime;
    }

    private Boolean _indicating;

    @Column(name = "INDICATING")
    @Basic
    public Boolean getIndicating() {
        return _indicating;
    }

    public void setIndicating(Boolean indicating) {
        _indicating = indicating;
    }

    private String _errorMsg;

    @Column(name = "ERRORMSG")
    @Basic
    public String getErrorMsg() {
        return _errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        _errorMsg = errorMsg;
    }

    @Basic
    @Column(name = "WMSMCKEY")
    private String wmsMckey;

    public String getWmsMckey() {
        return wmsMckey;
    }

    public void setWmsMckey(String wmsMckey) {
        this.wmsMckey = wmsMckey;
    }

    private String wareHouse;

    @Basic
    @Column(name = "WARE_HOUSE")
    public String getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }

    private boolean sendReport;

    @Basic
    @Column(name = "SEND_REPORT")
    public boolean isSendReport() {
        return sendReport;
    }

    public void setSendReport(boolean sendReport) {
        this.sendReport = sendReport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AsrsJob asrsJob = (AsrsJob) o;

        if (_id != asrsJob._id) return false;
        if (_priority != asrsJob._priority) return false;
        if (_barcode != null ? !_barcode.equals(asrsJob._barcode) : asrsJob._barcode != null) return false;
        if (_doneTime != null ? !_doneTime.equals(asrsJob._doneTime) : asrsJob._doneTime != null) return false;
        if (_errorMsg != null ? !_errorMsg.equals(asrsJob._errorMsg) : asrsJob._errorMsg != null) return false;
        if (_fromLocation != null ? !_fromLocation.equals(asrsJob._fromLocation) : asrsJob._fromLocation != null)
            return false;
        if (_fromStation != null ? !_fromStation.equals(asrsJob._fromStation) : asrsJob._fromStation != null)
            return false;
        if (_generateTime != null ? !_generateTime.equals(asrsJob._generateTime) : asrsJob._generateTime != null)
            return false;
        if (_indicating != null ? !_indicating.equals(asrsJob._indicating) : asrsJob._indicating != null) return false;
        if (_mcKey != null ? !_mcKey.equals(asrsJob._mcKey) : asrsJob._mcKey != null) return false;
        if (_startTime != null ? !_startTime.equals(asrsJob._startTime) : asrsJob._startTime != null) return false;
        if (_status != null ? !_status.equals(asrsJob._status) : asrsJob._status != null) return false;
        if (_statusDetail != null ? !_statusDetail.equals(asrsJob._statusDetail) : asrsJob._statusDetail != null)
            return false;
        if (_toLocation != null ? !_toLocation.equals(asrsJob._toLocation) : asrsJob._toLocation != null) return false;
        if (_toStation != null ? !_toStation.equals(asrsJob._toStation) : asrsJob._toStation != null) return false;
        if (_type != null ? !_type.equals(asrsJob._type) : asrsJob._type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_mcKey != null ? _mcKey.hashCode() : 0);
        result = 31 * result + (_fromStation != null ? _fromStation.hashCode() : 0);
        result = 31 * result + (_toStation != null ? _toStation.hashCode() : 0);
        result = 31 * result + (_fromLocation != null ? _fromLocation.hashCode() : 0);
        result = 31 * result + (_barcode != null ? _barcode.hashCode() : 0);
        result = 31 * result + (_toLocation != null ? _toLocation.hashCode() : 0);
        result = 31 * result + (_type != null ? _type.hashCode() : 0);
        result = 31 * result + (_status != null ? _status.hashCode() : 0);
        result = 31 * result + (_statusDetail != null ? _statusDetail.hashCode() : 0);
        result = 31 * result + _priority;
        result = 31 * result + (_generateTime != null ? _generateTime.hashCode() : 0);
        result = 31 * result + (_startTime != null ? _startTime.hashCode() : 0);
        result = 31 * result + (_doneTime != null ? _doneTime.hashCode() : 0);
        result = 31 * result + (_indicating != null ? _indicating.hashCode() : 0);
        result = 31 * result + (_errorMsg != null ? _errorMsg.hashCode() : 0);
        return result;
    }

    public static AsrsJob getById(int id) {
        Session session = HibernateUtil.getCurrentSession();
        return (AsrsJob) session.get(AsrsJob.class, id);
    }

    public static AsrsJob getAsrsJobByMcKey(String mckey) {
        Session session = HibernateUtil.getCurrentSession();
        Query q = session.createQuery("from AsrsJob aj where aj.mcKey=:mckey").setString("mckey", mckey);
        return (AsrsJob) q.uniqueResult();
    }

    public void delete(){
        Session session = HibernateUtil.getCurrentSession();
        session.delete(this);
        Query query = session.createQuery("from WcsMessage wm where wm.mcKey = :mcKey")
                .setString("mcKey",this._mcKey);

        List<WcsMessage> wms = query.list();
        for(WcsMessage wm : wms){
            session.delete(wm);
        }
    }
}

