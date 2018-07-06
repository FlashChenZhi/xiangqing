package com.wms.service;

import com.util.common.LogMessage;
import com.util.common.PagerReturnObj;
import com.util.common.ReturnObj;
import com.util.hibernate.HibernateUtil;
import com.util.hibernate.Transaction;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
@Service
public class FindOutOrInWarehouseService {
    /*
     * @author：ed_chen
     * @description：获取商品代码
     */
    public ReturnObj<List<Map<String, String>>> getSkuCode(){
        ReturnObj<List<Map<String, String>>> s = new ReturnObj();
        System.out.println("进入获取Sku代码方法！");
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            Query query = session.createQuery("select skuCode,skuName from Sku");
            List<Object[]> retList = query.list();
            List<Map<String,String>> mapList = new ArrayList<>();
            for (Object[] objects: retList) {
                Map<String, String> map = new HashMap();
                map.put("skuCode", objects[0].toString() );
                map.put("skuName",objects[1].toString() );
                mapList.add(map);
            }
            s.setSuccess(true);
            s.setRes(mapList);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            s.setSuccess(false);
            s.setMsg(LogMessage.DB_DISCONNECTED.getName());
        } catch (Exception ex) {
            Transaction.rollback();
            s.setSuccess(false);
            s.setMsg(LogMessage.UNEXPECTED_ERROR.getName());
        }
            return s;
    }

    /*
     * @description：查找出入库详情
     */
    public PagerReturnObj<List<Map<String,Object>>> findOutOrInWarehouse(int startIndex, int defaultPageSize, String productId, String beginDate, String endDate,String type){
        PagerReturnObj<List<Map<String,Object>>> returnObj = new PagerReturnObj<List<Map<String,Object>>>();
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();

            StringBuffer sb = new StringBuffer("select a.id as id,a.skuCode as skuCode,a.qty as qty, " +
                    "a.skuName as skuName,a.num as num,a.dateTime as dateTime,a.type as type,a.LOTNUM as lotNum " +
                    "from (select max(b.id) as id,b.skuCode as skuCode, " +
                    "b.skuName as skuName,count(*) as num,sum(qty) as qty,b.LOTNUM as LOTNUM,max(b.createDate) as dateTime," +
                    "case type when '01' then '入库' else '出库' end  as type from JOBLOG b where 1=1 ");
            StringBuffer sb1 = new StringBuffer("select count(*) from (select b.skuCode from JOBLOG b where  1=1 ");
            if(StringUtils.isNotBlank(productId)){
                sb.append("and b.skuCode =:productId ");
                sb1.append("and b.skuCode =:productId ");
            }
            if (StringUtils.isNotBlank(beginDate)) {
                sb.append("and b.createDate >= :beginDate ");
                sb1.append("and b.createDate >= :beginDate ");
            }
            if (StringUtils.isNotBlank(endDate)) {
                sb.append("and b.createDate <= :endDate ");
                sb1.append("and b.createDate <= :endDate ");
            }
            if (StringUtils.isNotBlank(type) && !"00".equals(type)) {
                sb.append("and b.type = :type ");
                sb1.append("and b.type = :type ");
            }
            sb.append(" group by skuCode,skuName,type,LOTNUM ) a  order by a.dateTime desc ");
            sb1.append("group by skuCode,skuName,type,LOTNUM)a ");
            Query query = session.createSQLQuery(sb.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            Query query1 = session.createSQLQuery(sb1.toString());

            query.setFirstResult(startIndex);
            query.setMaxResults(defaultPageSize);

            if(StringUtils.isNotBlank(productId)){
                query.setString("productId",productId);
                query1.setString("productId",productId);
            }
            if (StringUtils.isNotBlank(beginDate)) {
                query.setString("beginDate",beginDate);
                query1.setString("beginDate",beginDate);
            }
            if (StringUtils.isNotBlank(endDate)) {
                query.setString("endDate",endDate);
                query1.setString("endDate",endDate);
            }
            if (StringUtils.isNotBlank(type) && !"00".equals(type)) {
                query.setString("type",type);
                query1.setString("type",type);
            }
            List<Map<String,Object>> jobList = query.list();
            int count = (int)query1.uniqueResult();

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

    public StringBuffer getSqlAfter(StringBuffer sql,String productId, String beginDate, String endDate){
        if(StringUtils.isNotBlank(productId)){
            sql.append(" and a.skuCode = :skuCode ");
        }
        if (StringUtils.isNotBlank(beginDate)) {
            sql.append(" and a.STORE_DATE+' '+a.STORE_TIME >= :beginDate ");
        }
        if (StringUtils.isNotBlank(endDate)) {
            sql.append(" and a.STORE_DATE+' '+a.STORE_TIME <= :endDate ");
        }
        return sql;
    }
    public StringBuffer getSql2After(StringBuffer sql,String productId, String beginDate, String endDate){
        if(StringUtils.isNotBlank(productId)){
            sql.append(" and r.SKU_CODE = :skuCode2 ");
        }
        if (StringUtils.isNotBlank(beginDate)) {
            sql.append(" and  r.RETRIEVAL_DATE+' '+r.RETRIEVAL_TIME >= :beginDate2 ");
        }
        if (StringUtils.isNotBlank(endDate)) {
            sql.append(" and  r.RETRIEVAL_DATE+' '+r.RETRIEVAL_TIME <= :endDate2 ");
        }
        return sql;
    }

    /**
     * 报表导出功能
     */
    public void exportReport(String beginDate, String endDate, HttpServletResponse response, HttpServletRequest request){
        //定义输出流对象
        OutputStream ouputStream = null;
        try {
            Transaction.begin();
            Session session = HibernateUtil.getCurrentSession();
            String fileName="库存汇总表";
            String filename1 = "库存汇总表";
            //请求获取浏览器信息
            String userAgent = request.getHeader("User-Agent");
            //针对IE或者IE为内核的浏览器
            if (userAgent.contains("MSIE")||userAgent.contains("Trident")) {
                try {
                    //处理编码问题
                    filename1 = URLEncoder.encode(filename1, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                //非IE浏览器的处理
                try {
                    //转换编码方式，解决乱码问题
                    filename1 = new String(filename1.getBytes("UTF-8"),"ISO-8859-1");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            //设置浏览器针对不同数据的响应方式-设置消息头部的处理方式
            response.setContentType("application/msexcel;charset=UTF-8");
            response.setHeader("Content-disposition", "attachment;filename="+ filename1+".xls");
            response.setHeader("Content-Type", "application/force-download");
            //获取当前时间并转换格式
            Date currentTime = new Date();
            ouputStream = response.getOutputStream();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //对页面时间输入框判空处理
            if (StringUtils.isBlank(beginDate)) {
                beginDate = sdf1.format(currentTime)+" 00:00:00";
            }
            if (StringUtils.isBlank(endDate)) {
                endDate =sdf2.format(currentTime);
            }
            String currentDate =sdf2.format(currentTime);
            List<List<Map<String,Object>>> dataList= getExclContent(beginDate,endDate);

            //jxl导出报表方法
            exportExcel(dataList, ouputStream,beginDate,endDate,currentDate);
            Transaction.commit();
        } catch (JDBCConnectionException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            Transaction.rollback();
        }finally {
            if(ouputStream!=null){
                try {
                    ouputStream.close();
                    ouputStream.flush();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }

        }
    }

    /**
     * 将数据写入到excel中
     * 创建Excel的输出流
     * 要插入的数据源
     * excel表名称
     */
      public void exportExcel( List<List<Map<String,Object>>> datalines, OutputStream os,
                             String beginDate,String endDate,String currentDate ) {

        WritableWorkbook workbook = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //给单元格设置一个统一的格式
            WritableCellFormat wcformat = getExcelCellFormat();
            //创建一个excel
            workbook = Workbook.createWorkbook(os);
            //workbook = Workbook.createWorkbook(new File(path+fileName+hz));
            Map<String,String> mapTitle = new HashMap<>();
            mapTitle.put("title0", "库存汇总表");
            mapTitle.put("title1", "库存明细表");
            mapTitle.put("title2", "入库明细表");
            mapTitle.put("title3", "出库明细表");
            //循环集合
            for(int i =0;i<datalines.size();i++){
                List<Map<String,Object>> datalist = datalines.get(i);
                WritableSheet sheet=null;
                //设置字体为Arial，30号，加粗
                WritableFont font = new  WritableFont(WritableFont.ARIAL, 11 ,WritableFont.NO_BOLD);
                WritableCellFormat format = new  WritableCellFormat(font);
                WritableFont font1 = new  WritableFont(WritableFont.ARIAL, 11 ,WritableFont.BOLD);
                WritableCellFormat format1 = new  WritableCellFormat(font1);
                WritableCellFormat format2 = new  WritableCellFormat(font);
                //设置上下居中对齐
                format1.setVerticalAlignment(VerticalAlignment.CENTRE);
                //设置左右居中对齐
                format1.setAlignment(Alignment.CENTRE);
                format2.setVerticalAlignment(VerticalAlignment.CENTRE);
                //创建一个sheet
                sheet = workbook.createSheet(mapTitle.get("title"+i), i);
                //设置单元格宽度
                for(int j = 0;j<6;j++){
                    if(j==4){
                        sheet.setColumnView(j, 20);
                    }else{
                        sheet.setColumnView(j, 36);
                    }
                }
                sheet.setRowView(0, 600);
                sheet.setRowView(1, 600);
                // 合并单元格    (开始列, 开始行, 结束列, 结束行)
                sheet.mergeCells(0, 1, 2, 1);
                if(i==0){
                    // 合并单元格  (开始列, 开始行, 结束列, 结束行)
                    sheet.mergeCells(0, 0, 5, 0);
                    //添加第二行标题
                    sheet.addCell(new Label(0, 1, "时间："+currentDate,format2));
                }else if(i==1){
                    // 合并单元格    (开始列, 开始行, 结束列, 结束行)
                    sheet.mergeCells(0, 0, 6, 0);
                    sheet.addCell(new Label(6, 2, "批号"));
                    //添加第二行标题
                    sheet.addCell(new Label(0, 1, "时间："+currentDate,format2));
                }else{
                    // 合并单元格    (开始列, 开始行, 结束列, 结束行)
                    sheet.mergeCells(0, 0, 6, 0);
                    sheet.addCell(new Label(6, 2, "批号"));
                    //添加第二行标题
                    sheet.addCell(new Label(0, 1, "期间："+beginDate+"——"+endDate,format2));
                }
                //添加第一行标题
                sheet.addCell(new Label(0, 0, mapTitle.get("title"+i),format1));
                sheet.addCell(new Label(0, 2, "行号"));
                sheet.addCell(new Label(1, 2, "商品代码"));
                sheet.addCell(new Label(2, 2, "商品名称"));
                sheet.addCell(new Label(3, 2, "数量"));
                sheet.addCell(new Label(4, 2, "最近操作时间"));

                if(i!=1){
                    for(int j =0;j<datalist.size();j++){
                        Map<String,Object> dataMap = datalist.get(j);
                        int k =j+1;
                        sheet.addCell(new Label(0, j+3, k+"",format));
                        sheet.addCell(new Label(1, j+3, (String)dataMap.get("skuCode"),format));
                        sheet.addCell(new Label(2, j+3, (String)dataMap.get("skuName"),format));
                        sheet.addCell(new Label(3, j+3, (String)dataMap.get("qty"),format));
                        if(i==0){
                            sheet.addCell(new Label(5, j+3, (String)dataMap.get("dateTime"),format));
                        }else{
                            sheet.addCell(new Label(5, j+3, sdf.format((Date)dataMap.get("dateTime")),format));
                            sheet.addCell(new Label(6, j+3, (String)dataMap.get("lotNum"),format));
                        }
                    }
                }else{
                    List<Map<String,Object>> datalist0 = datalines.get(0);
                    int j=0;
                    for(int k =0;k<datalist0.size();k++){
                        Map<String,Object> dataMap0 = datalist0.get(k);
                        int z =j+k+1;

                        sheet.addCell(new Label(0, k+j+3, z+"",format));
                        sheet.addCell(new Label(1, k+j+3, (String)dataMap0.get("skuCode"),format));
                        sheet.addCell(new Label(2, k+j+3, (String)dataMap0.get("skuName"),format));
                        sheet.addCell(new Label(3, k+j+3, (String)dataMap0.get("qty"),format));
                        sheet.addCell(new Label(4, k+j+3, ""+dataMap0.get("dateTime"),format));
                        for(int h =0;h<datalist.size();h++){
                            Map<String,Object> dataMap = datalist.get(h);
                            if(dataMap.get("skuCode").toString().equals(dataMap0.get("skuCode").toString())){
                                j=j+1;
                                sheet.addCell(new Label(0, k+j+3, z+"",format));
                                sheet.addCell(new Label(1, k+j+3, "",format));
                                sheet.addCell(new Label(2, k+j+3, "",format));
                                sheet.addCell(new Label(3, k+j+3, "",format));
                                sheet.addCell(new Label(4, k+j+3, ""+dataMap.get("qty"),format));
                                sheet.addCell(new Label(5, k+j+3, (String)dataMap.get("dateTime"),format));
                                sheet.addCell(new Label(6, k+j+3, (String)dataMap.get("lotNum"),format));
                            }
                        }
                    }
                }
            }
            workbook.write();
        } catch (RowsExceededException e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (WriteException e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (IOException e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } catch (Exception e) {
            //LogUtil.error(e.getMessage(),e);
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                    System.out.println("11");
                } catch (WriteException e) {
                    //LogUtil.error(e.getMessage(),e);
                    e.printStackTrace();
                } catch (IOException e) {
                    //LogUtil.error(e.getMessage(),e);
                    e.printStackTrace();
                }
            }

        }
    }

    public static WritableCellFormat getExcelCellFormat() throws Exception {
        WritableCellFormat wcformat = new WritableCellFormat();
        wcformat.setBorder(Border.ALL, BorderLineStyle.THIN);
        return wcformat;
    }

    //查询库存汇总/库存明细/入库明细/出库明细
    public List<List<Map<String,Object>>> getExclContent(String beginDate, String endDate){
        Session session = HibernateUtil.getCurrentSession();
        List<List<Map<String,Object>>> list = new ArrayList<>();
        //查询sheet1，库存汇总表,当前时间
        StringBuffer sb1 = new StringBuffer("select a.SKUCODE as skuCode,a.SKUCODE as skuName,SUM(a.QTY) as qty,max(a.STORE_DATE+' '+a.STORE_TIME) as dateTime " +
                " from Inventory a,Sku s where a.SKUCODE=s.SKU_CODE  group by a.SKUCODE,a.SKUNAME");
        Query query1 = session.createSQLQuery( sb1.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String,Object>> list1 = query1.list();

        //查询sheet2，库存明细表，当前时间
        StringBuffer sb2 = new StringBuffer(" select a.skuCode as skuCode,a.skuName as skuName,SUM(a.qty) as qty,  max(a.STORE_DATE+' '+a.STORE_TIME) as dateTime," +
                "a.LOT_NUM as lotNum  from Inventory a,Sku s where a.skuCode=s.SKU_CODE  group by a.skuCode,a.skuName,a.LOT_NUM");
        Query query2 = session.createSQLQuery(sb2.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String,Object>> list2 = query2.list();

        //查询sheet3，入库明细表，按照用户输入时间，若无，默认当天零点到当先时间
        StringBuffer sb3 = new StringBuffer("select a.id as id,a.skuCode as skuCode,a.skuName as skuName, a.num as num ," +
                "  a.dateTime as dateTime,a.lotNum as lotNum from (select max(J.ID) as id,j.SKUCODE as skuCode,i.LOT_NUM as lotNum, " +
                "  s.SKU_NAME as skuName,count(*) as num,sum(j.QTY) as qty, max(j.CREATEDATE) as dateTime " +
                "  from JOBLOG j,SKU s,INVENTORY i where j.SKUCODE=s.SKU_CODE and s.SKU_CODE=i.SKUCODE and i.SKUCODE=j.SKUCODE " +
                "  and j.CREATEDATE >=  :beginDate  and j.CREATEDATE <= :endDate and type=:type " +
                "  group by j.SKUCODE,s.SKU_NAME,i.LOT_NUM) a  order by a.dateTime desc ");

        Query query3 = session.createSQLQuery(sb3.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query3.setString("beginDate",beginDate);
        query3.setString("endDate",endDate);
        query3.setString("type","01");
        List<Map<String,Object>> list3 = query3.list();

        //查询sheet4，出库明细表，按照用户输入时间，若无，默认当天零点到当先时间
        StringBuffer sb4 = new StringBuffer("select a.id as id,a.skuCode as skuCode,a.skuName as skuName, a.num as num ," +
                "  a.dateTime as dateTime,a.lotNum as lotNum from (select max(J.ID) as id,j.SKUCODE as skuCode,i.LOT_NUM as lotNum, " +
                "  s.SKU_NAME as skuName,count(*) as num,sum(j.QTY) as qty, max(j.CREATEDATE) as dateTime " +
                "  from JOBLOG j,SKU s,INVENTORY i where j.SKUCODE=s.SKU_CODE and s.SKU_CODE=i.SKUCODE and i.SKUCODE=j.SKUCODE " +
                "  and j.CREATEDATE >=  :beginDate  and j.CREATEDATE <= :endDate and type=:type " +
                "  group by j.SKUCODE,s.SKU_NAME,i.LOT_NUM) a  order by a.dateTime desc");
        Query query4 = session.createSQLQuery(sb4.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        query4.setString("beginDate",beginDate);
        query4.setString("endDate",endDate);
        query4.setString("type","03");
        List<Map<String,Object>> list4 = query4.list();
        list.add(list1);
        list.add(list2);
        list.add(list3);
        list.add(list4);
        return list;
    }

}
