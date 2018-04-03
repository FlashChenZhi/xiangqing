package com.util.common;

/**
 * Created by Administrator on 2014/10/9.
 */
public class LogMessage {
    private String code;

    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private LogMessage(String code, String name) {
        this.code = code;
        this.name = name;
    }



    /**
     * 共通(01)
     */
    //Code 0101与Oracle数据库断开连接
    public final static LogMessage DB_DISCONNECTED = new LogMessage("401001", "与Oracle数据库断开连接");
    public final static LogMessage UNEXPECTED_ERROR = new LogMessage("401002", "系统错误");
    public final static LogMessage SERVICE_EXCEPTION = new LogMessage("401003", "业务异常");
    public final static LogMessage VERSION_EXCEPTION = new LogMessage("401004", "乐观锁异常");


}

