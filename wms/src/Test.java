import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.hibernate.Cache;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/12/19.
 */
public class Test {
    public static void main(String[] args) {
        Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
        List list= new ArrayList<>();
        list.add(11);
        list.add(12);
        List list1= new ArrayList<>();
        list.add(21);
        list.add(22);
        List list2= new ArrayList<>();
        list.add(31);
        list.add(32);
        Map<String,List> stations = new HashMap<>();
        stations.put("1",list);
        stations.put("2",list1);
        stations.put("3",list2);
        if(stations.get("1")!=null){
            System.out.println(1);

            Query query1 = session.createSQLQuery("(select count(*) from AsrsJob ) " +
                    "union all( select count(*) from AsrsJob )");
            List<Integer> list3 = query1.list();
            System.out.println(list3.size());
        }
        Transaction.commit();
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
