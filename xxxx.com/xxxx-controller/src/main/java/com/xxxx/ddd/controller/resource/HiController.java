package com.xxxx.ddd.controller.resource;

import com.xxxx.ddd.application.service.event.EventAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HiController {
    @Autowired
    private EventAppService eventAppService;

    @GetMapping("/hi")
    public String hello() {
        return eventAppService.sayHi("Hi");
    }

    @GetMapping("/hi/v1")
    public String sayHi() {
        return eventAppService.sayHi("Ho");
    }
}
