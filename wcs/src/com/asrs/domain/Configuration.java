package com.asrs.domain;

import com.util.hibernate.HibernateUtil;

import javax.persistence.*;

/**
 * Created by van on 2017/5/15.
 */
@Entity
@Table(name = "CONFIGURATION")
public class Configuration {
    private String key;
    private String value;

    public static final String KEY_RUNMODEL = "RUNMODEL";
    public static final String MODEL_ONLINE = "1";
    public static final String MODEL_OFFLINE = "0";
    public static final String CURRENTSRM = "CURRENTSRM";

    @Id
    @Column(name = "CFKEY")
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

    @Transient
    public static Configuration getConfig(String key) {
        org.hibernate.Query q = HibernateUtil.getCurrentSession().createQuery("from Configuration c where c.key=:k");
        q.setParameter("k", key);
        q.setMaxResults(1);
        return (Configuration) q.uniqueResult();
    }
}
