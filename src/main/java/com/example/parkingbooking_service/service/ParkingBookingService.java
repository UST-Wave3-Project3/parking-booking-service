package com.example.parkingbooking_service.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

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
    private ParkingClient parkingClient;
    
    @Autowired
    private UserClient userClient;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    public ParkingBookingService() {
        scheduleParkingAvailabilityCheck();
    }

    /**
     * Schedules a task to check parking availability every 10 seconds.
     */
    private void scheduleParkingAvailabilityCheck() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAndToggleParkingAvailability();
            }
        }, 0, 10 * 1000); // Run every 10 seconds
    }

    /**
     * Checks all active parking bookings and toggles parking availability if the end time has passed.
     */
    private void checkAndToggleParkingAvailability() {
        List<ParkingBookingEntity> bookings = parkingBookingRepository.findAll();

        for (ParkingBookingEntity booking : bookings) {
            LocalDate bookingDate = booking.getParkingBookingDate();
            LocalTime endTime = booking.getEndTime();

            // Check if the current date and time exceed the booking's end time
            if (LocalDate.now().isAfter(bookingDate) || 
                (LocalDate.now().isEqual(bookingDate) && LocalTime.now().isAfter(endTime))) {

                // Fetch parking details using the client
                ParkingDto parking = parkingClient.getParkingById(booking.getParkingId());

                if (!parking.isParkingAvailable()) {
                    // Update parking availability to true
                    parking.setParkingAvailable(true);
                    parkingClient.updateParkingSlot(parking);

                    System.out.println("Parking slot " + parking.getParkingId() + " is now available.");
                }
            }
        }
    }

    /**
     * Converts time to AM/PM format.
     *
     * @param time LocalTime instance
     * @return Formatted time string
     */
    private String formatTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    // Other service methods (create, delete, modify, etc.)
    public ParkingBookingEntity createParkingBooking(ParkingBookingEntity parkingBooking) {
        // Validate and save logic
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
            
            response.setUserId(parkingBooking.getUserId());
            response.setParkingId(parkingBooking.getParkingId());
            parking.setParkingAvailable(false);
            parkingClient.updateParkingSlot(parking);
            response.setUser(user);
            response.setParking(parking);

            return response;
        } else {
            throw new RuntimeException("Parking Booking not found with ID: " + bookingId);
        }
    }
}
