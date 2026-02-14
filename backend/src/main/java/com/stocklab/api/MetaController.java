package com.stocklab.api;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meta")
public class MetaController {
    @Value("${app.mode:SECURE}")
    private String mode;

    @GetMapping("/mode")
    public Map<String, String> mode() {
        return Map.of("mode", mode);
    }
}
