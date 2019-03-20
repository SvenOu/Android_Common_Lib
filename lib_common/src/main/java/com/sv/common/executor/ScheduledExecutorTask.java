package com.sv.common.executor;

import android.text.TextUtils;

public abstract class ScheduledExecutorTask implements Runnable {
    private static final String TAG = ScheduledExecutorTask.class.getName();

    private int repeatTime;
    private String name;

    public ScheduledExecutorTask(int repeatTime, String name) {
        this.repeatTime = repeatTime;
        this.name = name;
        if(TextUtils.isEmpty(name)){
            throw new RuntimeException("name cannot be empty.");
        }
    }

    public int getRepeatTime() {
        return repeatTime;
    }

    public String getName() {
        return name;
    }
}
