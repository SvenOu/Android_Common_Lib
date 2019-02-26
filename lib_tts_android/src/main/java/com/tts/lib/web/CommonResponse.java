package com.tts.lib.web;


import java.io.Serializable;
import java.util.Map;
import com.tts.lib.web.Response;
import java.io.Serializable;
import java.util.Map;

public class CommonResponse extends Response<Object, Map<String, Object>> implements Serializable {
    private static final long serialVersionUID = -4546350488380014155L;
    public static CommonResponse SIMPLE_SUCCESS = new CommonResponse(true, (Object)null, (String)null, (Map)null, (String)null);
    public static CommonResponse SIMPLE_FAILURE = new CommonResponse(false, (Object)null, (String)null, (Map)null, (String)null);

    public CommonResponse() {
    }

    public static CommonResponse success(String message) {
        return new CommonResponse(true, (Object)null, (String)null, (Map)null, message);
    }

    public static CommonResponse success(Object data) {
        return new CommonResponse(true, data, (String)null, (Map)null, (String)null);
    }

    public static CommonResponse success(Object data, String message) {
        return new CommonResponse(true, data, (String)null, (Map)null, message);
    }

    public static CommonResponse failure(String errorCode) {
        return new CommonResponse(false, (Object)null, errorCode, (Map)null, (String)null);
    }

    public static CommonResponse failure(String errorCode, Map<String, Object> errors) {
        return new CommonResponse(false, (Object)null, errorCode, errors, (String)null);
    }

    public static CommonResponse failure(String errorCode, Map<String, Object> errors, String message) {
        return new CommonResponse(false, (Object)null, errorCode, errors, message);
    }

    public CommonResponse(boolean success, Object data, String errorCode, Map<String, Object> errors, String message) {
        super(success, data, errorCode, errors, message);
    }
}
