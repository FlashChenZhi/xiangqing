import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.hibernate.Query;

import java.util.List;


/**
 * Created by Administrator on 2016/12/19.
 */
public class Test {
    public static void main(String[] args) {
        Transaction.begin();
        Query query = HibernateUtil.getCurrentSession().createQuery("from Role");
        List<Menu> inventories = query.list();
        Transaction.commit(

        );
    }

    //1到8排
    public static void oneToEight(String area) {
        for (int level = 1; level <= 4; level++) {
            //level
            for (int bay = 1; bay <= 26; bay++) {
                //bay
                for (int bank = 1; bank <= 15; bank++) {
                    //bank
                    String locationNo = area + org.apache.commons.lang.StringUtils.leftPad(bank + "", 2, '0')
                            + org.apache.commons.lang.StringUtils.leftPad(bay + "", 3, '0')
                            + org.apache.commons.lang.StringUtils.leftPad(level + "", 3, '0');

                    Location location = new Location();
                    location.setLocationNo(locationNo);
                    location.setBank(bank);
                    location.setBay(bay);
                    location.setLevel(level);
                    location.setSeq(1);
                    HibernateUtil.getCurrentSession().save(location);
                }
            }
        }
    }

}
