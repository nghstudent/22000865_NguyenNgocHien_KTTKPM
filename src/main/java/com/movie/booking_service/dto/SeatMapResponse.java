package com.movie.booking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatMapResponse {
    // Tổng số ghế có trong phòng chiếu
    private int totalSeats;
    // Danh sách các ghế đã được đặt (ví dụ: A1, B3, C10)
    private List<String> occupiedSeats;
}
