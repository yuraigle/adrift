package ru.orlov.adrift.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping(value = "/api/version", produces = "application/json")
    public Map<String, String> version() {
        return Map.of("version", "0.0.1");
    }
}
