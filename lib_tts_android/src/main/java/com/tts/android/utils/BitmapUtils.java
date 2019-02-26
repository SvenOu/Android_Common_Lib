package com.tts.android.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

public class BitmapUtils {
	/**
	 * 
	 * @param source
	 * @param radius
	 * @return
	 */
	public static Bitmap drawBitmapCorner(Bitmap source, float radius) {

		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
		RectF oval = new RectF(0, 0, source.getWidth(), source.getHeight());
		Canvas canvas = new Canvas(target);
		canvas.drawRoundRect(oval, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}

	/**
	 * radius = 10
	 * 
	 * @param source
	 * @return
	 */
	public static Bitmap drawBitmapCorner(Bitmap source) {
		if(null == source){
			return source;
		}
		float radius = 10;
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap target = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Config.ARGB_8888);
		RectF oval = new RectF(0, 0, source.getWidth(), source.getHeight());
		Canvas canvas = new Canvas(target);
		canvas.drawRoundRect(oval, radius, radius, paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);
		return target;
	}
}
