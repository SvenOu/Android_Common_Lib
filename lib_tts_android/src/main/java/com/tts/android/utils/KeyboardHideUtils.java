package com.tts.android.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class KeyboardHideUtils {

	private static InputMethodManager imm;
	
	public static void hideKeyboaad(Context context, EditText view){
		imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
}
