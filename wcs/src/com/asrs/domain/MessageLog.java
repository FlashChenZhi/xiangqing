package com.asrs.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by van on 2017/5/10.
 */
@Entity
@Table(name = "MESSAGELOG")
public class MessageLog {
    public static final String __MESSAGETYPE = "type";
    public static final String __CREATEDATE = "createDate";
    private int id;
    private String type;
    private String msg;
    private Date createDate;

    public static final String TYPE_SEND = "S";
    public static final String TYPE_RECV = "R";

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
    @Column(name = "MSGTYPE")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "MSG")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Basic
    @Column(name = "CREATEDATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
