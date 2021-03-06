package com.asrs.domain.XMLbean;

import com.asrs.communication.XmlProxy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-7-9
 * Time: 下午1:38
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public abstract class XMLProcess {
    XmlProxy xmlProxy;
    Date arriveTime;


    public abstract void execute();

    public void setProxy(XmlProxy xmlProxy) {
        this.xmlProxy = xmlProxy;
    }

    @Column(name = "arriveTime")
    public Date getDate() {
        return arriveTime;
    }

    public void setDate(Date date) {
        this.arriveTime = date;
    }

}
