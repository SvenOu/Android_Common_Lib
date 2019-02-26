package com.tts.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkMonitor {
	public static boolean isNetworkOnline(Context appContext) {
		boolean status = false;
		try {
			ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (netInfo != null
					&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
				status = true;
			} else {
				netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if (netInfo != null
						&& netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    status = true;
                }
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return status;
	}

}
