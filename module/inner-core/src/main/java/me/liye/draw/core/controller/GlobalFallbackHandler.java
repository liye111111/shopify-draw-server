package me.liye.draw.core.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * Created by liye on 2025-09-17.
 */
@Slf4j
@ControllerAdvice
public class GlobalFallbackHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFound(HttpServletRequest request,
                                                 NoHandlerFoundException ex) throws IOException {
        // 1) 基础信息
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String query = request.getQueryString();
        String remote = request.getRemoteAddr();

        // 2) Headers
        StringBuilder headersSb = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headersSb.append(name).append(": ").append(request.getHeader(name)).append("\n");
        }

        // 3) Body
        String body = "";
        try {
            body = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            // body 可能被提前消费，忽略即可
        }

        log.info("=== Fallback Request ===\nmethod: {}\nuri: {}\nquery: {}\nremote: {}\nheaders:\n{}\nbody:\n{}\n========================",
                method, uri, query, remote, headersSb.toString(), body);

        // 4) 返回 200
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("{\"status\":\"ok\"}");
    }
}