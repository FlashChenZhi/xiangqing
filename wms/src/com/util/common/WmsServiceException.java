package com.util.common;


/**
 * @author xiongying
 * @ClassName: WmsServiceException
 * @Description: 业务异常处理
 * @date 2014年4月10日 下午11:02:39
 */
public class WmsServiceException extends RuntimeException {
    private String msg;

    public WmsServiceException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }

}
