package ru.test.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@FeignClient(name = "scheduleFeign", url = "http://localhost:8080/scheduleTemplate")
public interface ScheduleServiceFeignClient {
    @PostMapping("/")
    ResponseEntity<Map<String, Object>> createScheduleTemplate();

    @GetMapping("/{id}")
    ResponseEntity<Map<String, Object>> readScheduleTemplate(@PathVariable String id);
}
