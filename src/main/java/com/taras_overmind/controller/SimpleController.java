package com.taras_overmind.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/controller")
public class SimpleController {
    @GetMapping("/smthing")
    public String getSmthing() {
        return "ZHOPA";
    }
}
