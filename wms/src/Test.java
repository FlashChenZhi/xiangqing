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
            int i =249;
            System.out.println((int) Math.ceil((double)i/125));
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
