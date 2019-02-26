package com.sv.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sv.common.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定一组 button 的 id
 * android:tag="1" or "2" 单选和多选
 */

public class ButtonGroup extends LinearLayout implements View.OnClickListener {

    private static final String TAG = ButtonGroup.class.getSimpleName();

    public static final int TYPE_SINGLE_CHOOSE = 1;
    public static final int TYPE_SINGLE_MULTIPLE_CHOOSE = 2;

    private int type = TYPE_SINGLE_CHOOSE;

    private List<Button> buttonsList;
    private EventListener listener;

    private Button selectedButton;

    public ButtonGroup(Context context) {
        super(context);
        init();
    }

    public ButtonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ButtonGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        try {
            int inputType = Integer.parseInt((String) getTag());
            if (inputType == TYPE_SINGLE_MULTIPLE_CHOOSE) {
                type = inputType;
            }
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
        buttonsList = new ArrayList<>();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int total = getChildCount();
        for (int i = 0; i < total; i++) {
            View view = getChildAt(i);
            if (view instanceof Button) {
                Button button = (Button) view;
                button.setOnClickListener(this);
                buttonsList.add(button);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (TYPE_SINGLE_CHOOSE == type) {
            for (Button btn : buttonsList) {
                if (v.getId() == btn.getId()) {
                    if (!btn.isSelected()) {
                        btn.setSelected(true);
                        if (null != listener) {
                            selectedButton = btn;
                            listener.onSelectButton(btn, this);
                        }
                    }
                } else {
                    btn.setSelected(false);
                }
            }
        } else if (v instanceof Button) {
            Button btn = (Button) v;
            boolean s = !btn.isSelected();
            btn.setSelected(s);
            if (s) {
                if (null != listener) {
                    selectedButton = btn;
                    listener.onSelectButton(btn, this);
                }
            }
        }
    }

    public void selectButton(int resId){
        for (Button btn : buttonsList) {
            if(btn.getId() == resId){
                onClick(btn);
            }
        }
    }

    public Button getLastSelectedButton() {
        return selectedButton;
    }
    public @NonNull List<Button> getSelectedButtons() {
        List<Button> btns = new ArrayList<>();
        int total = getChildCount();
        for (int i = 0; i < total; i++) {
            View view = getChildAt(i);
            if (view instanceof Button) {
                Button button = (Button) view;
                if(button.isSelected() && button.getVisibility() == VISIBLE){
                    btns.add(button);
                }
            }
        }
        return btns;
    }

    public void setEventListener(EventListener listener) {
        this.listener = listener;
    }

    public interface EventListener {
        void onSelectButton(Button selectedBtn, ButtonGroup buttonGroup);
    }

}
