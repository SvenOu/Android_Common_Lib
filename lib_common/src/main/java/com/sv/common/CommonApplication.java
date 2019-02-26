package com.sv.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;

import com.alibaba.android.arouter.launcher.ARouter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sv.common.util.Logger;
import com.sv.lib_theme.ThemeManager;
import com.umeng.analytics.MobclickAgent;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class CommonApplication extends Application {
    private static final String TAG = CommonApplication.class.getSimpleName();
    private static CommonApplication instance;
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
        initImageLoader();
        initArouter();
        initStyles();
        initUmeng();
    }

    private void initStyles() {
        ThemeManager.getInstance().init(this);
    }

    private void initUmeng() {
        MobclickAgent.setDebugMode(isDebugMode());
    }

    private void initArouter() {
        if (isDebugMode()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }

    private void initImageLoader() {
        int DISK_CACHE_SIZE = 50 * 1024 * 1024;// 50 Mb
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(DISK_CACHE_SIZE)
                .tasksProcessingOrder(QueueProcessingType.LIFO);

        if (isDebugMode()) {
            builder.writeDebugLogs(); // Remove for release app
        }
        ImageLoaderConfiguration config = builder.build();
        ImageLoader loader = ImageLoader.getInstance();
        loader.init(config);
    }
    
    private boolean isDebugMode() {
        // TODO: 2019/2/2
        return true;
    }

    public static CommonApplication getInstance() {
        if (null == instance) {
            Logger.e(TAG, "Application instance is null.");
            return null;
        }
        return instance;
    }
}
