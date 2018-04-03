package com.asrs;

import com.thread.blocks.Conveyor;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by van on 2017/11/21.
 */
public class CreateBlock {
    public static void main(String[] args) {
        try {
            Transaction.begin();
            List<String> blockNos = new ArrayList<String>();
            blockNos.add("4001");
            blockNos.add("4002");
            blockNos.add("4003");
            blockNos.add("4004");
            blockNos.add("4007");
            blockNos.add("4008");
            blockNos.add("4009");
            blockNos.add("4010");
            blockNos.add("4011");
            blockNos.add("4012");
            blockNos.add("4013");
            blockNos.add("4014");

            for (String blockNo : blockNos) {
                Conveyor conveyor = new Conveyor();
                conveyor.setStatus("1");
                conveyor.setWaitingResponse(false);
                conveyor.setPlcName("BL01");
                conveyor.setBlockNo(blockNo);
                HibernateUtil.getCurrentSession().save(conveyor);
            }

            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }
}
