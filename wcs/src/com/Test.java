package com;

import com.asrs.communication.XmlProxy;
import com.asrs.domain.LedMessage;
import com.asrs.domain.WcsMessage;

import com.asrs.domain.ScarChargeLocation;
import com.asrs.xml.util.XMLUtil;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.Session;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        try{
            Transaction.begin();
            XmlProxy _wmsproxy = (XmlProxy) Naming.lookup(Const.WMSPROXY);

            String s ="<WmsWcsXML__Envelope>\n" +
                    "  <MovementReport>\n" +
                    "    <ControlArea>\n" +
                    "      <Sender>\n" +
                    "        <Division>WCS</Division>\n" +
                    "      </Sender>\n" +
                    "      <Receiver>\n" +
                    "        <Division>WMS</Division>\n" +
                    "      </Receiver>\n" +
                    "      <CreationDateTime>2018-07-03 18:19:16</CreationDateTime>\n" +
                    "      <RefId>\n" +
                    "        <Id>3959</Id>\n" +
                    "      </RefId>\n" +
                    "    </ControlArea>\n" +
                    "    <DataArea>\n" +
                    "      <FromLocation>\n" +
                    "        <MHA></MHA>\n" +
                    "        <Rack></Rack>\n" +
                    "        <Rack></Rack>\n" +
                    "        <Rack></Rack>\n" +
                    "      </FromLocation>\n" +
                    "      <StUnitId>6ee87b29-62ec-4</StUnitId>\n" +
                    "      <ToLocation>\n" +
                    "        <MHA>0006</MHA>\n" +
                    "        <Rack>7</Rack>\n" +
                    "        <Rack>2</Rack>\n" +
                    "        <Rack>1</Rack>\n" +
                    "      </ToLocation>\n" +
                    "      <ReasonCode>11</ReasonCode>\n" +
                    "      <Information>00</Information>\n" +
                    "    </DataArea>\n" +
                    "  </MovementReport>\n" +
                    "</WmsWcsXML__Envelope>";
            _wmsproxy.addSendXML(s);
            Transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
