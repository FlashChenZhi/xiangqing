package com.led;

import com.asrs.domain.LedMessage;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.file.ProgramBxFile;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class LedCenter {
    public static void main(String[] args) throws Exception {
        Bx5GEnv.initial("log.properties");


        while (true) {
            List<LedMessage> ledMessages = new ArrayList<>();
            try {
                Transaction.begin();
                Session session = HibernateUtil.getCurrentSession();

                Query query = session.createQuery("from LedMessage lm where lm.processed = true");
                ledMessages = query.list();


                Transaction.commit();
            } catch (Exception ex) {
                Transaction.rollback();
                ex.printStackTrace();
            }

            for (LedMessage ledMessage : ledMessages) {
                Thread thread = new Thread(new LedSender(ledMessage));
                thread.start();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
