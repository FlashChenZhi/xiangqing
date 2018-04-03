package com.util.interceptor;


import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: Cui Shiqiang
 * Date: 14-10-23
 * Time: 下午3:13
 * Rubicware 2.0
 */
public class SessionTimeOutInterceptor implements HandlerInterceptor {

    private String[] allowUrls;

    public void setAllowUrls(String[] allowUrls) {
        this.allowUrls = allowUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        if ("Rf".equals(request.getHeader("Request-By"))) {
            return  true;
        }
        String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
        if (null != allowUrls && allowUrls.length > 0) {
            for (String allowUrl : allowUrls) {
                if (requestUrl.equals(allowUrl)) {
                    return true;
                }
            }
        }
        response.setHeader("timeOut", "false");
        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("user");
        if (StringUtils.isBlank(userName)) {
            if ("Ext".equals(request.getHeader("Request-By"))) {    //如果是由Ext.Ajax.Request提交的话，返回timeout,由Ext自动跳转到登陆页面去"login.html"
                session.invalidate();
                response.setHeader("timeOut", "true");
            } else {
                response.sendRedirect(request.getContextPath());
            }
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
