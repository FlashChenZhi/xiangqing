package com.web.vo;

/**
 * Created by wangfan
 * Created on 2017/3/11.
 */
public class BlockVo {
    private String blockNo;
    private String mcKey;
    private String reservMcKey;
    private boolean waitResponse;
    private String status;
    private String sCarNo;
    private String mCarNo;
    private Integer power;
    private String error;
    private int level;
    private int bay;
    private Integer bank;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBay() {
        return bay;
    }

    public void setBay(int bay) {
        this.bay = bay;
    }

    public Integer getBank() {
        return bank;
    }

    public void setBank(Integer bank) {
        this.bank = bank;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getMcKey() {
        return mcKey;
    }

    public void setMcKey(String mcKey) {
        this.mcKey = mcKey;
    }

    public String getReservMcKey() {
        return reservMcKey;
    }

    public void setReservMcKey(String reservMcKey) {
        this.reservMcKey = reservMcKey;
    }

    public boolean isWaitResponse() {
        return waitResponse;
    }

    public void setWaitResponse(boolean waitResponse) {
        this.waitResponse = waitResponse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getsCarNo() {
        return sCarNo;
    }

    public void setsCarNo(String sCarNo) {
        this.sCarNo = sCarNo;
    }

    public String getmCarNo() {
        return mCarNo;
    }

    public void setmCarNo(String mCarNo) {
        this.mCarNo = mCarNo;
    }
}
