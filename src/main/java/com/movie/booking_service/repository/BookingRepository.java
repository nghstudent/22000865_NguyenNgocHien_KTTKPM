package com.movie.booking_service.repository;

import com.movie.booking_service.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUsernameOrderByIdDesc(String username);
    List<Booking> findByMovieAndStatusIn(String movie, Collection<String> statuses);
    List<Booking> findByMovieAndShowDateAndShowTimeAndStatusIn(
            String movie,
            String showDate,
            String showTime,
            Collection<String> statuses
    );
}
