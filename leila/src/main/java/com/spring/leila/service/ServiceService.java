package com.spring.leila.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.leila.repository.ServiceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public List<com.spring.leila.model.ServiceModel> getAllServices() {
        return serviceRepository.findAll();
    }
}