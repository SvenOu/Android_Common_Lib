package com.sv.common.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ThreadSafeSharedPreferencesUtil {

    public static void setString(Context context, String key, String val){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        synchronized (ThreadSafeSharedPreferencesUtil.class) {
            editor.putString(key, val).commit();
        }

    }
    public static String getString(Context context, String key){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        synchronized (ThreadSafeSharedPreferencesUtil.class) {
            return sp.getString(key, null);
        }
    }
}
