package com.hazelgym.controller;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, Object> home() {
        return Map.of(
                "app", "Hazel Gym API",
                "status", "running",
                "docs", "/swagger-ui.html",
                "openapi", "/api-docs",
                "timestamp", OffsetDateTime.now().toString()
        );
    }
}
