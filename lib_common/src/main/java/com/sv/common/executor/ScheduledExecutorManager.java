package com.sv.common.executor;

import com.sv.common.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorManager implements Runnable {
    private static final String TAG = ScheduledExecutorManager.class.getName();

    private volatile static ScheduledExecutorManager instance;
    public static ScheduledExecutorManager getInstance() {
        if (instance == null) {
            synchronized (ScheduledExecutorManager.class) {
                if (instance == null) {
                    instance = new ScheduledExecutorManager();
                }
            }
        }
        return instance;
    }
    protected ScheduledExecutorManager() {
        tasks = new ArrayList<>();
        service = Executors.newScheduledThreadPool(corePoolSize);
    }

    private final int corePoolSize = 120;
    private final long delay = 1;
    private final long period = 1;
    private final TimeUnit timeUnit = TimeUnit.SECONDS;

    private ScheduledFuture scheduledFuture;
    private ScheduledExecutorService service;
    private int time = 0;
    private int maxTime = 1;

    private List<ScheduledExecutorTask> tasks;

    public void registerTask(ScheduledExecutorTask task){
        if(!tasks.contains(task)){
            tasks.add(task);
            recalculateMaxTime();
        }
    }

    public void unregisterTask(String name){
        for (ScheduledExecutorTask t: tasks) {
            if(t.getName().equals(name)){
                unregisterTask(t);
                break;
            }
        }
    }

    public void unregisterTask(ScheduledExecutorTask task){
        if(tasks.contains(task)){
            tasks.remove(task);
            recalculateMaxTime();
        }
    }

    private void recalculateMaxTime() {
        maxTime = 1;
        for (ScheduledExecutorTask t: tasks) {
            maxTime *= t.getRepeatTime();
        }
    }

    public void start(){
        scheduledFuture = service.scheduleWithFixedDelay(this, delay, period, timeUnit);
    }

    public void clearAndStop(boolean mayInterruptIfRunning){
        clear();
        stop(mayInterruptIfRunning);
    }

    public void stop(boolean mayInterruptIfRunning){
        scheduledFuture.cancel(mayInterruptIfRunning);
    }

    public void clear(){
        tasks.clear();
        maxTime = 1;
    }

    @Override
    public void run() {
        if(tasks.size() <= 0){
            return;
        }

        time ++;
        for (ScheduledExecutorTask task: tasks) {
            int repeatTime = task.getRepeatTime();
            if(time % repeatTime == 0){
                Logger.d(TAG, "time: " + time + "----- maxTime: " + maxTime);
                try {
                    task.run();
                }catch (Exception e){
                    Logger.e(TAG, e.getMessage(), e);
                }
            }
        }

        if(time >= maxTime){
            time = 0;
        }
    }
}
