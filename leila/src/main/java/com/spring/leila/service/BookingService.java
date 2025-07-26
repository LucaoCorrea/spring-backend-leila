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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;

    public BookingModel createBooking(BookingModel booking) {
        if (booking.getClient() == null || booking.getClient().getId() == null) {
            throw new RuntimeException("Client ID is required");
        }
        UserModel client = userRepository.findById(booking.getClient().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        booking.setClient(client);

        if (booking.getServices() != null && !booking.getServices().isEmpty()) {
            List<Long> serviceIds = booking.getServices().stream()
                    .map(ServiceModel::getId)
                    .toList();

            List<ServiceModel> services = serviceRepository.findAllById(serviceIds);
            booking.setServices(services);
        } else {
            booking.setServices(List.of());
        }

        booking.setStatus(BookingStatus.REQUESTED);

        return bookingRepository.save(booking);
    }

    public List<BookingModel> getAllBookings() {
        return bookingRepository.findAll();
    }

    public BookingModel updateBooking(Long id, BookingModel updatedBooking) {
        BookingModel existing = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        long daysUntil = ChronoUnit.DAYS.between(LocalDateTime.now(), existing.getScheduledDate());
        if (daysUntil < 2) {
            throw new IllegalArgumentException("Changes are only allowed at least 2 days in advance. Otherwise, contact by phone.");
        }

        existing.setScheduledDate(updatedBooking.getScheduledDate());
        existing.setNotes(updatedBooking.getNotes());

        if (updatedBooking.getServices() != null && !updatedBooking.getServices().isEmpty()) {
            List<Long> serviceIds = updatedBooking.getServices().stream()
                    .map(ServiceModel::getId)
                    .toList();

            List<ServiceModel> services = serviceRepository.findAllById(serviceIds);
            existing.setServices(services);
        } else {
            existing.setServices(List.of());
        }

        return bookingRepository.save(existing);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    public List<BookingModel> getClientBookings(Long clientId) {
        return bookingRepository.findByClientId(clientId);
    }
}
