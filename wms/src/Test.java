import com.asrs.business.consts.AsrsJobType;
import com.util.common.Const;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.Block;
import com.wms.domain.blocks.MCar;
import com.wms.domain.blocks.SCar;
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
            String po ="2";
            //查找没有任务并且有小车的母车
            List<Integer> levList=MCar.getMCarByHasNotAsrsJob(po);
            //查找不存在前往或到达此层的换层，充电，充电完成任务，存在入库任务，按照入库任务数量升序排列
            List<String> typeList = new ArrayList<>();
            typeList.add(AsrsJobType.CHANGELEVEL);
            typeList.add(AsrsJobType.RECHARGED);
            typeList.add(AsrsJobType.RECHARGEDOVER);

            boolean flag=true;
            MCar noGroupNomCar = MCar.getMCarByOtherLevOutKuAsrsJob(po);
            if(levList.size()!=0 && noGroupNomCar!=null){
                //存在空闲小车并且存在无小车的母车有任务,分出一辆去做出库任务
                levList.remove(0);
                flag=false;
            }
            Query query = HibernateUtil.getCurrentSession().createSQLQuery(
                    "select count(*) as count, m.lev as lev,m.blockNo from Block m  join AsrsJob a on a.toStation=m.blockNo and a.type=:putType where " +
                            "m.position=:po and m.type=4 and not exists( select 1 from AsrsJob b where (b.toStation=m.blockNo or " +
                            "b.fromStation=m.blockNo) and type in(:types) )  group by m.lev,m.blockNo order by count asc").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            query.setParameter("putType", AsrsJobType.PUTAWAY);
            query.setParameterList("types",typeList);
            query.setParameter("po",po);

            List<Map<String,Object>> list =query.list();
            List<Integer> stagingList = new ArrayList<>();

            for(int i=0;i<list.size();i++){
                Map<String,Object> map = list.get(i);
                String fromStation = map.get("blockNo").toString();
                if(flag){
                    //查找有无出库任务，有出库任务排到无出库任务的后面
                    AsrsJob asrsJob=AsrsJob.getAsrsJobByRetrievalTypeAndFromStation(fromStation);
                    if(asrsJob!=null || noGroupNomCar!=null){
                        //本层存在出库任务或者其他层存在出库任务，并且没有空闲小车
                        stagingList.add((int)map.get("lev"));
                        flag=false;
                    }else{
                        levList.add((int)map.get("lev"));
                    }
                }else{
                    levList.add((int)map.get("lev"));
                }

            }

            for(Integer i :stagingList){
                levList.add(i);
            }

            List<Integer> AllLevList =MCar.getMCarsByPosition(po);
            for(Integer i:AllLevList){
                if(!levList.contains(i)){
                    levList.add(i);
                }
            }
            List<Integer> stagingList2 = new ArrayList<>();
            for(int i=0;i<levList.size();i++){
                int level = levList.get(i);
                MCar mCar = MCar.getMCarByPosition(po, level);
                if(mCar.getGroupNo()!=null){
                    //查找有无小车电量低，有小车电量低的层，先存储
                    SCar sCar = SCar.getScarByGroup(mCar.getGroupNo());
                    if(sCar.getPower() <= Const.LOWER_POWER){
                        stagingList2.add(level);
                    }
                }
            }
            for(Integer i :stagingList2){
                if(levList.contains(i)){
                    //查找有无小车电量低，有小车电量低的层，不向此层入库
                    levList.remove(levList.indexOf(i));
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
