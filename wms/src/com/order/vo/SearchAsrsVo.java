package com.order.vo;

/**
 * Created by van on 2018/1/15.
 */
public class SearchAsrsVo {
    private String type;
    private String fromLocation;
    private String fromStation;
    private String barcode;
    private String mckey;
    private String toLocation;
    private String toStation;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getMckey() {
        return mckey;
    }

    public void setMckey(String mckey) {
        this.mckey = mckey;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }
}
