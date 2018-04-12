package com.wms.domain.blocks;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/10/12.
 * 固定提升机
 */
@Entity
@Table(name = "Block")
@DiscriminatorValue(value = "2")
public class Lift extends Block {
    private int level;
    private String onCar;
    private String stationBlock;
    private String dock;

    @Basic
    @Column(name = "LEV")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Basic
    @Column(name = "ONCAR")
    public String getOnCar() {
        return onCar;
    }

    public void setOnCar(String onCar) {
        this.onCar = onCar;
    }

    @Basic
    @Column(name = "STATION_BLOCK")
    public String getStationBlock() {
        return stationBlock;
    }

    public void setStationBlock(String stationBlock) {
        this.stationBlock = stationBlock;
    }

    @Basic
    @Column(name = "DOCK")
    public String getDock() {
        return dock;
    }

    public void setDock(String dock) {
        this.dock = dock;
    }

    @Transient
    public boolean isOkToGo(String mcKey, int level) {
        if (!this.waitingResponse && mcKey.equals(getReservedMcKey()) && this.level == level) {
            return true;
        } else {
            return false;
        }
    }
}
