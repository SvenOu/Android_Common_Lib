package com.sv.common.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;

public class UIUtils {

	public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
		Drawable drawable = ContextCompat.getDrawable(context, drawableId);
		return getBitmapFromVectorDrawable(context, drawable);
	}

	public static Bitmap getBitmapFromVectorDrawable(Context context, Drawable drawable) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			drawable = (DrawableCompat.wrap(drawable)).mutate();
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static Bitmap vectorToBitmap(Context ctx, @DrawableRes int resVector) {
		@SuppressLint("RestrictedApi") Drawable drawable = AppCompatDrawableManager.get().getDrawable(ctx, resVector);
		Bitmap b = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		drawable.setBounds(0, 0, c.getWidth(), c.getHeight());
		drawable.draw(c);
		return b;
	}

	public static  void dialog(Context ctx,String msg) {
		Builder builder = new Builder(ctx);
		builder.setMessage(msg);
		builder.setTitle("Warning");
		builder.setNegativeButton("OK", null);
		builder.create().show();
	}

	/***** image_util ***/
	public static Boolean haveSDCard() {
		Boolean isSDPresent = android.os.Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if (isSDPresent) {
			return isSDPresent;
		} else {
			return false;
		}

	}
	/***** image_util_end ***/
}
