package com.sv.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;

import com.sv.common.R;

public class AsyncTaskProgressDialog extends Dialog {

    public AsyncTaskProgressDialog(Context context) {
        super(context, R.style.AsyncTaskProgressDialog);
        init();
    }

    private void init() {
        if (null != getWindow() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.dialog_loading_progress);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }
}
