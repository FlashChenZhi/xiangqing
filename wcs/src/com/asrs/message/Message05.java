package com.asrs.message;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

public class Message05 extends Message implements Serializable {
    private String id = "05";
    private String plcName = "";
    private String blockNo = "";

    public String getBlockNo() {
        return blockNo;
    }

    public void setBlockNo(String blockNo) {
        this.blockNo = blockNo;
    }

    public String getID() {
        return id;
    }

    public void setID(String id) {
        this.id = id;
    }

    public String getPlcName() {
        return plcName;
    }

    public void setPlcName(String plcName) {
        this.plcName = plcName;
    }

    public String McKey = "";
    public String Response = "";
    public String BlockNo = "";

    public Message05() {
    }

    public Message05(String str) throws MsgException {
        if (str.length() == 5) {
            McKey = str.substring(0, 4);
            BlockNo = str.substring(4,8);
            Response = str.substring(8, 9);
        } else {
            throw new MsgException("MsgException.Invalid_length   " + str);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.rightPad(McKey, 4, '0'));
        sb.append(StringUtils.rightPad(BlockNo,4,'0'));
        sb.append(StringUtils.rightPad(Response, 1, '0'));
        return sb.toString();
    }
}
