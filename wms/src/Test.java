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
        Transaction.begin();
        Session session = HibernateUtil.getCurrentSession();
//        int startIndex=1,  defaultPageSize=1;
//        String productId="";
//        StringBuffer sb1 = new StringBuffer("select * from (select a.id as id,a.skuCode as skuCode,a.skuName as skuName, " +
//                " a.qty as qty, a.STORE_DATE+' '+a.STORE_TIME as dateTime,'入库' as type from INVENTORY a where 1=1   ");
//        StringBuffer sb2 = new StringBuffer("select count(*) from INVENTORY  a where  1=1  ");
//        StringBuffer sb3 = new StringBuffer("select count(*) from RETRIEVAL_RESULT r where 1=1");
//        sb1 = getSqlAfter(sb1, productId, beginDate, endDate);
//        sb2 = getSqlAfter(sb2, productId, beginDate, endDate);
//
//        sb1.append(" union all");
//        sb1.append(" select  r.ID as id,r.SKU_CODE as skuCode,r.SKU_NAME as skuName,r.QTY as qty , " +
//                " r.RETRIEVAL_DATE+' '+r.RETRIEVAL_TIME as dateTime, '出库' as type from RETRIEVAL_RESULT r  where 1=1  ");
//        sb1 = getSql2After(sb1, productId, beginDate, endDate);
//        sb3 = getSql2After(sb3, productId, beginDate, endDate);
//        sb1.append(" ) c order by c.dateTime desc ");
//        Query query1 = session.createSQLQuery( sb1.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//        Query query2 = session.createSQLQuery(sb2.toString());
//        Query query3 = session.createSQLQuery(sb3.toString());
//        query1.setFirstResult(startIndex);
//        query1.setMaxResults(defaultPageSize);
//
//        List<Map<String,Object>> jobList = query1.list();
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
