package com.example.parkingbooking_service.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.parkingbooking_service.client.ParkingClient;
import com.example.parkingbooking_service.client.UserClient;
import com.example.parkingbooking_service.dto.ParkingBookingResponseDto;
import com.example.parkingbooking_service.dto.ParkingDto;
import com.example.parkingbooking_service.dto.UserDto;
import com.example.parkingbooking_service.repository.ParkingBookingRepository;
import com.example.parkingbooking_service.repository.entity.ParkingBookingEntity;

@Service
public class ParkingBookingService {
	
	@Autowired
    private ParkingBookingRepository parkingBookingRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ParkingClient parkingClient;
    
    public ParkingBookingEntity createParkingBooking(ParkingBookingEntity parkingBooking) {
        // Validate user
        UserDto user = userClient.getUserById(parkingBooking.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + parkingBooking.getUserId());
        }

        // Validate parking slot
        ParkingDto parking = parkingClient.getParkingById(parkingBooking.getParkingId());
        if (parking == null || !parking.isParkingAvailable()) {
            throw new RuntimeException("Parking slot not available with ID: " + parkingBooking.getParkingId());
        }

        // Save booking
        return parkingBookingRepository.save(parkingBooking);
    }
    
    public void deleteParkingBooking(int parkingBookingId) {
        if (!parkingBookingRepository.existsById(parkingBookingId)) {
            throw new RuntimeException("Booking not found with ID: " + parkingBookingId);
        }
        parkingBookingRepository.deleteById(parkingBookingId);
    }
    
    public ParkingBookingEntity modifyParkingBooking(int parkingBookingId, ParkingBookingEntity updatedBooking) {
        Optional<ParkingBookingEntity> existingBookingOpt = parkingBookingRepository.findById(parkingBookingId);
        if (existingBookingOpt.isEmpty()) {
            throw new RuntimeException("Booking not found with ID: " + parkingBookingId);
        }

        ParkingBookingEntity existingBooking = existingBookingOpt.get();
        existingBooking.setParkingId(updatedBooking.getParkingId());
        existingBooking.setUserId(updatedBooking.getUserId());
        existingBooking.setParkingBookingDate(updatedBooking.getParkingBookingDate());
        existingBooking.setStartTime(updatedBooking.getStartTime());
        existingBooking.setEndTime(updatedBooking.getEndTime());

        return parkingBookingRepository.save(existingBooking);
    }
    
    public ParkingBookingEntity viewParkingBooking(int bookingId) {
        return parkingBookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));
    }
    
    public List<ParkingBookingEntity> viewAllParkingBookings() {
        return parkingBookingRepository.findAll();
    }
    
    public ParkingBookingResponseDto getParkingBookingWithUserDetails(int bookingId) {
        // Fetch parking booking details
        Optional<ParkingBookingEntity> parkingBookingOpt = parkingBookingRepository.findById(bookingId);
        
        if (parkingBookingOpt.isPresent()) {
            ParkingBookingEntity parkingBooking = parkingBookingOpt.get();
 
            UserDto user = userClient.getUserById(parkingBooking.getUserId());

            ParkingDto parking = parkingClient.getParkingById(parkingBooking.getParkingId());

            ParkingBookingResponseDto response = new ParkingBookingResponseDto();
            response.setParkingBookingid(parkingBooking.getParkingBookingid());
            response.setParkingBookingDate(parkingBooking.getParkingBookingDate());
            response.setStartTime(parkingBooking.getStartTime());
            response.setEndTime(parkingBooking.getEndTime());
            
            // Explicitly set userId and parkingId
            response.setUserId(parkingBooking.getUserId());
            response.setParkingId(parkingBooking.getParkingId());
            
            // Set nested user and parking details
            response.setUser(user);
            response.setParking(parking);

            return response;
        } else {
            throw new RuntimeException("Parking Booking not found with ID: " + bookingId);
        }
    }

    
}
