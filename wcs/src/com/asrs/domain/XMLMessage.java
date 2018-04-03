package com.asrs.domain;

import javax.persistence.*;

/**
 * Created by van on 2018/1/9.
 */
@Entity
@Table(name = "XMLMESSAGE")
public class XMLMessage {
    private int id;
    private String messageInfo;
    private String status;
    private String recv;
    private int version;

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
    @Column(name = "MESSAGE_INFO")
    public String getMessageInfo() {
        return messageInfo;
    }

    public void setMessageInfo(String messageInfo) {
        this.messageInfo = messageInfo;
    }

    @Basic
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Version
    @Column(name = "VERSION")
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Basic
    @Column(name = "RECV")
    public String getRecv() {
        return recv;
    }

    public void setRecv(String recv) {
        this.recv = recv;
    }
}
