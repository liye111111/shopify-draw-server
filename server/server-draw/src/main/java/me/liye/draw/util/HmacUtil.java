package me.liye.draw.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.liye.draw.ShopifyProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Created by liye on 2025-09-17.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HmacUtil {

    final ShopifyProperties shopifyProperties;

    /**
     * HMAC 校验
     */
    public boolean verifyHmac(String data, String hmacHeader) throws Exception {


        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(shopifyProperties.getApiSecret().getBytes(), "HmacSHA256"));
        byte[] hash = mac.doFinal(data.getBytes());
        String calculatedHmac = Base64.getEncoder().encodeToString(hash);
        return calculatedHmac.equals(hmacHeader);
    }
}
