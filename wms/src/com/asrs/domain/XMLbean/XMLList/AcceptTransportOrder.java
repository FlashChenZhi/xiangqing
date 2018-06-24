package com.asrs.domain.XMLbean.XMLList;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.asrs.domain.XMLbean.XMLList.DataArea.DAList.AcceptTransportOrderDA;
import com.asrs.domain.XMLbean.XMLProcess;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.util.hibernate.HibernateUtil;
import com.wms.domain.InventoryView;
import com.wms.domain.Job;
import com.wms.domain.Location;

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
        String infomation = dataArea.getInformation();
        if ("00".equals(infomation)) {
            //正确

        } else {
            String refId = controlArea.getRefId().getReferenceId();
            Job job = Job.getByMcKey(refId);

            if (job.getType().equals(AsrsJobType.PUTAWAY)) {
                Location location = job.getToLocation();
                location.setReserved(false);
                InventoryView inventoryView = InventoryView.getByPalletNo(job.getContainer());
                HibernateUtil.getCurrentSession().delete(inventoryView);
            } else {

            }
            HibernateUtil.getCurrentSession().delete(job);
        }

    }

}
