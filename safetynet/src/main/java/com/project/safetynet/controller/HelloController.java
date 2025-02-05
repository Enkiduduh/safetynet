package com.project.safetynet.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // Toutes les routes commenceront par /api
public class HelloController {

    @GetMapping("/hello") // Accessible via http://localhost:8080/api/hello
    public String hello() {
        return "Hello, World!";
    }
}
