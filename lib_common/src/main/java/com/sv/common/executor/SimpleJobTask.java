package com.sv.common.executor;

import android.content.Context;

import com.sv.common.util.Logger;
import com.sv.common.view.AsyncTaskProgressDialog;


public abstract class SimpleJobTask<Params, Progress, Result> extends JobTask<Params, Progress, Result> {
    private static final String TAG = SimpleJobTask.class.getSimpleName();
    private AsyncTaskProgressDialog dialog;
    private boolean showDialog = false;
    private boolean dialogCancelable = true;

    public SimpleJobTask() {}

    public SimpleJobTask(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public SimpleJobTask(boolean showDialog, boolean dialogCancelable) {
        this.showDialog = showDialog;
        this.dialogCancelable = dialogCancelable;
    }

    private void initDialog() {
        if (null == dialog) {
            dialog = new AsyncTaskProgressDialog(getActivityContext());
            dialog.setCancelable(dialogCancelable);
            dialog.setCanceledOnTouchOutside(dialogCancelable);
        }
    }

    /**
     * Override this method to init context
     */
    protected Context getActivityContext() {
        throw new RuntimeException("activity content not set, cannot ini dialog !");
    }

    @Override
    protected void onPreExecute() {
        if (showDialog) {
            initDialog();
            try {
                dialog.show();
            } catch (Exception e) {
                Logger.e(TAG, e.getMessage());
            }
        }
        super.onPreExecute();
    }

    @Override
    protected Result doInBackground(Params... params) {
        try {
            return doRequest(params);
        } catch (Exception e) {
            Logger.e(TAG, "doRequest fail.", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        if (null != dialog) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
                Logger.e(TAG, "dimss dialog fail.", e);
            }
        }
    }

    protected abstract Result doRequest(Params... params);

}