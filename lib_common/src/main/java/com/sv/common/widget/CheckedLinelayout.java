package com.sv.common.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckedLinelayout extends LinearLayout implements Checkable {
    private static final String TAG = CheckedLinelayout.class.getName();
    private boolean mChecked = false;
    private OnCheckChangeListener onCheckChangeListener;

    public CheckedLinelayout(Context context) {
        super(context);
    }

    public CheckedLinelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckedLinelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CheckedLinelayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        if(onCheckChangeListener != null){
            onCheckChangeListener.onCheckChange(mChecked);
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked = !mChecked;
        if(onCheckChangeListener != null){
            onCheckChangeListener.onCheckChange(mChecked);
        }
    }

    public interface OnCheckChangeListener{
        void onCheckChange(boolean mChecked);
    }

    public OnCheckChangeListener getOnCheckChangeListener() {
        return onCheckChangeListener;
    }

    public void setOnCheckChangeListener(OnCheckChangeListener onCheckChangeListener) {
        this.onCheckChangeListener = onCheckChangeListener;
    }
}
