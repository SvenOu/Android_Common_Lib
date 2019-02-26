package com.sv.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Base64;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SharedPreferencesUtil {

    private static final String TAG = SharedPreferencesUtil.class.getSimpleName();

    private static final String DEFUALT_ENCODING_TYPE = "UTF-8";
    private static final String BLANK = "";

    private static ObjectMapper om = new ObjectMapper();

    static {
        om.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static void deleteBykey(Context context, String key) {
        context.getSharedPreferences(key, 0).edit().clear().apply();
    }

    /**
     * 不支持List
     */
    public static <T> T getObjectFromSharedPreferences(Context context,
                                                       String key, Class<T> clazz) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String jsonString = sp.getString(key, null);
        Logger.d(TAG, "getObjectFromSharedPreferences:{ " + jsonString + "}");
        if (null == jsonString) {
            Logger.e(TAG, key + " is null !");
            return null;
        }
        return readBase64JsonStringToObject(jsonString, clazz);
    }

    public static String getStringFromSharedPreferences(Context context,
                                                        String key) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        String jsonString = sp.getString(key, null);
        Logger.d(TAG, "getStringFromSharedPreferences:{ " + jsonString + "}");
        return jsonString;
    }

    /**
     * 不支持List
     */
    public static <T> void setObjectToSharedPreferences(Context context,
                                                        String key, T object) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        String jsonString = readObjectToBase64JsonString(object);
        Logger.d(TAG, "setObjectFromSharedPreferences:{ " + jsonString + "}");
        editor.putString(key, jsonString);
        editor.apply();
    }

    public static void setStringToSharedPreferences(Context context,
                                                    String key, String value) {
        SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        Logger.d(TAG, "setStringFromSharedPreferences:{ " + key + " : " + value
                + "}");
        editor.putString(key, value);
        editor.apply();
    }

    public static String base64Encoding(String message) {
        if (!StringUtils.hasText(message)) {
            return BLANK;
        }

        try {
            return Base64.encodeToString(
                    message.getBytes(DEFUALT_ENCODING_TYPE), Base64.DEFAULT);

        } catch (UnsupportedEncodingException e) {
            Logger.e(TAG, e.getMessage());
            return message;
        }
    }

    public static String base64Decoding(String encodeString) {
        if (!StringUtils.hasText(encodeString)) {
            return BLANK;
        }

        try {
            return new String(Base64.decode(encodeString, Base64.DEFAULT),
                    DEFUALT_ENCODING_TYPE);
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
            return encodeString;
        }
    }
    private static <T> String readObjectToBase64JsonString(T object) {
        try {
            String value = om.writeValueAsString(object);
            return base64Encoding(value);
        } catch (JsonProcessingException e) {
            Logger.e(TAG, e.getMessage());
            return "";
        }
    }

    private static <T> T readBase64JsonStringToObject(String json, Class<T> clazz) {
        try {
            return om.readValue(base64Decoding(json), clazz);
        } catch (JsonParseException e) {
            Logger.e(TAG, e.getMessage());
            return null;
        } catch (JsonMappingException e) {
            Logger.e(TAG, e.getMessage());
            return null;
        } catch (IOException e) {
            Logger.e(TAG, e.getMessage());
            return null;
        }
    }
}
