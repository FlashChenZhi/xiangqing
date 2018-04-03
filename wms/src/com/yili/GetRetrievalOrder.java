package com.yili;

import com.asrs.Mckey;
import com.asrs.business.consts.AsrsJobType;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.wms.domain.*;
import com.wms.domain.blocks.MCar;
import com.yili.vo.GetRNVo;
import commonservice.CommonService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by van on 2018/1/25.
 */
public class GetRetrievalOrder {

    public static final String param = "{  \"WhID\": \"WH113\"}";

    public static void main(String[] args) {

        while (true) {
            try{
                CommonService service = new CommonService();

                String result = service.getCommonServiceSoap().queryOutboundTask(param);

                System.out.println(result);

                JSONObject jsonObject = JSONObject.fromObject(result);

                    if ("00".equals(jsonObject.get("Status"))) {


                    JSONArray jsonArray = jsonObject.getJSONArray("TaskDetails");

                    for (int i = 0; i < jsonArray.size(); i++) {
                        try {

                            Transaction.begin();

                            Session session = HibernateUtil.getCurrentSession();
                            JSONObject object = jsonArray.getJSONObject(i);

                            Job job = Job.getByContainer(String.valueOf(object.get("FromLpnID")));

                            if (job == null) {
                                String wmsLocationNo = String.valueOf(object.get("FromLoc"));
                                BigDecimal qty = new BigDecimal(String.valueOf(object.get("Qty")));
                                Location fromLocation = Location.getOutLocation(wmsLocationNo);


                                if(fromLocation != null) {
                                    MCar mCar = MCar.getMCarByPosition(StringUtils.isNotBlank(fromLocation.getOutPosition()) ? fromLocation.getOutPosition() : fromLocation.getPosition(), fromLocation.getLevel());

                                    Configuration configuration = Configuration.getConfig(mCar.getBlockNo());
                                    String[] stations = configuration.getValue().split(",");
                                    String toStation = "";
                                    int jobCount = 9999;
                                    for (String stationNo : stations) {
                                        Query q = session.createQuery("from Job j where j.toStation = :stationNo")
                                                .setString("stationNo", stationNo);
                                        int count = q.list().size();
                                        if (jobCount > count) {
                                            jobCount = count;
                                            toStation = stationNo;
                                        }
                                    }

                                    job = new Job();
                                    job.setType(AsrsJobType.RETRIEVAL);
                                    job.setCreateDate(new Date());
                                    job.setMcKey(Mckey.getNext());
                                    job.setContainer(String.valueOf(object.get("FromLpnID")));
                                    job.setFromStation(mCar.getBlockNo());
                                    job.setQty(qty);
                                    job.setToStation(toStation);
                                    job.setFromLocation(fromLocation);
                                    job.setOrderNo(String.valueOf(object.get("TaskDetailID")));

                                    session.save(job);


                                    AsrsJob asrsJob = new AsrsJob();
                                    asrsJob.setToStation(job.getToStation());
                                    asrsJob.setStatus("1");
                                    asrsJob.setStatusDetail("0");
                                    asrsJob.setPriority(0);
                                    asrsJob.setSendReport(false);
                                    asrsJob.setIndicating(true);
                                    asrsJob.setFromStation(mCar.getBlockNo());
                                    asrsJob.setBarcode(job.getContainer());
                                    asrsJob.setType(AsrsJobType.RETRIEVAL);
                                    asrsJob.setMcKey(job.getMcKey());
                                    asrsJob.setFromLocation(fromLocation.getLocationNo());
                                    session.save(asrsJob);

                                    fromLocation.setReserved(true);
                                }
                            }

                            Transaction.commit();


                            if(job != null) {
                                GetRNVo getRNVo = new GetRNVo();
                                getRNVo.setFromLpnID(job.getContainer());
                                getRNVo.setQty(job.getQty().toString());
                                getRNVo.setTaskDetailID(String.valueOf(object.get("TaskDetailID")));
                                getRNVo.setToLpnID(job.getContainer());
                                getRNVo.setStatus("InProcess");

                                CommonService commonService = new CommonService();
                                commonService.getCommonServiceSoap().taskReceivedOrCompleted(JSONObject.fromObject(getRNVo).toString());
                            }

                        } catch (Exception e) {
                            Transaction.rollback();
                            e.printStackTrace();
                        }

                    }


                }
         } catch(Exception e){
                e.printStackTrace();
                Transaction.rollback();
            } finally{
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
