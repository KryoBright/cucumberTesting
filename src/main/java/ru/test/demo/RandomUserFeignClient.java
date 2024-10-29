package ru.test.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "randomUserFeign", url = "${rest.user.url}")
public interface RandomUserFeignClient {

    @GetMapping
    ResponseEntity<Map<String, Object>> getUsers();

    @PostMapping(path = "/subEndpoint")
    ResponseEntity<Map<String, Object>> postUsers(@RequestBody String body);

}
