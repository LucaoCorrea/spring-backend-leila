package com.spring.leila.repository;

import com.spring.leila.model.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingModel, Long> {
    List<BookingModel> findByClientId(Long clientId);
    List<BookingModel> findByClientIdAndScheduledDateBetween(Long clientId, LocalDateTime startDate, LocalDateTime endDate);
    List<BookingModel> findByScheduledDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<BookingModel> findByStatus(String status);
    List<BookingModel> findByScheduledDateBetweenAndStatus(LocalDateTime startDate, LocalDateTime endDate, String status);
}