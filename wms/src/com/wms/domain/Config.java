package com.wms.domain;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/20.
 */
@Entity
@Table(name = "CONFIG")
public class Config {
    private String key;
    private String value;
    public static final String __KEY = "key";
    public static final String MODEL_TEST = "0";//测试
    public static final String MODEL_PRODUCT = "1";//正式
    public static final String KEY_RUM_MODEL="RUN_MODEL";

    @Id
    @Column(name = "\"KEY\"")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Basic
    @Column(name = "VALUE")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static Config getByKey(String key) {
        return (Config) HibernateUtil.getCurrentSession().get(Config.class, key);
    }
}
