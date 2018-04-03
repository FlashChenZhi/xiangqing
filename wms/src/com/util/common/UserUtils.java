package com.util.common;


/**
 * @author xiongying
 * @ClassName: UserUtils
 * @Description: 获取全局用户对象的工具类
 * @date 2014-4-11 上午10:26:42
 */
public class UserUtils {
    private static ThreadLocal<String> instance = new ThreadLocal<String>();

    public static final void setUser(String user) {
        instance.set(user);
    }

    /**
     * 得到当前threadLocal中的用户，如果是null, 抛出NoUserException异常
     *
     * @return
     */
    public static final String getUser() {
        String user = instance.get();
        if (user == null)
            throw new WmsServiceException("用户不存在");
        return instance.get();
    }


    /**
     * 得到当前threadLocal中的用户名称，如果是null, 返回null, 不抛出异常
     * @return
     */
    public static final String getUserIgnoreNull() {
        String user = instance.get();
        if(user == null)
            return null;
        return user;
    }

}
