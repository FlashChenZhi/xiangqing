package com.util.common;

/**
 * Created by Administrator on 2016/11/21.
 */
public enum LoggerType {
    XMLMessageInfo("XMLMessageInfo"),
    WMS("WMS_INFO"),
    MessageLog("MessageLog");

    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    LoggerType(String code) {
        setCode(code);
    }
}
