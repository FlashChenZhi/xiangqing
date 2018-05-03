package com.master.service;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobStatus;
import com.asrs.business.consts.AsrsJobType;
import com.master.vo.SkuVo2;
import com.util.common.BaseReturnObj;
import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PutInStorageService {
    /**
     * 获取商品代码
     *
     * @return
     * @throws IOException
     */
    public ReturnObj<List<Sku>> getCommodityCode() {
        System.out.println("进入获取商品代码方法！");
        ReturnObj<List<Sku>> returnObj = new ReturnObj<>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("from Sku");
            List<Sku> skuList = query.list();
            List<Sku> mapList = new ArrayList<>();
            for (Sku sku : skuList) {
                Sku  vo= new Sku();
                vo.setId(sku.getId());
                vo.setSkuCode(sku.getSkuCode());
                vo.setSkuName(sku.getSkuName());
                mapList.add(vo);
            }
            returnObj.setSuccess(true);
            returnObj.setRes(mapList);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
            return returnObj;
    }

    /**
     * 设定任务
     * @param palletCode     托盘号
     * @param stationNo       站台
     * @param skuName 货品代码
     * @param num           数量
     * @return "0"设定成功，"1"设定失败
     * @throws IOException
     */

    public BaseReturnObj addTask(String palletCode, String stationNo, String skuName, int num) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Sku sk=new Sku();
            sk.setSkuName(skuName);
            Sku sku = Sku.getByCode(sk.getSkuCode());
            if (sku == null) {
                returnObj.setSuccess(false);
                returnObj.setMsg("商品不存在!");
                Transaction.rollback();
                return returnObj;
            }
            Container container = Container.getByBarcode(palletCode);
            if (container != null) {
                returnObj.setSuccess(false);
                returnObj.setMsg("托盘号已存在!");
                Transaction.rollback();
                return returnObj;
            }
            Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView iv where iv.palletCode = :palletCode")
                    .setString("palletCode", palletCode);
            InventoryView inventoryView = (InventoryView) query.uniqueResult();
            if (inventoryView != null) {
                returnObj.setSuccess(false);
                returnObj.setMsg("托盘号已存在");
                Transaction.rollback();
                return returnObj;
            }
            Job job = new Job();
            session.save(job);
            job.setFromStation(stationNo);
            job.setContainer(palletCode);
//          job.setSendReport(false);
            job.setCreateDate(new Date());
//            if (zhantai.equals("1101")) {
//                job.setToStation("ML01");
//            }
//            if (zhantai.equals("1102")) {
//                job.setToStation("ML02");
//            }
            job.setType(AsrsJobType.PUTAWAY);
            job.setMcKey(Mckey.getNext());
            job.setStatus(AsrsJobStatus.WAITING);

            JobDetail jobDetail = new JobDetail();
            session.save(jobDetail);
            jobDetail.setJob(job);
            jobDetail.setQty(new BigDecimal(num));

            inventoryView = new InventoryView();
            session.save(inventoryView);
            inventoryView.setPalletCode(palletCode);
            inventoryView.setQty(new BigDecimal(num));
            inventoryView.setSkuName(skuName);
            inventoryView.setSkuName(sku.getSkuName());
            inventoryView.setLotNum(null);
            returnObj.setSuccess(true);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
            return returnObj;
    }
    /**
     * @author：ed_chen
     * @date：2018/3/4 17:49
     * @description： 查询入库设定任务记录
     * @param
     * @return：com.util.common.ReturnObj<java.util.List<java.util.Map<java.lang.String,java.lang.String>>>
     */
    public PagerReturnObj<List<Map<String,Object>>> findPutInStorageOrder(int startIndex, int defaultPageSize) {
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            //根据Job表和InventoryView表查找相关信息(ID,创建时间，任务号，托盘号，数量，商品代码，商品名称，源站台和目标站台，最少量)
            Query query1 = session.createQuery("select j.id as id,j.createDate as createDate,j.mcKey as mcKey,j.container as containerId,b.qty as qty, " +
                    "b.skuCode as skuCode,b.skuName as skuName,j.fromStation as fromStation,j.toStation as toStation,b.lotNum as lotNo, " +
                    "case j.type when '01' then '入库' when '03' then '出库' else '其他' end as type," +
                    "case j.status when '0' then '就绪' when '1' then '准备运行' when '2' then '正在运行' when '3' then '完成'" +
                    " when '8' then '异常' else '其他' end as status " +
                    "from Job j, InventoryView b where j.container=b.palletCode order by j.createDate desc,j.id desc").setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query2 = session.createQuery("select count(*) from Job j, InventoryView b where  j.container=b.palletCode");
            query1.setFirstResult(startIndex);
            query1.setMaxResults(defaultPageSize);
            List<Map<String,Object>> jobList = query1.list();
            //将获取查询数量，给返回对象设值
            Long count = (Long) query2.uniqueResult();
            returnObj.setSuccess(true);
            returnObj.setRes(jobList);
            returnObj.setCount(count);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;

    }
    /**
     * @author：ed_chen
     * @date：2018/3/10 18:34
     * @description：删除入库任务
     * @param selectedRowKeysString
     * @return：com.util.common.BaseReturnObj
     */
    public BaseReturnObj deleteTask(String selectedRowKeysString) {
        BaseReturnObj returnObj = new BaseReturnObj();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            String[] sId = selectedRowKeysString.split(",");
            boolean flag = true;
            for(int i = 0;i<sId.length;i++){
                int id = Integer.valueOf(sId[i]);
                Job job = Job.getById(id);
                AsrsJob asrsJob= AsrsJob.getAsrsJobByMcKey(job.getMcKey());
                if(asrsJob==null){
                    //若不存在作业任务的情况下，根据托盘号删除该条库存数据
                    Query query = session.createQuery("delete from InventoryView i where i.palletCode = :palletCode ");
                    query.setString("palletCode",job.getContainer());
                    query.executeUpdate();
                    //清除作业任务明细后，直接删除该条任务对象
                    job.getJobDetails().clear();
                    session.delete(job);
                }else{
                    flag= false;
                    break;
                }
            }
            if(flag){
                returnObj.setSuccess(true);
                Transaction.commit();
            }else{
                Transaction.rollback();
                returnObj.setSuccess(false);
                returnObj.setMsg("请查验所删入库任务是否在执行状态！");
            }
        } catch (JDBCConnectionException ex) {
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.DB_DISCONNECTED.getName());

        } catch (Exception ex) {
            Transaction.rollback();
            returnObj.setSuccess(false);
            returnObj.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
        return returnObj;
    }
}



