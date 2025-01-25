package com.example.parkingbooking_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.parkingbooking_service.dto.ParkingBookingResponseDto;
import com.example.parkingbooking_service.repository.entity.ParkingBookingEntity;
import com.example.parkingbooking_service.service.ParkingBookingService;

@RestController
@RequestMapping("/api/parking-bookings")
public class ParkingBookingController {
	
	@Autowired
    private ParkingBookingService parkingBookingService;

    // Create Parking Booking
    @PostMapping
    public ResponseEntity<ParkingBookingEntity> createParkingBooking(@RequestBody ParkingBookingEntity parkingBooking) {
        ParkingBookingEntity createdBooking = parkingBookingService.createParkingBooking(parkingBooking);
        return ResponseEntity.ok(createdBooking);
    }

    // Delete Parking Booking
    @DeleteMapping("/{parkingBookingId}")
    public ResponseEntity<Void> deleteParkingBooking(@PathVariable int parkingBookingId) {
        parkingBookingService.deleteParkingBooking(parkingBookingId);
        return ResponseEntity.noContent().build();
    }

    // Modify Parking Booking
    @PutMapping("/{parkingBookingId}")
    public ResponseEntity<ParkingBookingEntity> modifyParkingBooking(
            @PathVariable int parkingBookingId,
            @RequestBody ParkingBookingEntity updatedBooking) {
        ParkingBookingEntity modifiedBooking = parkingBookingService.modifyParkingBooking(parkingBookingId, updatedBooking);
        return ResponseEntity.ok(modifiedBooking);
    }

    // View a Parking Booking
    @GetMapping("/booking/{parkingBookingId}")
    public ResponseEntity<ParkingBookingEntity> viewParkingBooking(@PathVariable int parkingBookingId) {
        ParkingBookingEntity booking = parkingBookingService.viewParkingBooking(parkingBookingId);
        return ResponseEntity.ok(booking);
    }

    // View All Parking Bookings
    @GetMapping
    public ResponseEntity<List<ParkingBookingEntity>> viewAllParkingBookings() {
        List<ParkingBookingEntity> bookings = parkingBookingService.viewAllParkingBookings();
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/{parkingBookingId}")
    public ParkingBookingResponseDto getParkingBookingWithUserDetails(@PathVariable int parkingBookingId) {
        return parkingBookingService.getParkingBookingWithUserDetails(parkingBookingId);
    }
}
