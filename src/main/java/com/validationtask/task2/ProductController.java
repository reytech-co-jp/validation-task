package com.validationtask.task2;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ProductController {
    @PostMapping("/products")
    public Map<String, String> names(@RequestBody @Validated ProductRequest productRequest) {
        return Map.of("message", "Successfully created");
    }
}
