package com.yili.vo;

import com.util.common.Const;

/**
 * Created by van on 2018/1/25.
 */
public class GetRNVo {

    private String WhID = Const.WHID;
    private String TaskDetailID;
    private String Qty;
    private String FromLpnID;
    private String ToLpnID;
    private String Status;

    public String getWhID() {
        return WhID;
    }

    public void setWhID(String whID) {
        WhID = whID;
    }

    public String getTaskDetailID() {
        return TaskDetailID;
    }

    public void setTaskDetailID(String taskDetailID) {
        TaskDetailID = taskDetailID;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getFromLpnID() {
        return FromLpnID;
    }

    public void setFromLpnID(String fromLpnID) {
        FromLpnID = fromLpnID;
    }

    public String getToLpnID() {
        return ToLpnID;
    }

    public void setToLpnID(String toLpnID) {
        ToLpnID = toLpnID;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
