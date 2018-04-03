package com.util.common;


import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/7.
 */
public class JedisPoolFactory {
    private static Map<String, JedisPool> jedisPoolMap = new HashMap();

    public JedisPoolFactory() {
    }

    public static JedisPool getPool(String hostName, int port) {
        String key = String.format("%s:%d", new Object[]{hostName, Integer.valueOf(port)});
        if(!jedisPoolMap.keySet().contains(key)) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(500);
            config.setMaxIdle(5);
            config.setTestOnBorrow(true);
            jedisPoolMap.put(key, new JedisPool(config, hostName, port));
        }

        return (JedisPool)jedisPoolMap.get(key);
    }
}
