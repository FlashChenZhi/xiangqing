package com.asrs.domain.XMLbean.XMLList.DataArea.DAList;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by van on 2017/4/17.
 */
public class AcceptTransportOrderDA {

    @XStreamAlias("StUnitID")
    private String stUnitID;

    @XStreamAlias("RouteChange")
    private String routeChange;

    @XStreamAlias("Information")
    private String information;

    public String getStUnitID() {
        return stUnitID;
    }

    public void setStUnitID(String stUnitID) {
        this.stUnitID = stUnitID;
    }

    public String getRouteChange() {
        return routeChange;
    }

    public void setRouteChange(String routeChange) {
        this.routeChange = routeChange;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }
}
