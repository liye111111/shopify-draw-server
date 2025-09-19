package me.liye.draw.controller;

import lombok.extern.slf4j.Slf4j;
import me.liye.draw.ShopifyConfig;
import me.liye.draw.ShopifyUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * Created by liye on 2025-09-16.
 */
@Slf4j
@RestController
@RequestMapping("/shopify/auth")
public class ShopifyAuthController {

    @GetMapping("/install")
    public void install(
            @RequestParam String shop,
            @RequestParam String hmac,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        // 校验 HMAC
        if (!ShopifyUtils.verifyHmac(request.getParameterMap(), hmac, ShopifyConfig.API_SECRET)) {
            response.sendError(401, "Invalid HMAC");
            return;
        }

        // 重定向到授权页面
        String redirectUrl = "https://" + shop + "/admin/oauth/authorize"
                + "?client_id=" + ShopifyConfig.API_KEY
                + "&scope=" + ShopifyConfig.SCOPES
                + "&redirect_uri=" + URLEncoder.encode(ShopifyConfig.REDIRECT_URI, "UTF-8")
                + "&state=" + UUID.randomUUID();
        log.info("Redirecting to: {}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(
            @RequestParam String shop,
            @RequestParam String code,
            @RequestParam String hmac,
            HttpServletRequest request
    ) throws Exception {
        // 校验 HMAC
        if (!ShopifyUtils.verifyHmac(request.getParameterMap(), hmac, ShopifyConfig.API_SECRET)) {
            return ResponseEntity.status(401).body("Invalid HMAC");
        }

        // 用 code 换取 access_token
        String tokenUrl = "https://" + shop + "/admin/oauth/access_token";

        String body = "{"
                + "\"client_id\":\"" + ShopifyConfig.API_KEY + "\","
                + "\"client_secret\":\"" + ShopifyConfig.API_SECRET + "\","
                + "\"code\":\"" + code + "\"}";

        org.apache.http.client.methods.HttpPost post = new org.apache.http.client.methods.HttpPost(tokenUrl);
        post.setEntity(new org.apache.http.entity.StringEntity(body, "UTF-8"));
        post.setHeader("Content-Type", "application/json");

        try (org.apache.http.impl.client.CloseableHttpClient client = org.apache.http.impl.client.HttpClients.createDefault();
             org.apache.http.client.methods.CloseableHttpResponse resp = client.execute(post)) {
            String respBody = new java.util.Scanner(resp.getEntity().getContent(), "UTF-8").useDelimiter("\\A").next();

            // TODO: 把 access_token 保存数据库（与 shop 关联）
            return ResponseEntity.ok("App installed! Token response: " + respBody);
        }
    }
}
