package com.util.common;

/**
 * Created by Administrator on 2016/10/8.
 */
public class HttpMessage {

    private boolean success;
    private Object msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }
}
