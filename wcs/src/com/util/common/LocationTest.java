package com.util.common;

import com.asrs.domain.Location;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import org.apache.commons.lang.StringUtils;

/**
 * Created by van on 2017/5/15.
 */
public class LocationTest {
    public static void main(String[] args) {
        Transaction.begin();
        int seq = 0;
        for (int level = 1; level <= 4; level++) {
            for (int bank = 1; bank <= 33; bank++) {
                for (int bay = 1; bay <= 31; bay++) {
                    String locationNo = StringUtils.leftPad(bank + "", 3, '0')
                            + StringUtils.leftPad(bay + "", 3, '0')
                            + StringUtils.leftPad(level + "", 3, '0');

                    Location location = new Location();
                    location.setLevel(level);
                    location.setLocationNo(locationNo);
                    location.setBay(bay);
                    location.setBank(bank);

                    if (location.getBank() <= 8) {
                        location.setPosition(Location.RIGHT);
                    } else {
                        location.setPosition(Location.LEFT);
                    }
                    if (location.getBank() <= 28) {
                        location.setOutPosition(Location.RIGHT);
                    } else {
                        location.setOutPosition(Location.LEFT);
                    }

                    if (location.getBank() <= 8) {
                        location.setActualArea("3");
                    } else if (location.getBank() > 8 && location.getBank() <= 28) {
                        location.setActualArea("2");
                    } else if (location.getBank() > 28) {
                        location.setActualArea("1");
                    }

                    location.setSeq(seq);
                    location.setAisle(1);
                    HibernateUtil.getCurrentSession().save(location);
                    seq++;
                }
            }
        }

        Transaction.commit();
    }
}
