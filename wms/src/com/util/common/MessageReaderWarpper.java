package com.util.common;

import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author xiongying
 * @ClassName: MessageReaderWarpper
 * @Description: MessageReader的包装类
 * @date 2014-4-11 上午10:25:39
 */
public class MessageReaderWarpper {
    private static final MessageReaderWarpper instance = new MessageReaderWarpper();

    /**
     * 保存当前线程的Locale的缓存
     */
    private ThreadLocal<Locale> buffer = new ThreadLocal<Locale>();

    private MessageSource messageSource;

    public static final MessageReaderWarpper getInstance() {
        return instance;
    }

    private MessageReaderWarpper() {
    }

    /**
     * 设置Locale
     *
     * @param locale
     */
    public void setLocale(Locale locale) {
        buffer.set(locale);
    }

    Locale getLocale() {
        Locale locale = buffer.get();
        if (locale == null)
            return Locale.getDefault();
        return locale;
    }

    /**
     * 设置MessageSource
     *
     * @param source
     */
    public void setMessageSource(MessageSource source) {
        messageSource = source;
    }

    /**
     * 判断是否已经设置过MessageSource
     *
     * @return
     */
    public boolean hasMessageSource() {
        return messageSource != null;
    }

    MessageSource getMessageSource() {
        return messageSource;
    }
}
