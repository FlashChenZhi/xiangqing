package com.util.common;

import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-16
 * Time: 11:02:45
 * Copyright Dsl.Worgsoft.
 */
public class LogWriter {

    public static void main(String[] args) {
        try {
            System.out.println(1 / 0);
        } catch (Exception e) {
            error(LoggerType.WMS, e);
        }
    }

    public static void error(LoggerType logType, Exception e) {
        Logger logger = Logger.getLogger(logType.getCode());
        logger.error(e);
    }

    public static void error(LoggerType logType, String log) {
        Logger logger = Logger.getLogger(logType.getCode());
        logger.error(log);
    }

    public static void info(LoggerType logType, String log) {
        Logger logger = Logger.getLogger(logType.getCode());
        logger.info(log);
    }

}

