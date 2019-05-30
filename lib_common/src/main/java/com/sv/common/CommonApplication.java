package com.sv.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.sv.common.executor.ScheduledExecutorManager;
import com.sv.common.init.InitializeIntentService;
import com.sv.common.util.Logger;

public abstract class CommonApplication extends Application {
    private static final String TAG = CommonApplication.class.getSimpleName();
    private static CommonApplication instance;
    private ScheduledExecutorManager scheduledExecutorManager;
    static {
        // 开启 5.0 以下系统的 vector 支持
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //支持 MultiDex
        MultiDex.install(this);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        startService(new Intent(this,
                InitializeIntentService.class));
    }

    public abstract boolean isDebugMode();

    public static CommonApplication getInstance() {
        if (null == instance) {
            Logger.e(TAG, "Application instance is null.");
            return null;
        }
        return instance;
    }

    public ScheduledExecutorManager getScheduledExecutorManager() {
        return scheduledExecutorManager;
    }

    public void setScheduledExecutorManager(ScheduledExecutorManager scheduledExecutorManager) {
        this.scheduledExecutorManager = scheduledExecutorManager;
    }
}
