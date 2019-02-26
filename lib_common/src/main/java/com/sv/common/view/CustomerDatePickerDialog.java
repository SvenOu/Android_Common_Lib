package com.sv.common.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

/**
 * Created by sven-ou on 2016/5/6.
 */
public class CustomerDatePickerDialog extends DatePickerDialog {
    public CustomerDatePickerDialog(Context context,
                                    OnDateSetListener callBack, int year, int monthOfYear,
                                    int dayOfMonth) {
        super(context, callBack, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int month, int day) {
        super.onDateChanged(view, year, month, day);
        setTitle(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).toString());
    }
}
