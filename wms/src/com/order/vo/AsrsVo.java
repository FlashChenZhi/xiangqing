package com.order.vo;

/**
 * Created by van on 2018/1/15.
 */
public class AsrsVo {
//    fields: ['id', 'mcKey', 'barCode', 'fromLocation', 'toLocation', 'type','createDate','status','fromStation','toStation'],

    private  int id;
    private String mcKey;
    private String barCode;
    private String fromLocation;
    private String toLocation;
    private String type;
    private String createDate;
    private String status;
    private String fromStation;
    private String toStation;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }
}
