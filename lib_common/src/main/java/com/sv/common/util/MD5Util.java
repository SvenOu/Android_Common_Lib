package com.sv.common.util;

import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	
	private static final MessageDigest md5Digest;

	static {
		try {
	        md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	        throw new RuntimeException("can not get md5 message digest",e);
        }
	}
	
	public static String getMD5(String message) {

		if (!StringUtils.hasText(message)) {
            return "";
        }

		MessageDigest digest = getMd5MessageDigest();
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
	
	public final static MessageDigest getMd5MessageDigest(){
		try {
	        MessageDigest digest = (MessageDigest)md5Digest.clone();
	        digest.reset();
	        return digest;
        } catch (CloneNotSupportedException e) {
        	e.printStackTrace();
        	throw new RuntimeException("can not get md5 message digest",e);
        }
	}
}