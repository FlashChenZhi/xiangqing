package com.util.common;

import java.io.InputStream;
import java.util.Properties;


/**
 * 操作文件的工具类
 * User: xiongying
 * Date: 13-12-10
 * Time: 上午11:33
 * To change this template use File | Settings | File Templates.
 */
public class FileHelper {

    private static final FileHelper INSTANCE = new FileHelper();

    private FileHelper() {
    }

    public static FileHelper getInstance() {
        return INSTANCE;
    }

    /**
     * 读取Properties文件
     *
     * @param resourceFilePath
     * @return
     */
    public Properties loadPropFile(String resourceFilePath) {
        Properties prop = new Properties();
        InputStream is = null;
        try {
            String suffix = ".properties";
            if (resourceFilePath.lastIndexOf(suffix) == -1) {
                resourceFilePath += suffix;
            }
            is = getClass().getResourceAsStream(resourceFilePath);
            if (is != null) {
                prop.load(is);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
            }
        }
        return prop;
    }

    /**
     * 根据key获取value
     *
     * @param resourceFilePath properties文件路径
     * @param resourceKey      properties文件key
     * @return
     */
    public String read(String resourceFilePath, String resourceKey) {
        Properties properties = loadPropFile(resourceFilePath);
        if (properties.containsKey(resourceKey)) {
            return properties.getProperty(resourceKey);
        } else {
            return "";
        }
    }

    /**
     * 根据key获取value
     * eg:orderNotExist=订单号?不存在
     * read("xx.properties", "orderNotExist", new String[]{"a123"})
     * 返回:订单号a123不存在
     *
     * @param resourceFilePath properties文件路径
     * @param resourceKey      properties文件key
     * @param params           properties文件value的参数
     * @return
     */
    public String read(String resourceFilePath, String resourceKey, String[] params) {
        Properties properties = loadPropFile(resourceFilePath);
        if (properties.containsKey(resourceKey)) {
            String value = properties.getProperty(resourceKey);
            if (value == null || "".equals(value.trim()))
                return "";
            char[] chars = value.toCharArray();
            StringBuffer stringBuffer = new StringBuffer();
            int paramIndex = 0;
            for (int i = 0; i < chars.length; i++) {
                if (String.valueOf(chars[i]).equals("?")) {
                    if (params != null && paramIndex < params.length) {
                        stringBuffer.append(params[paramIndex]);
                        paramIndex++;
                        continue;
                    }
                }
                stringBuffer.append(String.valueOf(chars[i]));
            }
            return stringBuffer.toString();
        } else {
            return "";
        }
    }
}
