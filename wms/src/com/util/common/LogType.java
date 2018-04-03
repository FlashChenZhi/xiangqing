package com.util.common;

/**
 * Author: Zhouyue
 * Date: 2008-9-16
 * Time: 11:19:40
 * Copyright Daifuku Shanghai Ltd.
 */
public enum LogType
{
      Event("event"),
      Info("info"),
      Warning("warn"),
      Error("error"),
      Debug("debug"),
      Sql("sql");

      private String code;

      public String getCode() {
            return code;
      }

      public void setCode(String code) {
            this.code = code;
      }

      LogType(String code) {
            setCode(code);
      }

}
