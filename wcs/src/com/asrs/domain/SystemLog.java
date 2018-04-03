package com.asrs.domain;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by van on 2018/1/6.
 */
@Entity
@Table(name = "SYSTEM_LOG")
public class SystemLog {
    public static final String __MESSAGETYPE = "type";
    public static final String __CREATEDATE = "createDate";
    private int id;
    private String message;
    private String type;
    private Date createDate;

    public static final String INFO = "1";
    public static final String ERROR = "2";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false, length = 8)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "MESSAGE")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Basic
    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public static void error(String message) {
        SystemLog log = new SystemLog();
        log.setType(ERROR);
        log.setCreateDate(new Date());
        log.setMessage(message);
        HibernateUtil.getCurrentSession().save(log);
    }

    public static void info(String message) {
        SystemLog log = new SystemLog();
        log.setType(INFO);
        log.setCreateDate(new Date());
        log.setMessage(message);
        HibernateUtil.getCurrentSession().save(log);
    }


}
