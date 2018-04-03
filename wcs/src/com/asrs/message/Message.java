package com.asrs.message;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-19
 * Time: 8:51:38
 * Copyright Dsl.Worgsoft.
 */
public abstract class Message {
    public String IdClassification = "0";

    public abstract void setID(String Id);

    public abstract String getID();

    public abstract String getPlcName();

    public abstract void setPlcName(String plcName);

    public abstract String toString();
}
