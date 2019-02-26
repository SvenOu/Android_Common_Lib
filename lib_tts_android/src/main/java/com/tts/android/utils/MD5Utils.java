package com.tts.android.utils;

import java.security.MessageDigest;

import android.text.TextUtils;

public class MD5Utils {

	public static String getMD5(String message) {
		
		if (!TextUtils.isEmpty(message)) {
            return "";
        }
		
		MessageDigest digest = Md5Digest.getMd5MessageDigest();
		digest.reset();
		digest.update(message.getBytes());
		byte[] messageDigest = digest.digest();
		
		StringBuilder hexString = new StringBuilder();
        for (byte aMessageDigest : messageDigest) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2) {
                h = "0" + h;
            }
            hexString.append(h);
        }
       
		return hexString.toString();
	}
}
