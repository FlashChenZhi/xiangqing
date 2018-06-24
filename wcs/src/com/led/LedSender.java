package com.led;

import com.asrs.domain.LedMessage;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import onbon.bx05.Bx5GEnv;
import onbon.bx05.Bx5GException;
import onbon.bx05.Bx5GScreen;
import onbon.bx05.Bx5GScreenClient;
import onbon.bx05.area.TextCaptionBxArea;
import onbon.bx05.area.page.TextBxPage;
import onbon.bx05.file.BxFileWriterListener;
import onbon.bx05.file.ProgramBxFile;
import onbon.bx05.utils.TextBinary;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class LedSender implements BxFileWriterListener<Bx5GScreen>,Runnable {
    private LedMessage ledMessage;

    public LedSender(LedMessage _ledMessage){
        ledMessage = _ledMessage;
    }


    @Override
    public void cancel(Bx5GScreen owner, String fileName, Bx5GException ex) {
        ex.printStackTrace();
    }

    @Override
    public void done(Bx5GScreen owner) {

    }

    @Override
    public void fileFinish(Bx5GScreen owner, String fileName, int total) {
        System.out.println(fileName + " fileFinish:" + total);
    }

    @Override
    public void fileWriting(Bx5GScreen owner, String fileName, int total) {
        System.out.println(fileName + " fileWriting:" + total);
    }

    @Override
    public void progressChanged(Bx5GScreen owner, String fileName, int write, int total) {
        System.out.println(fileName + " progressChanged:" + write + "/" + total);
    }

    @Override
    public void run() {
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Bx5GScreenClient screen = new Bx5GScreenClient(ledMessage.getLedNo());
            if (screen.connect(ledMessage.getIpAddress(), 5005)) {

                ProgramBxFile program = new ProgramBxFile("P000", screen.getProfile());

                TextCaptionBxArea area1 = new TextCaptionBxArea(0, 0, 256, 16, screen.getProfile());
                TextCaptionBxArea area2 = new TextCaptionBxArea(0, 16, 256, 16, screen.getProfile());
                TextCaptionBxArea area3 = new TextCaptionBxArea(0, 32, 256, 16, screen.getProfile());
                TextCaptionBxArea area4 = new TextCaptionBxArea(0, 48, 256, 16, screen.getProfile());

                TextBxPage page1 = new TextBxPage(ledMessage.getMessage1() == null ? "" : ledMessage.getMessage1());
                TextBxPage page2 = new TextBxPage(ledMessage.getMessage2() == null ? "" : ledMessage.getMessage2());
                TextBxPage page3 = new TextBxPage(ledMessage.getMessage3() == null ? "" : ledMessage.getMessage3());
                TextBxPage page4 = new TextBxPage(ledMessage.getMessage4() == null ? "" : ledMessage.getMessage4());
                area1.addPage(page1);
                area2.addPage(page2);
                area3.addPage(page3);
                area4.addPage(page4);

                //
                program.addArea(area1);
                program.addArea(area2);
                program.addArea(area3);
                program.addArea(area4);

                //
                screen.writeProgramAsync(program, new LedSender(ledMessage));
                Thread.sleep(5000);

                //
                screen.readProgramList();

                ledMessage.setProcessed(false);
                session.update(ledMessage);

                screen.disconnect();
            }
            Transaction.commit();
        } catch (Exception ex) {
            Transaction.rollback();
            ex.printStackTrace();
        }
    }
}
