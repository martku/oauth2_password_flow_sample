package com.example;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableResourceServer
@RestController
@EnableCircuitBreaker
@EnableGlobalMethodSecurity(securedEnabled = true)
public class Application {

    @Autowired OAuth2RestTemplate template;
    
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Secured("ROLE_FOO_SERVICE_HELLO")
    @RequestMapping("/hello")
    public String home() {
        return this.calculateHello();
    }

    @HystrixCommand(fallbackMethod = "defaultHello")
    private String calculateHello() {
        ResponseEntity<String> response = this.template.getForEntity("http://localhost:8071/hello", String.class);
        return "Answer: " + response.getBody();
    }

    private String defaultHello() {
        return "No Answer: Hello World!";
    }

    @Bean
    public OAuth2RestTemplate restTemplate(UserInfoRestTemplateFactory factory) {
        return factory.getUserInfoRestTemplate();
    }

}
