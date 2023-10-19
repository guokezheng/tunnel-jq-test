package com.tunnel.business.datacenter.util;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;


public class DESUtil {
    /**
     * @return 加密后的字符串的base64格式
     * @Description: 加密
     * @Param sourceStr 原始未加密明文 key 密钥，长度必须大于等于8位
     * @Date: 2023/5/25 13:24
     **/

    public static String encryptDesToBase64(String sourceStr, String key) {
        try {
            if (sourceStr == null || sourceStr.isEmpty()) {
                throw new Exception("原文不能为空！");
            }
            if (key == null || key.isEmpty() || key.length() < 8) {
                throw new Exception("密钥不能为空，且密钥必须大于等于8位！");
            }
            byte[] datasource = sourceStr.getBytes("utf-8");
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(key.getBytes("utf-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey securekey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            return Base64.encodeBase64String(cipher.doFinal(datasource));

        } catch (Throwable e) {
            System.out.println("DES 加密异常，详情：" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 解密后的字符串
     * @Description: 解密
     * @Param encodedStrBase64 加密过的密文base64形式（由加密方法返回的）  key 密钥，长度必须大于等于8位
     * @Date: 2023/5/25 13:27
     **/
    public static String decrypt(String encodedStrBase64, String key) throws Exception {
        if (encodedStrBase64 == null || encodedStrBase64.isEmpty()) {
            throw new Exception("密文不能为空！");
        }

        if (key == null || key.isEmpty() || key.length() < 8) {
            throw new Exception("密钥不能为空，且密钥必须大于等于8位！");
        }

        byte[] src = Base64.decodeBase64(encodedStrBase64);

        SecureRandom random = new SecureRandom();
        DESKeySpec desKey = new DESKeySpec(key.getBytes("utf-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        return new String(cipher.doFinal(src));
    }

    public static void main(String[] args) {
        String d = encryptDesToBase64("dataUser", "CPs6i7cqjEthTZ4I8wU");
        String s = encryptDesToBase64("single123!@#", "CPs6i7cqjEthTZ4I8wU");
        String t = encryptDesToBase64("1697098845000", "CPs6i7cqjEthTZ4I8wU");

        System.out.println(d);
        System.out.println(s);
        System.out.println(t);
    }
}
