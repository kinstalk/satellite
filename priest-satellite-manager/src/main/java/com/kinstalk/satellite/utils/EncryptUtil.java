package com.kinstalk.satellite.utils;

import java.security.MessageDigest;

public class EncryptUtil {

    public static void main(String[] args) {

        //sha加密测试s
        String sha_1 = sha("123");
        String sha_2 = sha("abc");
        System.out.println(sha_1 + "\n" + sha_2);
        System.out.println("sha length: " + sha_1.length());
    }


    // sha加密
    public static String sha(String inputText) {
        return encrypt(inputText, "SHA-256");
    }

    /**
     * md5或者sha-1加密
     *
     * @param inputText     要加密的内容
     * @param algorithmName 加密算法名称：md5或者sha-1，不区分大小写
     */
    private static String encrypt(String inputText, String algorithmName) {
        if (inputText == null || "".equals(inputText.trim())) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = "md5";
        }
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes("UTF8"));
            byte s[] = m.digest();
            // m.digest(inputText.getBytes("UTF8"));
            return hex(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 返回十六进制字符串
    private static String hex(byte[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
                    3));
        }
        return sb.toString();
    }
}