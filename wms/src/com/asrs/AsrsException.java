package com.asrs;

/**
 * Author: Zhouyue
 * Date: 2008-10-17
 * Time: 16:50:09
 * Copyright Daifuku Shanghai Ltd.
 */
public class AsrsException extends Exception {
    public static final String UNKNOWN_TASK = "Unknown task, Mckey:";

    public AsrsException() {
        super();
    }

    public AsrsException(String msg) {
        super(msg);
    }
}
