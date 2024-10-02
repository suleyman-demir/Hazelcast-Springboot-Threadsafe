package com.shop.products.hazelcast.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/hazelcast")
public class HazelcastController {

    private final com.shop.products.hazelcast.HazelcastService hazelcastService;

    public HazelcastController(com.shop.products.hazelcast.HazelcastService hazelcastService) {
        this.hazelcastService = hazelcastService;
    }

    @GetMapping("/execute")
    public String executeRandomNumberTask() {
        hazelcastService.executeRandomNumberTask();
        return "Rastgele sayı üretim işlemi tamamlandı!";
    }
}
