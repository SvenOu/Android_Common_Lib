package com.tts.lib.utils;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES是一种对称的加密算法，可基于相同的密钥进行加密和解密。<br/>Java采用AES算法进行加解密的逻辑大致如下：<br/>
 * 1、生成/获取密钥<br/>
 * 2、加/解密
 * */
public class AES {
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private static final String KEY = "iseedeadpeople00";
    private static final String CHARSET = "utf-8";
    /**
     * 生成密钥
     */
    public static SecretKey geneKey() throws Exception {
        SecretKeySpec key = new SecretKeySpec(Arrays.copyOf(KEY.getBytes(CHARSET), 16), "AES");
        return key;
    }
    /**
     * 加密
     * */
    public static byte[] encrypt(String content) throws Exception{
        //指定算法(AES)、获取Cipher对象
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        //生成/读取用于加解密的密钥
        SecretKey secretKey = geneKey();
        //用指定的密钥初始化Cipher对象，指定是加密模式，还是解密模式
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        //进行最终的加解密操作
        byte[] result = cipher.doFinal(content.getBytes());
        return result;
    }
    /**
     * 解密
     * */
    public static String decrypt(byte[] encodedBytes) throws Exception{
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKey secretKey = geneKey();
        //指定解密模式
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        //对加密后的字节数组进行解密
        byte[] result = cipher.doFinal(encodedBytes);
        return new String(result, CHARSET);
    }

}
