import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.Block;
import com.wms.domain.blocks.MCar;
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

            //查找没有任务并且有小车的母车
            List<Integer> levList=MCar.getMCarByHasNotAsrsJob("2");
            //查找不存在前往或到达此层的换层，充电，充电完成任务，存在入库任务，按照入库任务数量升序排列
            List<String> typeList = new ArrayList<>();
            typeList.add(AsrsJobType.CHANGELEVEL);
            typeList.add(AsrsJobType.RECHARGED);
            typeList.add(AsrsJobType.RECHARGEDOVER);

            Query query = HibernateUtil.getCurrentSession().createSQLQuery(
                    "select count(*) as count, m.lev as lev,m.blockNo from Block m  join AsrsJob a on a.toStation=m.blockNo and a.type=:putType where " +
                            "m.position=:po and m.type=4 and not exists( select 1 from AsrsJob b where (b.toStation=m.blockNo or " +
                            "b.fromStation=m.blockNo) and type in(:types) )  group by m.lev,m.blockNo order by count asc").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setParameter("putType", AsrsJobType.PUTAWAY);
            query.setParameterList("types",typeList);
            query.setParameter("po","2");

            List<Map<String,Object>> list =query.list();
            List<Integer> stagingList = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                Map<String,Object> map = list.get(i);
                String fromStation = map.get("blockNo").toString();
                AsrsJob asrsJob=AsrsJob.getAsrsJobByRetrievalTypeAndFromStation(fromStation);
                if(asrsJob!=null){
                    stagingList.add((int)map.get("lev"));
                }else{
                    levList.add((int)map.get("lev"));
                }
            }

            for(Integer i :stagingList){
                levList.add(i);
            }

            List<Integer> AllLevList =MCar.getMCarsByPosition("2");
            for(Integer i:AllLevList){
                if(!levList.contains(i)){
                    levList.add(i);
                }
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
