package com.util.common;

import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: jack
 * Date: 14-10-9
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
public class JedisUtil {
    private static String hostName;
    private static int port;

    static {
        Properties prop = new Properties();
        InputStream in = Object.class.getResourceAsStream("/datasource-redis.properties");
        if (in == null) {
            hostName = "127.0.0.1";
            port = 6379;
        } else {
            try {
                prop.load(in);
                hostName = prop.getProperty("redis.hostname").trim();
                port = Integer.parseInt(prop.getProperty("redis.port").trim());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isExist(String key) {
        try {
            JedisPool jedisPool = JedisPoolFactory.getPool(hostName, port);

            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return jedis.exists(key);
            } catch (JedisException e) {

                if (jedis != null) {
                    try {
                        jedisPool.returnBrokenResource(jedis);
                    } catch (Exception e1) {
                        e.printStackTrace();
                    }
                }

                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (jedis != null) {
                    try {
                        jedisPool.returnResource(jedis);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean add(String key, String values) {
        if (StringUtils.isBlank(key)) {
            return false;
        }

        try {
            JedisPool jedisPool = JedisPoolFactory.getPool(hostName, port);

            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return null != jedis.set(key, values);
            } catch (JedisException e) {
                e.printStackTrace();
                if (jedis != null) {
                    try {
                        jedisPool.returnBrokenResource(jedis);
                    } catch (Exception e1) {
                        e.printStackTrace();
                    }
                }

                return false;
            } catch (Exception e) {
                e.printStackTrace();

                return false;
            } finally {
                if (jedis != null) {
                    try {
                        jedisPool.returnResource(jedis);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    public static String get(String key) {
        try {
            JedisPool jedisPool = JedisPoolFactory.getPool(hostName, port);

            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                return jedis.get(key);
            } catch (JedisException e) {
                e.printStackTrace();

                if (jedis != null) {
                    try {
                        jedisPool.returnBrokenResource(jedis);
                    } catch (Exception e1) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (jedis != null) {
                    try {
                        jedisPool.returnResource(jedis);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void del(String key) {
        try {
            JedisPool jedisPool = JedisPoolFactory.getPool(hostName, port);

            Jedis jedis = null;
            try {
                jedis = jedisPool.getResource();
                jedis.del(key);
            } catch (JedisException e) {
                e.printStackTrace();

                if (jedis != null) {
                    try {
                        jedisPool.returnBrokenResource(jedis);
                    } catch (Exception e1) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (jedis != null) {
                    try {
                        jedisPool.returnResource(jedis);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
