package com.util.excel;


/**
 * @author xiongying
 * @ClassName: ExcelException
 * @Description:
 * @date 2014年4月10日 下午11:02:39
 */
public class ExcelException extends Exception {

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }

    public ExcelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
