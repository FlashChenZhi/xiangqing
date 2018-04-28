package com.util.common;

/**
 * Created by Administrator on 2014/10/15.
 */
public class BaseReturnObj {
    public BaseReturnObj(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public BaseReturnObj() {
    }

    private boolean success;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
