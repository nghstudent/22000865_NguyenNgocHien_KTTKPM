package com.movie.booking_service.controller;

import com.movie.booking_service.dto.SeatMapResponse;
import com.movie.booking_service.dto.SlotStatusResponse;
import com.movie.booking_service.model.Booking;
import com.movie.booking_service.producer.BookingEventProducer;
import com.movie.booking_service.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private static final Pattern SEAT_PATTERN = Pattern.compile("^[A-C](10|[1-9])$");

    @Autowired
    private BookingService service;

    @Autowired
    private BookingEventProducer producer;

    @PostMapping
    public Booking create(@RequestBody Booking booking) {
        if (booking.getUsername() == null || booking.getUsername().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
        }

        if (booking.getMovie() == null || booking.getMovie().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie is required");
        }

        if (booking.getShowDate() == null || booking.getShowDate().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Show date is required");
        }

        if (booking.getShowTime() == null || booking.getShowTime().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Show time is required");
        }

        if (booking.getSeats() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seats must be greater than 0");
        }

        LocalDate showDate = parseShowDate(booking.getShowDate());
        if (showDate.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Show date cannot be in the past");
        }

        if (!BookingService.SHOW_TIMES.contains(booking.getShowTime())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid show time");
        }

        List<String> requestedSeats = service.parseSeatNumbers(booking.getSeatNumbers());
        if (requestedSeats.size() != booking.getSeats()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selected seats count must match seats number"
            );
        }

        Set<String> uniqueSeats = new LinkedHashSet<>();
        for (String seat : requestedSeats) {
            if (!SEAT_PATTERN.matcher(seat).matches()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid seat code: " + seat);
            }
            uniqueSeats.add(seat);
        }

        if (uniqueSeats.size() != requestedSeats.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate seat selection is not allowed");
        }

        List<String> occupiedSeats = service.getOccupiedSeats(
                booking.getMovie(),
                booking.getShowDate(),
                booking.getShowTime()
        );
        Set<String> occupiedSet = new HashSet<>(occupiedSeats);

        List<String> conflictedSeats = new ArrayList<>();
        for (String seat : uniqueSeats) {
            if (occupiedSet.contains(seat)) {
                conflictedSeats.add(seat);
            }
        }

        if (!conflictedSeats.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Seats already booked: " + String.join(", ", conflictedSeats)
            );
        }

        if (booking.getPaymentMethod() == null || booking.getPaymentMethod().isBlank()) {
            booking.setPaymentMethod("CASH");
        }

        booking.setShowDate(showDate.toString());
        booking.setSeatNumbers(String.join(",", uniqueSeats));
        Booking saved = service.create(booking);

        producer.sendBooking(saved);

        return saved;
    }

    @GetMapping
    public List<Booking> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Booking getById(@PathVariable Long id) {
        return service.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
    }

    @GetMapping("/user/{username}")
    public List<Booking> getByUsername(@PathVariable String username) {
        return service.getByUsername(username);
    }

    /**
     * API lấy sơ đồ ghế (seat map) của một suất chiếu
     * - Tổng số ghế
     * - Danh sách ghế đã được đặt
     */
    @GetMapping("/seat-map")
    public SeatMapResponse getSeatMap(
            @RequestParam String movie,
            @RequestParam String showDate,
            @RequestParam String showTime
    ) {
        if (movie == null || movie.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie is required");
        }
        parseShowDate(showDate);
        if (!BookingService.SHOW_TIMES.contains(showTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid show time");
        }

        return new SeatMapResponse(
                BookingService.TOTAL_SEATS,
                service.getOccupiedSeats(movie, showDate, showTime)
        );
    }

    /**
     * API lấy trạng thái các suất chiếu trong 7 ngày tới
     * - Số ghế đã đặt
     * - Trạng thái đầy / còn chỗ
     */
    @GetMapping("/slot-status")
    public List<SlotStatusResponse> getSlotStatus(@RequestParam String movie) {
        if (movie == null || movie.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie is required");
        }

        Map<String, Integer> occupiedBySlot = service.getSlotOccupiedCounts(movie);
        List<SlotStatusResponse> result = new ArrayList<>();
        LocalDate start = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            String showDate = start.plusDays(i).toString();
            for (String showTime : BookingService.SHOW_TIMES) {
                String key = showDate + "|" + showTime;
                int occupied = occupiedBySlot.getOrDefault(key, 0);
                result.add(new SlotStatusResponse(
                        showDate,
                        showTime,
                        occupied,
                        occupied >= BookingService.TOTAL_SEATS
                ));
            }
        }

        return result;
    }
    /**
     * Parse và validate định dạng ngày chiếu (YYYY-MM-DD)
     */
    private LocalDate parseShowDate(String showDate) {
        try {
            return LocalDate.parse(showDate);
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid show date format");
        }
    }
}
