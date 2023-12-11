package com.validationtask.task1;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {
    @PostMapping("/users")
    public Map<String, String> names(@RequestBody @Validated UserPostRequest userPostRequest) {
        return Map.of("message", "Successfully created");
    }

}
