package com.asrs;

import com.util.hibernate.HibernateUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Created by admin on 2017/3/6.
 */
public class Barcode {
    public static String getNext() {
        return StringUtils.leftPad(String.valueOf(HibernateUtil.nextSeq("seq_barcode")), 8, "0");
    }
}
