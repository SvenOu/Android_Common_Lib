package com.sv.common.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.github.kayvannj.permission_utils.PermissionUtil;

public class PermissionCheckUtils extends PermissionUtil {

	public static boolean checkPermission(Context context, String[] permissions){
		boolean allPermissionGranted = true;
		for(String permission:permissions){
			if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
				Log.e("PermissionCheckUtils", "-----no permission: " + permission + "-----");
				allPermissionGranted = false;
			}
		}
		return allPermissionGranted;
	}
}