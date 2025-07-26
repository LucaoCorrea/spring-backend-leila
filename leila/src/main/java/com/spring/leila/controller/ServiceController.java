package com.spring.leila.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.leila.model.ServiceModel;
import com.spring.leila.repository.ServiceRepository;

import lombok.*;

@RestController
@RequestMapping("/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceRepository serviceRepository;

    @PostMapping
    public ResponseEntity<ServiceModel> create(@RequestBody ServiceModel service) {
        return ResponseEntity.ok(serviceRepository.save(service));
    }

    @GetMapping
    public ResponseEntity<List<ServiceModel>> getByBooking() {
        return ResponseEntity.ok(serviceRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
