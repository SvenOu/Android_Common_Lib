package com.sv.common.init;

import android.app.IntentService;
import android.content.Intent;

import com.alibaba.android.arouter.launcher.ARouter;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sv.common.CommonApplication;
import com.sv.common.executor.ScheduledExecutorManager;
import com.sv.lib_theme.ThemeManager;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

public class InitializeIntentService extends IntentService {
    public static final String TAG = InitializeIntentService.class.getName();
    private CommonApplication application;

    public InitializeIntentService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = CommonApplication.getInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initImageLoader();
        initArouter();
        initStyles();
        initUmeng();
        initScheduledExecutor();
        EventBus.getDefault().post(new FinishInitEvent());
    }
    private void initStyles() {
        ThemeManager.getInstance().init(application);
    }

    private void initUmeng() {
        MobclickAgent.setDebugMode(application.isDebugMode());
    }

    private void initArouter() {
        if (application.isDebugMode()) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);
    }

    private void initImageLoader() {
        int DISK_CACHE_SIZE = 50 * 1024 * 1024;// 50 Mb
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(DISK_CACHE_SIZE)
                .tasksProcessingOrder(QueueProcessingType.LIFO);

        if (application.isDebugMode()) {
            builder.writeDebugLogs(); // Remove for release app
        }
        ImageLoaderConfiguration config = builder.build();
        ImageLoader loader = ImageLoader.getInstance();
        loader.init(config);
    }

    private void initScheduledExecutor(){
        ScheduledExecutorManager scheduledExecutorManager =
                application.getScheduledExecutorManager();
        if(null == scheduledExecutorManager){
            scheduledExecutorManager = ScheduledExecutorManager.getInstance();
            application.setScheduledExecutorManager(scheduledExecutorManager);
            scheduledExecutorManager.start();
        }
    };
}
