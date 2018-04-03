package com.util.common;

/**
 * Created by zhangming on 2014/10/22.
 */
public class PagerReturnObj<T> extends ReturnObj<T> {
    private long count;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
