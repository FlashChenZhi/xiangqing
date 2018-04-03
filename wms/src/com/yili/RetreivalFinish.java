package com.yili;

import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.Job;
import com.wms.domain.Location;
import com.wms.domain.RetrievalOrder;
import com.wms.domain.SystemLog;
import com.yili.vo.GetRNVo;
import commonservice.CommonService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

/**
 * Created by van on 2018/1/25.
 */
public class RetreivalFinish {
    public static void main(String[] args) {

//        finish();

    }

    public static void finish(Job job) {
        if(StringUtils.isBlank(job.getOrderNo())){
            return;
        }
        try {

            GetRNVo getRNVo = new GetRNVo();
            getRNVo.setFromLpnID(job.getContainer().trim());
            getRNVo.setQty(job.getQty().toString());
            getRNVo.setTaskDetailID(job.getOrderNo());
            getRNVo.setToLpnID(job.getContainer().trim());
            getRNVo.setStatus("Completed");

            CommonService commonService = new CommonService();
            String result = commonService.getCommonServiceSoap().taskReceivedOrCompleted(JSONObject.fromObject(getRNVo).toString());

            SystemLog.info("请求参数:[" + JSONObject.fromObject(getRNVo).toString() + "],返回数据：【" + result + "】");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void cancel(Job job) {
        try {


            GetRNVo getRNVo = new GetRNVo();
            getRNVo.setFromLpnID(job.getContainer().trim());
            getRNVo.setQty(job.getQty().toString());
            getRNVo.setTaskDetailID(job.getOrderNo());
            getRNVo.setToLpnID(job.getContainer().trim());
            getRNVo.setStatus("Cancelled");

            CommonService commonService = new CommonService();
            String result = commonService.getCommonServiceSoap().taskReceivedOrCompleted(JSONObject.fromObject(getRNVo).toString());
            SystemLog.info("请求参数:[" + JSONObject.fromObject(getRNVo).toString() + "],返回数据：【" + result + "】");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
