package com.util.common;

import org.apache.log4j.*;


/**
 * Created by IntelliJ IDEA.
 * Author: Zhouyue
 * Date: 2008-2-16
 * Time: 11:02:45
 * Copyright Dsl.Worgsoft.
 */
public class LogWriter
{
      public static void writeError(Class c, String log)
      {
            write(c, log, LogType.Error);
      }

      public static void write(Class c, String log, LogType logtype)
      {
            Logger logger = Logger.getLogger(c);
            switch (logtype)
            {
                  case Debug:
                        logger.debug(log);
                        break;
                  case Error:
                        logger.error(log);
                        break;
                  case Event:
                        logger.info(log);
                        break;
                  case Info:
                        logger.info(log);
                        break;
                  case Sql:
                        logger.debug(log);
                        break;
                  case Warning:
                        logger.warn(log);
                        break;
            }
      }

      public static void writeInfo(String logger,String log)
      {
            Logger.getLogger("WMS_INFO").info(log);
      }

      public static void writeXmlInfo(String logger,String log)
      {
            Logger.getLogger("XMLMessageInfo").info(log);
      }

      public static void main(String[] args) {
            writeInfo("WMS_INFO","fdsfsdfdsfdf12312312");
      }


}

