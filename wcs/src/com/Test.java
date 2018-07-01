package com;

import com.asrs.domain.LedMessage;
import com.asrs.domain.WcsMessage;

import com.asrs.domain.ScarChargeLocation;
import com.thread.blocks.Block;
import com.thread.blocks.Conveyor;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Test {
    public static void main(String[] args) {
        Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        Query msgQuery = HibernateUtil.getCurrentSession().createQuery("select wa from WcsMessage wa," +
                "WcsMessage wb where not exists(select aj.id from AsrsJob aj where " +
                "aj.mcKey = wa.mcKey) and wa.mcKey=wb.mcKey  and " +
                "wa.dock=wb.dock and wa.machineNo =wb.machineNo and wa.id != wb.id and " +
                "wb.received=true and wa.received = true  and datediff(MINUTE,wa.createDate , GETDATE())>= 10");
        List<WcsMessage> wms = msgQuery.list();
        Transaction.commit();
    }
}
