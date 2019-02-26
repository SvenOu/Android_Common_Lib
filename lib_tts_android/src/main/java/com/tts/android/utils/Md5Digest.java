/**
 * 
 */
package com.tts.android.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Digest {
	private static final MessageDigest md5Digest;
	static {
		try {
	        md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	        throw new RuntimeException("can not get md5 message digest",e);
        }
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
