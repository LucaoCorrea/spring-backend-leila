package com.spring.leila.repository;

import com.spring.leila.model.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingModel, Long> {
    List<BookingModel> findByClientId(Long clientId);
    List<BookingModel> findByClientIdAndScheduledDateBetween(Long clientId, LocalDateTime start, LocalDateTime end);
}
