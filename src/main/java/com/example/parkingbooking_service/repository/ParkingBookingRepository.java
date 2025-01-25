package com.example.parkingbooking_service.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.parkingbooking_service.repository.entity.ParkingBookingEntity;

@Repository
public interface ParkingBookingRepository extends JpaRepository<ParkingBookingEntity, Integer> {
 
}
