package me.liye.open.share.util;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

/**
 * @author knight@momo.com
 * 2022/8/25
 **/
public class Base64Util {

    /**
     * base64 編碼
     *
     * @param encodeStr 編碼前字符串
     * @return 編碼後字符串
     */
    public static String encode(String encodeStr){
        byte[] encodeBytes = Base64.encodeBase64(encodeStr.getBytes(StandardCharsets.UTF_8));
        return new String(encodeBytes,StandardCharsets.UTF_8);
    }

    /**
     * base64 解碼
     *
     * @param decodeStr 解碼前字符串
     * @return 解碼後字符串
     */
    public static String decode(String decodeStr){
         byte[] decodeBytes = Base64.decodeBase64(decodeStr.getBytes(StandardCharsets.UTF_8));
         return new String(decodeBytes,StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        String encode = encode("30477077-ca4a-4493-b8e5-32e0e8aae5de");
        System.out.println(encode);
        String decode = decode("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJndWVzdEBzaG9wYXN0cm8uY29tIiwiaWF0IjoxNjYzODI5NzI5LCJleHAiOjE2NjUxMjU3MjksInVzZXIiOiJ7XCJjb29raWVEb21haW5cIjpcInd5enNlbGYuYmV0YS5pc2hvcGFzdHJvLmNvbVwiLFwiZW1haWxcIjpcImd1ZXN0QHNob3Bhc3Ryby5jb21cIixcImxvZ2luXCI6ZmFsc2UsXCJyb2xlc1wiOltdLFwic2V0dGluZ3NcIjp7XCJvcmRlcl9hdXRoX3NldHRpbmdcIjpcImFsbG93X2FjY291bnRcIixcInNob3dfdW5hdXRoX29yZGVyXCI6XCJuZWVkX3N5bmNcIn0sXCJzaG9wSWRcIjozNjQ5NzY3OTk4MjgyMjUsXCJ2ZXJzaW9uXCI6XCJ2MVwifSJ9.sC9YWhiwGxVxXmLccSEcWI1caQidWzR_vZm0_AhPYDQ");
        System.out.println(decode);
    }
}
