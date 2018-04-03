package com.yili;


import com.util.common.Const;
import com.util.common.ContentUtil;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import com.webservice.vo.RetrievalSuccessVo;
import com.wms.domain.*;
import net.sf.json.JSONObject;
import org.hibernate.Query;

import java.util.*;

import java.util.List;
import java.util.Vector;

import javax.xml.namespace.QName;

/**
 * Created by van on 2017/12/14.
 */
public class WebService {

    private static String url = "http://10.60.137.170:5656/ZDHService/WebService/CommonService.asmx";//提供接口的地址
    private static String namespace = "http://10.60.137.170";
    private static String soapaction = "CommonService/";   //域名，这是在server定义的

//    private static String url = "http://www.webxml.com.cn/WebServices/WeatherWebService.asmx";//提供接口的地址
//    private static String soapaction = "http://WebXml.com.cn/";   //域名，这是在server定义的

    private static String MOVEINVLOCATION = "MoveInvLocation";//货位移动
    private static String QUERYINVADJUSTMENT = "QueryInvAdjustment";//库存调整查询
    private static String QUERYINVTRANSFER = "QueryInventory";//库存属性转移查询
    private static String QUERYOUTBOUNDTASK = "QueryOutboundTask";//查询出库任务
    private static String QUERYRECEIPTLPN = "QueryReceiptLPN";//查询入库LPN
    private static String QUERYRECEIPTPLANS = "QuerySku";//查询货品
    private static String RECEIVINGANDPUTAWAYLPN = "ReceivingAndPutawayLPN";//LPN收货上架
    private static String TASKRECEIVEDORCOMPLETED = "TaskReceivedOrCompleted";//任务接受及完成

    public static void main(String args[]) {
//        getSku();
//        moveInvLocation();
    }

//    public static void getSku() {
//        String param = "{\"WhID\": \"WH1\",\"BeginDateTime\": \"2014-01-01\",\"EndDateTime\": \"2017-12-30\",\"OwnerID\":\"SN\"}";
//        Service service = new Service();
//        try {
//            Call call = (Call) service.createCall();
//            call.setTargetEndpointAddress(url);
//            call.setOperationName(new QName(soapaction, QUERYRECEIPTPLANS)); //设置要调用哪个方法
//            call.addParameter(new QName(soapaction, "quyPara"), //设置要传递的参数
//                    org.apache.axis.encoding.XMLType.XSD_STRING,
//                    javax.xml.rpc.ParameterMode.IN);
//            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);
//            call.setUseSOAPAction(true);
//            call.setSOAPActionURI(soapaction + QUERYRECEIPTPLANS);
//
//            String v = (String) call.invoke(new String[]{"你好"});
//            System.out.println(v);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    public static void moveInvLocation() {
//        String param = "{\"WhID\": \"WH1\",\"OwnerID\":\"SN\"}";
//        Service service = new Service();
//        try {
//            Call call = (Call) service.createCall();
//            call.setTargetEndpointAddress(url);
//            call.setOperationName(new QName(soapaction, MOVEINVLOCATION)); //设置要调用哪个方法
//            call.addParameter(new QName(soapaction, "quyPara"), //设置要传递的参数
//                    org.apache.axis.encoding.XMLType.XSD_STRING,
//                    javax.xml.rpc.ParameterMode.IN);
//            call.setReturnType(new QName(soapaction, MOVEINVLOCATION), String.class); //要返回的数据类型（自定义类型）
//
//            call.setUseSOAPAction(true);
//            call.setSOAPActionURI(soapaction + MOVEINVLOCATION);
//
//            String v = (String) call.invoke(new Object[]{param});//调用方法并传递参数
//            System.out.println(v);
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//
//    public static void run() {
//        String City = "北京";//getWeatherbyCityNameResult
//        Service service = new Service();
//        try {
//            Call call = (Call) service.createCall();
//            call.setTargetEndpointAddress(url);
//            call.setOperationName(new QName(soapaction, "getWeatherbyCityName")); //设置要调用哪个方法
//            call.addParameter(new QName(soapaction, "theCityName"), //设置要传递的参数
//                    org.apache.axis.encoding.XMLType.XSD_STRING,
//                    javax.xml.rpc.ParameterMode.IN);
//            call.setReturnType(new QName(soapaction, "getWeatherbyCityName"), Vector.class); //要返回的数据类型（自定义类型）
//
//            call.setUseSOAPAction(true);
//            call.setSOAPActionURI(soapaction + "getWeatherbyCityName");
//
//            Vector v = (Vector) call.invoke(new Object[]{City});//调用方法并传递参数
//            for (int i = 0; i < v.size(); i++) {
//                System.out.println(v.get(i));
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public static InventoryView getPutawayInfo(String palletNo) {
        Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView where palletCode=:palletNo");
        query.setParameter("palletNo", palletNo);
        query.setMaxResults(1);
        InventoryView view = (InventoryView) query.uniqueResult();
        return view;

    }

    public static void save(String palletNo) {
        Container container = null;

        Query query = HibernateUtil.getCurrentSession().createQuery("from InventoryView where palletCode=:palletNo");
        query.setParameter("palletNo", palletNo);
        List<InventoryView> views = query.list();


        container = Container.getByBarcode(palletNo);

        if (container == null) {
            container = new Container();
            container.setBarcode(palletNo);
            container.setLocation(Location.getByLocationNo("000"));
            container.setCreateDate(new Date());
            container.setCreateUser("sys");
            container.setReserved(true);
            HibernateUtil.getCurrentSession().save(container);
        }

        for (InventoryView view : views) {

            if (view != null) {
                Inventory inventory = new Inventory();
                inventory.setWhCode(view.getWhCode());
                inventory.setLotNum(view.getLotNum());
                inventory.setCaseBarCode(view.getCaseBarCode());
                inventory.setQty(view.getQty());
                inventory.setCaseQty(view.getCaseQty());
                inventory.setSkuCode(view.getSkuCode());
                inventory.setContainer(container);
                HibernateUtil.getCurrentSession().save(inventory);
            }
        }
    }

    /**
     * 出库完成
     */
    public static void finishOrder(Job job) {
        try {

            Container container = Container.getByBarcode(job.getContainer());
            Set<Inventory> inventorySet = new HashSet<>(container.getInventories());

            RetrievalOrder retrievalOrder = RetrievalOrder.getByOrderNo(job.getOrderNo());
            RetrievalSuccessVo successVo = new RetrievalSuccessVo();
            successVo.setWhCode(retrievalOrder.getWhCode());
            successVo.setOrderType(retrievalOrder.getJobType());
            successVo.setOrderCode(retrievalOrder.getOrderNo());
            successVo.setPalletCode(job.getContainer());
            successVo.setItemCode(container.getInventories().iterator().next().getSkuCode());
            String param = JSONObject.fromObject(successVo).toString();

            HibernateUtil.getCurrentSession().delete(job);

            Iterator<Inventory> iterator = inventorySet.iterator();
            while (iterator.hasNext()) {
                Inventory inventory = iterator.next();
                HibernateUtil.getCurrentSession().delete(inventory);
            }

            HibernateUtil.getCurrentSession().delete(container);

            String result = ContentUtil.getResultJsonType(Const.OPPLE_OUT_WMS_URL, param);
            JSONObject resultJson = JSONObject.fromObject(result);
            if (!(Boolean) resultJson.get("success")) {
                throw new Exception("上传出库完成失败");
            }
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }

    /**
     * 上架完成
     */
    public static void finishPutaway(String palletNo) {
        try {
            Transaction.begin();
            //上架完成，保存库存
            save(palletNo);

            //上报上位ERP
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("palletCode", palletNo);
            String param = jsonObject.toString();
            String result = ContentUtil.getResultJsonType(Const.OPPLE_IN_WMS_URL, param);
            JSONObject resultJson = JSONObject.fromObject(result);
            if (!(Boolean) resultJson.get("success")) {
                throw new Exception("上传入库完成失败");
            }
            Transaction.commit();
        } catch (Exception e) {
            Transaction.rollback();
            e.printStackTrace();
        }
    }

}
