package com.util.common;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2009-2-26
 * Time: 10:57:40
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeFormatter
{
    private Logger logger = Logger.getLogger(DateTimeFormatter.class);
    private String format;

    public DateTimeFormatter()
    {
        format = "yyyy-MM-dd HH:mm:ss";
    }

    public DateTimeFormatter(String format)
    {
        this.format = format;
    }

    public String format(Date value)
    {
        if (value == null)
        {
            return null;
        }
        return new SimpleDateFormat(format).format(value);
    }

    public Date unformat(String value)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }
        try
        {
            return new SimpleDateFormat(format).parse(value);
        }
        catch (ParseException ex)
        {
            logger.error(ex.getMessage(), ex);

            return null;
        }
    }
}
