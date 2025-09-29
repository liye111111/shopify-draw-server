package me.liye.draw.core.controller;

import cn.hutool.core.net.NetUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

@RestController
public class HealthController {
    @Value("${APP_NAME:draw-server}")
    String appName;

    @GetMapping({"/health"})
    public Object health() {
        return Map.of(
                "status", "success",
                "APP_NAME", trimToEmpty(appName),
                "IP", trimToEmpty(NetUtil.getLocalhostStr()),
                "HOST", trimToEmpty(NetUtil.getLocalHostName()),
                "CID", trimToEmpty(System.getenv("COMMIT_ID"))

        );

    }

}
