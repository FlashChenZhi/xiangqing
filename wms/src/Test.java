import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.Block;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Cache;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2016/12/19.
 */
public class Test {
    public static void main(String[] args) {
        try{
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            List<String> typeList = new ArrayList<>();
            typeList.add(AsrsJobType.CHANGELEVEL);
            typeList.add(AsrsJobType.RECHARGED);
            typeList.add(AsrsJobType.RECHARGEDOVER);

            Query query = HibernateUtil.getCurrentSession().createQuery(
                    "select count(*) as count, m.level as level from MCar m,AsrsJob a where not exists( " +
                            "select 1 from AsrsJob b where (b.toStation=m.blockNo or b.fromStation=m.blockNo) and type in(:types) ) " +
                            "and a.toStation=m.blockNo and a.type=:putType and m.position=:po group by m.level").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setParameter("putType", AsrsJobType.PUTAWAY);
            query.setParameterList("types",typeList);
            query.setParameter("po","1");

            List<Map<String,Object>> list =query.list();
            if(list.size()>0){
                System.out.println("11");
            }else{
                System.out.println("22");
            }
            Transaction.commit();
        }catch (Exception e){
            Transaction.rollback();
            e.printStackTrace();
        }

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
