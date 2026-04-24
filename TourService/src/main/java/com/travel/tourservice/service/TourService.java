package com.travel.tourservice.service;

import com.travel.tourservice.entity.Tour;
import com.travel.tourservice.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TourService {
    @Autowired
    private TourRepository tourRepository;

    public List<Tour> findAll() {
        return tourRepository.findAll();
    }

    public Tour findById(Integer id) {
        return tourRepository.findById(id).orElse(null);
    }

    public Tour save(Tour tour) {
        return tourRepository.save(tour);
    }

    public void deleteById(Integer id) {
        tourRepository.deleteById(id);
    }
}