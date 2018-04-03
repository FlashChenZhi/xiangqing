package com.inventory.vo;

/**
 * Created by van on 2018/3/11.
 */
public class TransferVo {
    private int id;
    private String position;
    private String area;
    private Integer bay;
    private Integer level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getBay() {
        return bay;
    }

    public void setBay(Integer bay) {
        this.bay = bay;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
