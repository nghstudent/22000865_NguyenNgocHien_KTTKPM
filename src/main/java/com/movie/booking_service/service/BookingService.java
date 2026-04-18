package com.movie.booking_service.service;

import com.movie.booking_service.model.Booking;
import com.movie.booking_service.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookingService {

    public static final int TOTAL_SEATS = 30;
    public static final List<String> SHOW_TIMES = List.of("09:00", "13:00", "18:00", "21:00");
    private static final Set<String> ACTIVE_STATUSES = Set.of("PENDING", "SUCCESS");

    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking create(Booking booking) {
        booking.setStatus("PENDING");
        if (booking.getPaymentMethod() == null || booking.getPaymentMethod().isBlank()) {
            booking.setPaymentMethod("CASH");
        }
        return bookingRepository.save(booking);
    }

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getByUsername(String username) {
        return bookingRepository.findByUsernameOrderByIdDesc(username);
    }

    public Optional<Booking> updateStatus(Long id, String status) {
        return bookingRepository.findById(id).map(booking -> {
            booking.setStatus(status);
            return bookingRepository.save(booking);
        });
    }

    /**
     * Lấy danh sách ghế đã được đặt cho:
     * - 1 bộ phim
     * - 1 ngày chiếu
     * - 1 suất chiếu
     * Chỉ tính các booking có status còn hiệu lực (PENDING, SUCCESS)
     */
    public List<String> getOccupiedSeats(String movie, String showDate, String showTime) {
        List<Booking> activeBookings = bookingRepository.findByMovieAndShowDateAndShowTimeAndStatusIn(
                movie,
                showDate,
                showTime,
                ACTIVE_STATUSES
        );

        Set<String> occupied = new TreeSet<>();
        for (Booking booking : activeBookings) {
            occupied.addAll(parseSeatNumbers(booking.getSeatNumbers()));
        }

        return new ArrayList<>(occupied);
    }


     // Đếm số lượng ghế đã được đặt theo từng suất chiếu của một bộ phim
    public Map<String, Integer> getSlotOccupiedCounts(String movie) {
        List<Booking> activeBookings = bookingRepository.findByMovieAndStatusIn(movie, ACTIVE_STATUSES);
        Map<String, Set<String>> slotSeats = new HashMap<>();

        for (Booking booking : activeBookings) {
            if (booking.getShowDate() == null || booking.getShowTime() == null) {
                continue;
            }
            String slotKey = booking.getShowDate() + "|" + booking.getShowTime();
            slotSeats.computeIfAbsent(slotKey, k -> new HashSet<>()).addAll(parseSeatNumbers(booking.getSeatNumbers()));
        }

        Map<String, Integer> result = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : slotSeats.entrySet()) {
            result.put(entry.getKey(), entry.getValue().size());
        }
        return result;
    }

    /**
     * Parse chuỗi seatNumbers (vd: "A1, A2, b3")
     * -> List chuẩn hóa: ["A1", "A2", "B3"]
     *
     * - Loại bỏ khoảng trắng
     * - Convert uppercase
     * - Bỏ phần tử rỗng
     */
    public List<String> parseSeatNumbers(String seatNumbers) {
        if (seatNumbers == null || seatNumbers.isBlank()) {
            return List.of();
        }

        List<String> parsed = new ArrayList<>();
        for (String rawSeat : seatNumbers.split(",")) {
            String seat = rawSeat.trim().toUpperCase(Locale.ROOT);
            if (!seat.isBlank()) {
                parsed.add(seat);
            }
        }
        return parsed;
    }
}
