package com.movie.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlotStatusResponse {
    // Ngày chiếu (format: yyyy-MM-dd)
    private String showDate;
    // Giờ chiếu (ví dụ: 09:00, 13:00...)
    private String showTime;
    // Số lượng ghế đã được đặt
    private int occupiedSeats;
    // true nếu suất chiếu đã đầy ghế, false nếu còn chỗ
    private boolean full;
}
