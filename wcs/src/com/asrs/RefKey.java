package com.asrs;

import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-8-27
 * Time: 上午10:21
 * To change this template use File | Settings | File Templates.
 */
public class RefKey {
    public static String getNext()
    {
        return StringUtils.leftPad(String.valueOf(HibernateUtil.nextSeq("seq_refkey")), 8, "0");
    }
}
