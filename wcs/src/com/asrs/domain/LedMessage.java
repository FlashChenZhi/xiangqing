package com.asrs.domain;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "LEDMESSAGE")
public class LedMessage {
    private String ledNo;
    @Id
    @Column(name = "LEDNO")

    public String getLedNo() {
        return ledNo;
    }

    public void setLedNo(String ledNo) {
        this.ledNo = ledNo;
    }

    private String message1;

    @Column(name = "MESSAGE1")
    @Basic
    public String getMessage1() {
        return message1;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    private String message2;

    @Column(name = "MESSAGE2")
    @Basic
    public String getMessage2() {
        return message2;
    }

    public void setMessage2(String message2) {
        this.message2 = message2;
    }

    private String message3;

    @Column(name = "MESSAGE3")
    @Basic
    public String getMessage3() {
        return message3;
    }

    public void setMessage3(String message3) {
        this.message3 = message3;
    }

    private String message4;

    @Column(name = "MESSAGE4")
    @Basic
    public String getMessage4() {
        return message4;
    }

    public void setMessage4(String message4) {
        this.message4 = message4;
    }

    private boolean processed;

    @Column(name = "PROCESSED")
    @Basic
    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    private String ipAddress;

    @Column(name = "IPADDRESS")
    @Basic
    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    private String mcKey;

    @Column(name = "MCKEY")
    @Basic
    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String McKey) {
        this.mcKey = McKey;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LedMessage that = (LedMessage) o;
        return processed == that.processed &&
                Objects.equals(ledNo, that.ledNo) &&
                Objects.equals(message1, that.message1) &&
                Objects.equals(message2, that.message2) &&
                Objects.equals(message3, that.message3) &&
                Objects.equals(message4, that.message4) &&
                Objects.equals(ipAddress, that.ipAddress);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ledNo, message1, message2, message3, message4, processed, ipAddress);
    }

    public static LedMessage getByLedNo(String ledNo){
        return (LedMessage) HibernateUtil.getCurrentSession().get(LedMessage.class,ledNo);
    }

    public static void show(String ledNo,String message1,String message2,String message3,String message4){
        LedMessage ledMessage = getByLedNo(ledNo);
        ledMessage.setMessage1(message1);
        ledMessage.setMessage2(message2);
        ledMessage.setMessage3(message3);
        ledMessage.setMessage4(message4);
        ledMessage.setProcessed(true);
    }

    public static void clear(String ledNo){
        LedMessage ledMessage = getByLedNo(ledNo);
        ledMessage.setMessage1("");
        ledMessage.setMessage2("");
        ledMessage.setMessage3("");
        ledMessage.setMessage4("");
        ledMessage.setProcessed(true);
    }

    public void show(String message1,String message2,String message3,String message4){
        this.setMessage1(message1);
        this.setMessage2(message2);
        this.setMessage3(message3);
        this.setMessage4(message4);
        this.setProcessed(true);
    }

    public void clear(){
        this.setMessage1("");
        this.setMessage2("");
        this.setMessage3("");
        this.setMessage4("");
        this.setProcessed(true);
    }
}
