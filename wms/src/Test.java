import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.Block;
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
        list.add("1201");
        list.add("1202");
        List list1= new ArrayList<>();
        list1.add("1203");
        list1.add("1204");
        List list2= new ArrayList<>();
        list2.add("1205");
        list2.add("1206");
        Map<String,List> stations = new HashMap<>();
        stations.put("1",list);
        stations.put("2",list1);
        stations.put("3",list2);
        if(stations.get("1")!=null){
            Query query2 ;
            if(stations.get("1").contains("1201")){
                query2=session.createQuery("select blockNo from Block where stationNo IN (:s)").setParameterList("s",stations.get("1"));
            }else {
                query2=session.createQuery("select blockNo from Block where stationNo IN (:s,:ss)").setParameterList("s",stations.get("2")).setParameterList("ss",stations.get("3"));
            }
            List<String> list3 = query2.list();
            Query query3 = session.createQuery("select fromLocation from AsrsJob a where  a.type=03 and toStation not IN (:s) ").setParameterList("s",list3);
            List<String> list4 = query3.list();
            if(list4.size()>0){
                Location byLocationNo = Location.getByLocationNo("001001001");
                for(int j=0 ; j<list4.size();j++){
                    Location byLocationNo1 = Location.getByLocationNo(list4.get(j));
                    if(byLocationNo.getPosition()!=byLocationNo1.getPosition()){
                        System.out.println("出库路径不通");
                    }else {
                        System.out.println("出库路径通");
                    }
                }
            }
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
