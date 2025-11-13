package com.kadri.servicea;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceAController {

    @GetMapping("/public")
    public String publicEndpoint(){
        return "service A :: public";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('microservice_user')")
    public String userEndpoint(){
        return "service A :: protected for microservice_user";
    }
}
