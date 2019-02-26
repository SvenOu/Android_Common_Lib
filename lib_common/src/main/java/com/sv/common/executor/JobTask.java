package com.sv.common.executor;

import android.os.AsyncTask;

public abstract class JobTask<Params, Progress, Result>  extends AsyncTask<Params, Progress, Result> {
    public AsyncTask executeJob(Params... params) {
        return executeOnExecutor(JobExecutor.getInstance(), params);
//        return execute(params);
    }
}
