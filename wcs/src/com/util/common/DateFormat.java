package com.util.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2009-2-26
 * Time: 10:57:40
 * To change this template use File | Settings | File Templates.
 */
public class DateFormat {
    public static final String YYYYMMDD="yyyyMMdd";
    public static final String HHMMSS="HHmmss";
    public static final String YYYYMMDDHHMMSS="yyyy-MM-dd HH:mm:ss";
    public static String format(Date date, String format) {
        if (StringUtils.isEmpty(date) || StringUtils.isEmpty(format)) {
            return null;
        } else {
            return new SimpleDateFormat(format).format(date);
        }
    }

}
