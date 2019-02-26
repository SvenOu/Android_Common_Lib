package com.tts.android.utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IdUtils {
	private static final String TAG = "IdUtils";

	private static final String DEFAULT_SEED = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String generateId(String prefix, String seed) {
		if (prefix == null) {
			prefix = "";
		}
		StringBuilder t = new StringBuilder(prefix + System.currentTimeMillis());

		for (int i = 0; i < 4; i++) {
			t.append(seed.charAt((int) (Math.random() * 26)));
		}
		return t.toString();
	}

	public static String generateId(String prefix) {
		if (prefix == null) {
			prefix = "";
		}
		StringBuilder t = new StringBuilder(prefix + System.currentTimeMillis());

		for (int i = 0; i < 4; i++) {
			t.append(DEFAULT_SEED.charAt((int) (Math.random() * 26)));
		}
		return t.toString();
	}

	/**
	 * 此方法在6.0以上的系统，没有权限就会报错
	 * 是旧版本的ID生成规则
	 */
	public static String generateProfileId(Context context) {
		String combineLongId = getDeviceIMEI(context) + getPseudoUniqueId()
				+ getAndroidId(context) + getWLANMacAddress(context);
		Log.d(TAG, "Profile Long Id:"+combineLongId);
		return getMD5Str(combineLongId);
	}

	/**
	 * 新版本的ID生成规则
	 */
	public static String generateProfileIdByNewRule(Context context) {
		String combineLongId = getPseudoUniqueId() + getAndroidId(context);
		Log.d(TAG, "Profile Long Id:"+combineLongId);
		return getMD5Str(combineLongId);
	}

	public static String getDeviceIMEI(Context context) {
		// Get The IMEI
		TelephonyManager TelephonyMgr = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String deviceId = TelephonyMgr.getDeviceId();
		Log.d(TAG,"IMEI:" + deviceId);
		return deviceId;
	}

	public static String getPseudoUniqueId() {
		// Get Pseudo-Unique ID
		StringBuffer sb = new StringBuffer("35");
		sb.append(Build.BOARD.length() % 10).append(Build.BRAND.length() % 10)
				.append(Build.CPU_ABI.length() % 10)
				.append(Build.DEVICE.length() % 10)
				.append(Build.DISPLAY.length() % 10)
				.append(Build.HOST.length() % 10)
				.append(Build.ID.length() % 10)
				.append(Build.MANUFACTURER.length() % 10)
				.append(Build.MODEL.length() % 10)
				.append(Build.PRODUCT.length() % 10)
				.append(Build.TAGS.length() % 10)
				.append(Build.TYPE.length() % 10)
				.append(Build.USER.length() % 10);
		String uniqueId = sb.toString();
		Log.d(TAG, "Preudo-Unique ID:" + uniqueId);
		return uniqueId;
	}

	public static String getAndroidId(Context context) {
		// The Android ID
		String androidId = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		Log.d(TAG, "Android ID:" + androidId);
		return androidId;
	}

	public static String getWLANMacAddress(Context context) {
		// WLAN MAC Address string
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		String macAddr = wm.getConnectionInfo().getMacAddress();
		Log.d(TAG, "WLAN Mac Address:" + macAddr);
		return macAddr;
	}

	public static String getBlueToothMacAddress() {
		// The BT MAC Address string
		BluetoothAdapter bluetoothAdapter = null; // Local Bluetooth adapter
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter == null){
			return "";
		}
		String btMacAddr = bluetoothAdapter.getAddress();
		Log.d(TAG, "Blue Tooth Mac address:" + btMacAddr);
		return btMacAddr;
	}

	private static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");

			messageDigest.reset();

			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			Log.e(TAG, "NoSuchAlgorithmException caught!");
		} catch (UnsupportedEncodingException e) {
			Log.e(TAG, e.getMessage());
		}

		byte[] byteArray = messageDigest.digest();

		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
		}
		Log.d(TAG, md5StrBuff.toString().toLowerCase());
		return md5StrBuff.toString().toLowerCase();
	}
}
