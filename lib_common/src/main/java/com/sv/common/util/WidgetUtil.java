package com.sv.common.util;

import android.content.Context;

public class WidgetUtil {

    public static final String EN = "English";
    public static final String ES = "Español";
    public static final String en = "en";
    public static final String es = "es";
    public static final String esConuntryCode = "ES";

    public static String getLanguage(String code) {
        if (en.endsWith(code)) {
            return ES;
        }
        if (es.endsWith(code)) {
            return EN;
        }
        return "";
    }

    public static int getResourceId(Context context, String name, String defType) {
        return context.getResources().getIdentifier(name,defType, context.getPackageName());
    }
}