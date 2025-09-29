package me.liye.draw.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liye on 2025-09-17.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "shopify")
public class ShopifyProperties {
    String apiKey;
    String apiSecret;
    String scopes;
    String redirectUri;
}