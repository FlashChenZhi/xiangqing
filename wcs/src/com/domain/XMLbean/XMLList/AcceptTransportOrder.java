package com.domain.XMLbean.XMLList;

import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.DataArea.DAList.AcceptTransportOrderDA;
import com.domain.XMLbean.XMLProcess;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by van on 2017/4/17.
 */
public class AcceptTransportOrder extends XMLProcess {

    @XStreamAlias("ControlArea")
    private ControlArea controlArea;

    @XStreamAlias("DataArea")
    private AcceptTransportOrderDA dataArea;

    public ControlArea getControlArea() {
        return controlArea;
    }

    public void setControlArea(ControlArea controlArea) {
        this.controlArea = controlArea;
    }

    public AcceptTransportOrderDA getDataArea() {
        return dataArea;
    }

    public void setDataArea(AcceptTransportOrderDA dataArea) {
        this.dataArea = dataArea;
    }

    @Override
    public void execute() {

    }
}
