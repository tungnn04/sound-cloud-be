package com.example.soundcloudbe.util;

import jakarta.persistence.Query;

import java.util.Map;
import java.util.Set;

public class DataConvertUtil {
    public static Integer safeToInteger(Object obj){
        if (obj != null) {
            return Integer.parseInt(obj.toString());
        }
        return null;
    }
    public static String safeToString(Object obj){
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }
    public static Boolean safeToBoolean(Object obj){
        if (obj != null) {
            return Boolean.parseBoolean(obj.toString());
        };
        return null;
    }

    public static void setParams(Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }
}
