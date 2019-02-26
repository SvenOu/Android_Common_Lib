package com.tts.android.utils;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.lang.reflect.Field;

public class ResourceUtils {

	public static String getRealPathFromURI(Uri contentUri, Activity activity) {

		// can post image
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(contentUri, proj, null, null,
				null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
		cursor.moveToFirst();

		return cursor.getString(column_index);
	}
}
