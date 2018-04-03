package com.wms.vo;

/**
 * Created by wangfan
 * Created on 2017/3/12.
 */
public class Msg03Vo {
    private int id;
    private String cycleOrder;
    private String mcKey;
    private String station;
    private String dock;
    private String machineNo;
    private boolean received;
    private String sendDate;
    private String type;

    private String bank;
    private String bay;
    private String level;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCycleOrder() {
        return cycleOrder;
    }

    public void setCycleOrder(String cycleOrder) {
        this.cycleOrder = cycleOrder;
    }

    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDock() {
        return dock;
    }

    public void setDock(String dock) {
        this.dock = dock;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public String getSendDate() {
        return sendDate;
    }

    public void setSendDate(String sendDate) {
        this.sendDate = sendDate;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBay() {
        return bay;
    }

    public void setBay(String bay) {
        this.bay = bay;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
