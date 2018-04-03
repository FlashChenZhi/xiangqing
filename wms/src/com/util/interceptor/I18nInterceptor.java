package com.util.interceptor;

import com.util.common.MessageReaderWarpper;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 *
 * @ClassName: I18nInterceptor
 * @Description: i18n拦截器
 * @author xiongying
 * @date 2014年4月13日 下午5:27:27
 *
 */
public class I18nInterceptor implements HandlerInterceptor {
    public static final String LOCALE = "locale";
    private static final Locale CHINESE_LOCALE = new Locale("zh", "CN");
    private static final Locale ENGLISH_LOCALE = new Locale("en", "US");

    private final Logger log = Logger.getLogger(I18nInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        HttpSession session = request.getSession();
//        String locale = (String) session.getAttribute(LOCALE);

        MessageReaderWarpper readerWarpper = MessageReaderWarpper.getInstance();
        if (!readerWarpper.hasMessageSource()) {
            ApplicationContext applicationContext = RequestContextUtils.getWebApplicationContext(request);
            readerWarpper.setMessageSource(applicationContext);
        }
//        if ("en_US".equals(locale)) {
//            readerWarpper.setLocale(ENGLISH_LOCALE);
//        } else if ("zh_CN".equals(locale)) {
//            readerWarpper.setLocale(CHINESE_LOCALE);
//        } else {
//            Locale defaultLocale = Locale.getDefault();
//            readerWarpper.setLocale(defaultLocale);
//            session.setAttribute(LOCALE, defaultLocale.getLanguage() + "_" + defaultLocale.getCountry());
//        }
        readerWarpper.setLocale(CHINESE_LOCALE);
        return true;
    }

    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {
    }

    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {
    }
}
