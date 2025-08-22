package ru.orlov.adrift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Routes to handle SSR generated html
 */

@Controller
public class IndexController {

    @GetMapping(value = "/", produces = "text/html;charset=UTF-8")
    public String home() {
        return "/index.html";
    }

    @GetMapping(value = "/about", produces = "text/html;charset=UTF-8")
    public String about() {
        return "/about/index.html";
    }

    @GetMapping(value = "/auth/login", produces = "text/html")
    public String login() {
        return "/auth/login/index.html";
    }

    @GetMapping(value = "/auth/register", produces = "text/html")
    public String register() {
        return "/auth/register/index.html";
    }

    @GetMapping(value = "/a/{id}", produces = "text/html")
    public String ad(@PathVariable Long id) {
        return "/a/" + id + "/index.html";
    }

}
