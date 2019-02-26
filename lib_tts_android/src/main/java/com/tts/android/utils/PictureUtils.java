package com.tts.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class PictureUtils {
	private static final String TAG = PictureUtils.class.getSimpleName();

	public static Bitmap decodeSampledBitmapFromFile(String relativePath,
			int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(relativePath, options);
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(relativePath, options);
	}

	public static Bitmap getSampledBitmapFromUri(Context ctx, Uri uri,
			int targetWidth, int targetHeight) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options outDimens = getBitmapDimensions(ctx, uri);
			int sampleSize = 0;
			sampleSize = calculateSampleSize(outDimens.outWidth,
					outDimens.outHeight, targetWidth, targetHeight);
			bitmap = downSampleBitmap(ctx, uri, sampleSize);

		} catch (Exception e) {
			// handle the exception(s)
		}

		return bitmap;
	}

	private static BitmapFactory.Options getBitmapDimensions(Context ctx,
			Uri uri) throws FileNotFoundException, IOException {
		BitmapFactory.Options outDimens = new BitmapFactory.Options();
		outDimens.inJustDecodeBounds = true; // the decoder will return null (no
												// bitmap)

		InputStream is = ctx.getContentResolver().openInputStream(uri);
		// if Options requested only the size will be returned
		BitmapFactory.decodeStream(is, null, outDimens);
		is.close();

		return outDimens;
	}

	private static int calculateSampleSize(int width, int height,
			int targetWidth, int targetHeight) {
		int sampleSize = 1;

		if (height > targetHeight || width > targetWidth) {

			final int heightRatio = Math.round((float) height
					/ (float) targetHeight);
			final int widthRatio = Math.round((float) width
					/ (float) targetWidth);

			sampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return sampleSize;
	}

	private static Bitmap downSampleBitmap(Context ctx, Uri uri, int sampleSize)
			throws FileNotFoundException, IOException {
		Bitmap resizedBitmap;
		BitmapFactory.Options outBitmap = new BitmapFactory.Options();
		outBitmap.inJustDecodeBounds = false; // the decoder will return a
												// bitmap
		outBitmap.inSampleSize = sampleSize;

		InputStream is = ctx.getContentResolver().openInputStream(uri);
		resizedBitmap = BitmapFactory.decodeStream(is, null, outBitmap);
		is.close();

		return resizedBitmap;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static InputStream convertFileToInoutStream(String filePath) throws IOException {
		File file = new File(filePath);
		InputStream in = null;

		try {
			in = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			Log.e(TAG,e.getMessage());
		}
		return in;
	}
}
