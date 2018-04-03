package com.util.common;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    public static Map<String, Class<?>> stringMapToClassMap(Map<String, String> srcMap) {
        Map<String, Class<?>> map = new HashMap<String, Class<?>>();
        for (String key : srcMap.keySet()) {
            Class<?> clazz = null;
            try {
                String className = srcMap.get(key);
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            map.put(key, clazz);
        }
        return map;
    }
}
