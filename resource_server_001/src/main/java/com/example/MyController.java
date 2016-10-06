package com.example;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author straubec
 */
@RestController
public class MyController {

    private final MyServiceClient service;

    public MyController(MyServiceClient service) {
        this.service = service;
    }

    @Secured("ROLE_RESOURCE_001_HELLO")
    @RequestMapping("/hello")
    public String sayHello() {
        return this.service.sayHello();
    }

}
