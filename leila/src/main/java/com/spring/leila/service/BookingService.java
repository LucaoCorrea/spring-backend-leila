package com.spring.leila.service;

import com.spring.leila.enums.BookingStatus;
import com.spring.leila.model.BookingModel;
import com.spring.leila.model.ServiceModel;
import com.spring.leila.model.UserModel;
import com.spring.leila.repository.BookingRepository;
import com.spring.leila.repository.ServiceRepository;
import com.spring.leila.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    public BookingModel createBooking(BookingModel booking) {
        UserModel client = userRepository.findById(booking.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        List<ServiceModel> services = serviceRepository.findAllById(
                booking.getServices().stream().map(ServiceModel::getId).toList());

        booking.setClient(client);
        booking.setServices(services);
        booking.setStatus(BookingStatus.REQUESTED);
        booking.setTotalAmount(services.stream().mapToDouble(ServiceModel::getPrice).sum());

        return bookingRepository.save(booking);
    }

    public List<BookingModel> getAllBookings(LocalDateTime startDate, LocalDateTime endDate, String status) {
        if (startDate != null && endDate != null && status != null) {
            return bookingRepository.findByScheduledDateBetweenAndStatus(startDate, endDate, status);
        }
        if (startDate != null && endDate != null) {
            return bookingRepository.findByScheduledDateBetween(startDate, endDate);
        }
        if (status != null) {
            return bookingRepository.findByStatus(status);
        }
        return bookingRepository.findAll();
    }

    public BookingModel getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public BookingModel updateBooking(Long id, BookingModel bookingDetails) {
        BookingModel booking = getBookingById(id);

        if (bookingDetails.getScheduledDate() != null) {
            booking.setScheduledDate(bookingDetails.getScheduledDate());
        }
        if (bookingDetails.getNotes() != null) {
            booking.setNotes(bookingDetails.getNotes());
        }
        if (bookingDetails.getStatus() != null) {
            booking.setStatus(bookingDetails.getStatus());
        }
        if (bookingDetails.getServices() != null && !bookingDetails.getServices().isEmpty()) {
            List<ServiceModel> services = serviceRepository.findAllById(
                    bookingDetails.getServices().stream().map(ServiceModel::getId).toList());
            booking.setServices(services);
            booking.setTotalAmount(services.stream().mapToDouble(ServiceModel::getPrice).sum());
        }

        return bookingRepository.save(booking);
    }

    public BookingModel updateBookingStatus(Long id, BookingStatus status) {
        BookingModel booking = getBookingById(id);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<BookingModel> getClientBookings(Long clientId, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return bookingRepository.findByClientIdAndScheduledDateBetween(clientId, startDate, endDate);
        }
        return bookingRepository.findByClientId(clientId);
    }

    public BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                .filter(b -> (startDate == null || !b.getScheduledDate().isBefore(startDate)))
                .filter(b -> (endDate == null || !b.getScheduledDate().isAfter(endDate)))
                .map(b -> BigDecimal.valueOf(b.getTotalAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}