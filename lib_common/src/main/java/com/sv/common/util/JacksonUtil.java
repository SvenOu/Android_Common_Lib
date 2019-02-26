package com.sv.common.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JacksonUtil {
    private static final String TAG = JacksonUtil.class.getSimpleName();
    private static ObjectMapper mapper = new ObjectMapper();

    public static String objectToJson(Object value) throws JsonProcessingException {
        String jsonStr = mapper.writeValueAsString(value);
        return jsonStr;
    }

    public static <T> T jsonStringToObject(String jsonStr, Class<T> clazz) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T obj = null;
        try {
            obj = mapper.readValue(jsonStr, clazz);
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
        }
        return obj;
    }
    public static <T> T jsonStringToObject(String jsonStr, TypeReference tyl) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        T obj = null;
        try {
            obj = mapper.readValue(jsonStr, tyl);
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
        }
        return obj;
    }

    public static <T> List<T> objectToPOJOList(Object obj, TypeReference tyl) {
        List<T> result = null;
        try {
            result = mapper.convertValue(obj, tyl);
        } catch (IllegalArgumentException e) {
            Logger.e(TAG, e.getMessage());
        }
        return result;
    }

    public static <T> T objectToPojo(Object obj, Class<T> tClass) {
        T pojo = null;
        try {
            pojo = mapper.convertValue(obj, tClass);
        } catch (IllegalArgumentException e) {
            Logger.e(TAG, e.getMessage());
        }
        return pojo;
    }

    public static <T> String listToJsonString(List<T> list) {
        String str = null;
        try {
            str = mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            Logger.e(TAG, e.getMessage());
        }
        return str;
    }

    public static String objectToJsonString(Object pojo) {
        String str = null;
        try {
            str = mapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            Logger.e(TAG, e.getMessage());
        }
        return str;
    }
}