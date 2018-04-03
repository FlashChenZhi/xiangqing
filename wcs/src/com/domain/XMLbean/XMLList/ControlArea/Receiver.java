package com.domain.XMLbean.XMLList.ControlArea;

import com.domain.consts.xmlbean.XMLConstant;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by van on 2017/4/14.
 */
public class Receiver {

    @XStreamAlias("Division")
    private String division = XMLConstant.COM_DIVISION;

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
