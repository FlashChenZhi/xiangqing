package com;

import com.asrs.business.msgProc.Msg50Proc;
import com.asrs.business.msgProc.MsgProcess;
import com.asrs.communication.MessageCenter;
import com.asrs.communication.XmlProxy;
import com.asrs.domain.LedMessage;
import com.asrs.domain.WcsMessage;

import com.asrs.domain.ScarChargeLocation;
import com.asrs.message.MessageBuilder;
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
            List<ScarChargeLocation> scarChargeLocationList = ScarChargeLocation.getAbleChargeLocationBySCarBlockNo("SC05");
            if(scarChargeLocationList.size()>0){
                System.out.println("11");
            }
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
        }


    }
}
