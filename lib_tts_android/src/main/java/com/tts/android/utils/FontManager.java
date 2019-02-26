package com.tts.android.utils;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontManager {

	public static final String FONTKRISTEN = "ITCKRIST.TTF";
	public static final String FONT300 = "Museo_Slab_300.otf";
	public static final String FONT500 = "Museo_Slab_500.otf";
	public static final String FONT500I = "Museo_Slab_500italic.otf";
	public static final String FONT700 = "Museo_Slab_700.otf";
	public static final String FONT700I = "Museo_Slab_700italic.otf";
	public static final String FONT900 = "Museo_Slab_900.otf";
	public static final String FONT900I = "Museo_Slab_900italic.otf";
	
	public static void changeTextFont(View view, Activity act, String fontType){
		changeTextFont(view, act, fontType, -1);
	}
	
	public static void changeTextFont(View view, Activity act, String fontType, float size){

		Typeface tf = Typeface.createFromAsset(act.getAssets(), "fonts/"+fontType);
		if (view instanceof TextView) {
			((TextView) view).setTypeface(tf);
			if(size > -1){
				((TextView) view).setTextSize(size);
			}
		} else if (view instanceof Button) {
			((Button) view).setTypeface(tf);
			if(size > -1){
				((Button) view).setTextSize(size);
			}
		} else if (view instanceof EditText) {
			((EditText) view).setTypeface(tf);
			if(size > -1){
				((EditText) view).setTextSize(size);
			}
		}
	}
	
	public static void changeFonts(ViewGroup root, Activity act, String fontType) {
		changeFonts(root, act, fontType, -1);
	}
	
	public static void changeFonts(ViewGroup root, Activity act, String fontType, float size) {

		Typeface tf = Typeface.createFromAsset(act.getAssets(), "fonts/"+fontType);
		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
				if(size > -1){
					((TextView) v).setTextSize(size);
				}
			} else if (v instanceof Button) {
				((Button) v).setTypeface(tf);
				if(size > -1){
					((Button) v).setTextSize(size);
				}
			} else if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
				if(size > -1){
					((EditText) v).setTextSize(size);
				}
			} else if (v instanceof ViewGroup) {
				changeFonts((ViewGroup) v, act, fontType, size);
			}
		}
	}

}
