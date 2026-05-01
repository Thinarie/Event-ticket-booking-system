package com.example.ticketbooking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController 
{

    @GetMapping("/")
    public Map<String, String> home() 
    {
        return Map.of
        (
                "status", "ok",
                "service", "ticketbooking-backend",
                "api", "/api/payments"
        );
    }
}
