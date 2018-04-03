package com.asrs.domain.XMLbean.XMLList.ControlArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.util.common.DateTimeFormatter;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-6-4
 * Time: 下午1:28
 */
public class ControlArea {
    @XStreamAlias("Sender")
    private Sender sender;

    @XStreamAlias("Receiver")
    private Receiver receiver;

    @XStreamAlias("CreationDateTime")
    private String creationDateTime;

    @XStreamAlias("RefId")
    private RefId RefId;

    @XStreamOmitField
    private int id;

    public ControlArea() {
        creationDateTime = new DateTimeFormatter("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RefId getRefId() {
        return RefId;
    }

    public void setRefId(RefId refId) {
        RefId = refId;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public String getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}
