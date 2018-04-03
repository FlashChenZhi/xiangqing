package com.asrs.domain;

import com.util.hibernate.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Author: Zhouyue
 * Date: 2010-6-29
 * Time: 11:43:12
 * Copyright Daifuku Shanghai Ltd.
 */
@Entity
@Table(name = "XMLCOMMAND")
public class XMLCommand {
    private int _id;

    public static final String ONLINE = "ONLINE";
    public static final String OFFLINE = "OFFLINE";
//    public static final String MODE_CHANGE = "MODE_CHANGE";
//    public static final String STACKING_SET = "STACKING_SET";
//    public static final String FORCE_STACK_DONE = "FORCE_STACK_DONE";
//    public static final String JOB_CANCEL = "JOB_CANCEL";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    private String _command;

    @Column(name = "COMMAND")
    @Basic
    public String getCommand() {
        return _command;
    }

    public void setCommand(String command) {
        _command = command;
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

    private String _param1;

    @Column(name = "PARAM1")
    @Basic
    public String getParam1() {
        return _param1;
    }

    public void setParam1(String param1) {
        _param1 = param1;
    }

    private String _param2;

    @Column(name = "PARAM2")
    @Basic
    public String getParam2() {
        return _param2;
    }

    public void setParam2(String param2) {
        _param2 = param2;
    }

    private String _param3;

    @Column(name = "PARAM3")
    @Basic
    public String getParam3() {
        return _param3;
    }

    public void setParam3(String param3) {
        _param3 = param3;
    }

    private Date _writeTime;

    @Column(name = "WRITETIME")
    @Basic
    public Date getWriteTime() {
        return _writeTime;
    }

    public void setWriteTime(Date writeTime) {
        _writeTime = writeTime;
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

    private boolean _procFlag;

    @Column(name = "PROCFLAG")
    @Basic
    public boolean isProcFlag() {
        return _procFlag;
    }

    public void setProcFlag(boolean procFlag) {
        _procFlag = procFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        XMLCommand that = (XMLCommand) o;

        if (_id != that._id) return false;
        if (_procFlag != that._procFlag) return false;
        if (_command != null ? !_command.equals(that._command) : that._command != null) return false;
        if (_doneTime != null ? !_doneTime.equals(that._doneTime) : that._doneTime != null) return false;
        if (_param1 != null ? !_param1.equals(that._param1) : that._param1 != null) return false;
        if (_status != null ? !_status.equals(that._status) : that._status != null) return false;
        if (_writeTime != null ? !_writeTime.equals(that._writeTime) : that._writeTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _id;
        result = 31 * result + (_command != null ? _command.hashCode() : 0);
        result = 31 * result + (_status != null ? _status.hashCode() : 0);
        result = 31 * result + (_param1 != null ? _param1.hashCode() : 0);
        result = 31 * result + (_writeTime != null ? _writeTime.hashCode() : 0);
        result = 31 * result + (_doneTime != null ? _doneTime.hashCode() : 0);
        result = 31 * result + (_procFlag ? 1 : 0);
        return result;
    }

    public static List<XMLCommand> getXmlCommands() {
        Session session = HibernateUtil.getCurrentSession();
        return session.createCriteria(XMLCommand.class).list();
    }
}
