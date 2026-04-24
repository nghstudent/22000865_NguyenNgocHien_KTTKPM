package com.travel.tourservice.controller;

import com.travel.tourservice.entity.Tour;
import com.travel.tourservice.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@RestController
@RequestMapping("/tours")
@CrossOrigin(origins = "*") //Kết nối với Frontend
public class TourController {

    @Autowired
    private TourService tourService;

    // Lấy danh sách tất cả Tour
    @GetMapping
    public List<Tour> getAll() {
        return tourService.findAll();
    }

    // Lấy chi tiết 1 Tour theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Tour> getById(@PathVariable Integer id) {
        Tour tour = tourService.findById(id);
        return tour != null ? ResponseEntity.ok(tour) : ResponseEntity.notFound().build();
    }

    // Thêm mới 1 Tour
    @PostMapping
    public Tour create(@RequestBody Tour tour) {
        return tourService.save(tour);
    }

    // Cập nhật thông tin Tour
    @PutMapping("/{id}")
    public ResponseEntity<Tour> update(@PathVariable Integer id, @RequestBody Tour tourDetails) {
        Tour tour = tourService.findById(id);
        if (tour == null) return ResponseEntity.notFound().build();

        tour.setName(tourDetails.getName());
        tour.setPrice(tourDetails.getPrice());
        tour.setDescription(tourDetails.getDescription());

        return ResponseEntity.ok(tourService.save(tour));
    }

    // Xóa 1 Tour
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (tourService.findById(id) == null) return ResponseEntity.notFound().build();
        tourService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}