package com.wms.vo;

/**
 * Created by Administrator on 2014/10/15.
 */
public class ReturnObj<T> extends BaseReturnObj {
    private T res;

    public T getRes() {
        return res;
    }

    public void setRes(T res) {
        this.res = res;
    }
}
