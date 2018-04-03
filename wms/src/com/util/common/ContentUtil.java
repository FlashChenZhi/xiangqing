package com.util.common;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Copyright SKU360
 * User: van
 * Date: 9/5/14
 * Time: 10:25 AM
 */
public class ContentUtil {

    public static String getResult(String urlStr, String content) throws Exception {

        URL url = null;
        HttpURLConnection connection = null;
        url = new URL(urlStr);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(600000);
        connection.setReadTimeout(600000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(content.getBytes("utf-8"));
        out.flush();
        out.close();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuffer buffer = new StringBuffer();

        String line = "";

        while ((line = reader.readLine()) != null) {

            buffer.append(line);

        }

        reader.close();
        String result = buffer.toString();
        result = result.replaceAll("(null)\\s*,", "\"\",");
        return result;
    }

    public static String getResultJsonType(String urlStr, String content) throws Exception {
        System.out.println(urlStr);
        URL url = null;
        HttpURLConnection connection = null;
        url = new URL(urlStr);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(600000);
        connection.setReadTimeout(600000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(content.getBytes("utf-8"));
        out.flush();
        out.close();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuffer buffer = new StringBuffer();

        String line = "";

        while ((line = reader.readLine()) != null) {

            buffer.append(line);

        }

        reader.close();
        String result = buffer.toString();
        result = result.replaceAll("(null)\\s*,", "\"\",");
        return result;
    }


    /**
     * 连接到TOP服务器并获取数据
     */

    public static String getResult(String urlStr) throws Exception {

        URL url;

        HttpURLConnection connection = null;

        url = new URL(urlStr);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(600000);
        connection.setReadTimeout(600000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Charset", "utf-8");
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.flush();
        out.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuffer buffer = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }

        reader.close();
        String result = buffer.toString();
        result = result.replaceAll("(null)\\s*,", "\"\",");
        return result;
    }

    /**
     * APP,货主代码，方法，数据
     *
     * @return
     */
    public static String connectServer(String... args) throws Exception {
        String app, consignorCode, action, data = null;
        app = args[0];
        consignorCode = args[1];
        action = args[2];
        if (args.length > 3) {
            data = args[3];
        }
        StringBuffer url = new StringBuffer();
        url.append("http://localhost:8080/lyf_wms/oms/hibris/pushOrder").append(app).append("?");
        url.append("cust=").append(consignorCode);
        url.append("&method=").append(action);
        if (null != data) {
            url.append("&data=").append(data);
        }
        System.out.println(url.toString());
        String result = ContentUtil.getResult(url.toString());
        return result;
    }

//    public static void main(String[] args) throws Exception{
//        String url = "http://localhost:8080/wms_war_exploded/rubicware/searchInventory/searchStock";
//        StringBuffer sb = new StringBuffer();
//        sb.append("platformCode=1");
//        sb.append("&pageNum=2");
//        String result = ContentUtil.getResult(url,sb.toString());
//        JSONObject  jsonObject = JSONObject.fromObject(result);
//        System.out.println(result);
//    }
}
