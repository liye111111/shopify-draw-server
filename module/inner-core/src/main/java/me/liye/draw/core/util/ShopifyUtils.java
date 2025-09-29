package me.liye.draw.core.util;

import me.liye.draw.core.config.ShopifyConfig;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by liye on 2025-09-16.
 */
public class ShopifyUtils {
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {
        String s = "5307c610a1f267e3370e7de52e276fc0a896b9b05e490a9fa8563efab47889d5";
        String secret = ShopifyConfig.API_SECRET;
        String HMAC_ALGORITHM = "HmacSHA256";
        Mac hmac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
        hmac.init(key);

        byte[] digest = hmac.doFinal(s.getBytes());

        // Shopify uses Base64 encoding for webhook HMAC
        String calculatedHmac = java.util.Base64.getEncoder().encodeToString(digest);

        System.out.println(calculatedHmac);

        String r = Base64.encodeBase64String(hmac.doFinal(s.getBytes()));
        System.out.println(r);
    }


    public static boolean verifyHmac(Map<String, String[]> params, String hmac, String secret) throws Exception {
        SortedMap<String, String> sortedParams = new TreeMap<>();
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (!entry.getKey().equals("hmac")) {
                sortedParams.put(entry.getKey(), entry.getValue()[0]);
            }
        }
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (!data.isEmpty()) data.append("&");
            data.append(entry.getKey()).append("=").append(entry.getValue());
        }

        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
        String computed = bytesToHex(mac.doFinal(data.toString().getBytes("UTF-8")));

        return computed.equalsIgnoreCase(hmac);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
