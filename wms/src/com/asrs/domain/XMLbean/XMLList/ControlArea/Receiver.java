package com.asrs.domain.XMLbean.XMLList.ControlArea;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by van on 2017/4/14.
 */
public class Receiver {

    @XStreamAlias("Division")
    private String division ;

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
