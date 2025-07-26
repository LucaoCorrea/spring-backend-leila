package com.spring.leila.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.leila.model.ServiceModel;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceModel, Long> {

}
