package com.sv.common.context;

import com.github.moduth.blockcanary.BlockCanaryContext;
import com.sv.common.BuildConfig;
import com.sv.common.util.Logger;

import java.io.File;
// TODO: 2019/5/15 需要重新配置
public class AppBlockCanaryContext extends BlockCanaryContext {
    public static final String TAG = AppBlockCanaryContext.class.getSimpleName();

    @Override
    public String getQualifier() {
        return "testQualifier";
    }

    @Override
    public String getUid() {
        return "testUid";
    }

    @Override
    public String getNetworkType() {
        return "testNetworkType";
    }

    @Override
    public int getConfigDuration() {
        return 5;
    }

    @Override
    public int getConfigBlockThreshold() {
        return 5000;
    }

    // if set true, notification will be shown, else only write log file
    @Override
    public boolean isNeedDisplay() {
        return BuildConfig.DEBUG;
    }

    // path to save log file
    @Override
    public String getLogPath() {
        return "/blockcanary/performance";
    }

    @Override
    public boolean zipLogFile(File[] src, File dest) {
        return false;
    }

    @Override
    public void uploadLogFile(File zippedFile) {
        Logger.i(TAG, "uploadLogFile:  " + zippedFile.getAbsolutePath());
    }

}
