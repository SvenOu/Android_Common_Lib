package com.sv.common.executor;

import android.content.Context;
import android.support.v4.app.Fragment;


import com.sv.common.util.Logger;

import java.lang.ref.WeakReference;

public abstract class FragmentJobTask<Params, Progress, Result>extends SimpleJobTask<Params, Progress, Result> {

    private WeakReference<Fragment> fragment;

    public FragmentJobTask(Fragment fragment) {
        super();
        this.fragment = new WeakReference<Fragment>(fragment);
    }

    public FragmentJobTask(Fragment fragment, boolean showDialog) {
        super(showDialog);
        this.fragment = new WeakReference<Fragment>(fragment);
    }

    public FragmentJobTask(Fragment fragment, boolean showDialog, boolean dialogCancelable) {
        super(showDialog, dialogCancelable);
        this.fragment = new WeakReference<Fragment>(fragment);
    }

    @Override
    protected Context getActivityContext() {
        return fragment.get().getActivity();
    }

    @Override
    protected void onPostExecute(Result result){
        super.onPostExecute(result);
        Fragment fra = fragment.get();
        if(null == fra || !fra.isAdded() || null == fra.getActivity()){
            String errorMsg = "fragment not in activity, cancel the AsyncTask.";
            Logger.e(this.getClass().getSimpleName(), errorMsg);
            onException(new RuntimeException(errorMsg));
            return;
        }
        onPostExecuteSuccess(result);
    }

    protected void onException(Exception e) {}

    protected abstract void onPostExecuteSuccess(Result result);

}