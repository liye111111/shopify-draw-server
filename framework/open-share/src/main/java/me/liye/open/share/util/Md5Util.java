package me.liye.open.share.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class Md5Util {
    public static String getMD5Hash(String input) {
        try {
            // 獲取 MD5 消息摘要實例
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 計算哈希
            byte[] messageDigest = md.digest(input.getBytes());

            // 轉換爲 16 進制字符串
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
