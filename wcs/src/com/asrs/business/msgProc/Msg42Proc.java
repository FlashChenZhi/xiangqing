package com.asrs.business.msgProc;

import com.asrs.business.consts.AsrsJobType;
import com.asrs.business.consts.StationMode;
import com.asrs.communication.MessageProxy;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.Station;
import com.asrs.message.Message42;
import com.asrs.message.MessageBuilder;
import com.asrs.message.MsgException;
import com.asrs.xml.util.XMLUtil;
import com.domain.XMLbean.Envelope;
import com.domain.XMLbean.XMLList.ControlArea.ControlArea;
import com.domain.XMLbean.XMLList.ControlArea.Sender;
import com.domain.XMLbean.XMLList.DataArea.DAList.TransportModeChangeDA;
import com.domain.XMLbean.XMLList.TransportModeChangeReport;
import com.domain.consts.xmlbean.XMLConstant;
import com.util.hibernate.Transaction;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-3-19
 * Time: 20:44:42
 * Copyright Dsl.Worgsoft.
 */
public class Msg42Proc implements MsgProcess {
    public void Do(MessageBuilder msg) throws MsgException {
        Message42 message42 = new Message42(msg.DataString);
        message42.setPlcName(msg.PlcName);
        Do(message42);
    }

    @Override
    public void setProxy(XmlProxy wmsProxy, MessageProxy wcsProxy) {
        this._wmsProxy = wmsProxy;
        this._wcsProxy = wcsProxy;
    }

    XmlProxy _wmsProxy;
    MessageProxy _wcsProxy;


    public void Do(Message42 message42) {

        //应答
        /*Sender sender = new Sender();
        sender.setDivision(XMLConstant.COM_DIVISION);
        ControlArea controlArea = new ControlArea();
        controlArea.setSender(sender);
        TransportModeChangeDA transportModeChangeDA = new TransportModeChangeDA();
        transportModeChangeDA.setMha(message42.Station);
        transportModeChangeDA.setTransportType(message42.Mode.equals("00") ? StationMode.UNKNOWN : message42.Mode);
        transportModeChangeDA.setInformation("01");
        TransportModeChangeReport transportModeChange = new TransportModeChangeReport();
        transportModeChange.setDate(new Date());
        transportModeChange.setControlArea(controlArea);
        transportModeChange.setDataArea(transportModeChangeDA);
        Envelope envelope = new Envelope();
        envelope.setTransportModeChangeReport(transportModeChange);*/
        try {
            Transaction.begin();

            if (message42.Mode.equals("00")) {
                Station station = Station.getStation(message42.Station);
                //station.setType(station.getOldMode());
                station.setMode(station.getOldMode());
            } else {

                Station station = Station.getStation(message42.Station);
                //station.setType(message42.Mode.equals(StationMode.PUTAWAY) ? AsrsJobType.PUTAWAY : AsrsJobType.RETRIEVAL);
                station.setMode(message42.Mode.equals(StationMode.PUTAWAY) ? StationMode.PUTAWAY : StationMode.RETRIEVAL2);
                station.setDirection(message42.Mode.equals(StationMode.PUTAWAY) ? StationMode.PUTAWAY : StationMode.RETRIEVAL2);
            }

            Transaction.commit();
            /*XMLUtil.sendEnvelope(envelope);*/
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }

    }
}
