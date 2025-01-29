package com.example.parkingbooking_service.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    // Scheduler runs every minute to update parking availability
    @Scheduled(fixedRate = 10000) // Runs every 1 minute
    public void checkAndToggleParkingAvailability() {
        System.out.println("Checking expired bookings at: " + LocalTime.now());

        List<ParkingBookingEntity> bookings = parkingBookingRepository.findAll();

        // Iterate through all bookings
        for (ParkingBookingEntity booking : bookings) {
            LocalDate bookingDate = booking.getParkingBookingDate();
            LocalTime startTime = booking.getStartTime();
            LocalTime endTime = booking.getEndTime();
            ParkingDto parking = parkingClient.getParkingById(booking.getParkingId());

            System.out.println("Checking Booking ID: " + booking.getParkingBookingid() + 
                               " | Date: " + bookingDate + 
                               " | Start Time: " + startTime + 
                               " | End Time: " + endTime);

            // 1️⃣ Check if the current time is within any booking period for this parking slot
            boolean isSlotBooked = false;
            for (ParkingBookingEntity activeBooking : bookings) {
                if (activeBooking.getParkingId() == booking.getParkingId() &&
                    LocalDate.now().isEqual(activeBooking.getParkingBookingDate()) &&
                    (LocalTime.now().isAfter(activeBooking.getStartTime()) && LocalTime.now().isBefore(activeBooking.getEndTime()))) {
                    isSlotBooked = true;
                    break;
                }
            }

            // If any booking is active, the parking slot should remain unavailable (false)
            if (isSlotBooked && parking.isParkingAvailable()) {
                parking.setParkingAvailable(false);
                parkingClient.updateParkingSlot(parking);
                System.out.println("Parking slot " + parking.getParkingId() + " is now booked (false).");
            }

            // 2️⃣ If the current time is past the end time of all bookings, make the parking slot available (true)
            if (!isSlotBooked && !parking.isParkingAvailable()) {
                parking.setParkingAvailable(true);
                parkingClient.updateParkingSlot(parking);
                System.out.println("Parking slot " + parking.getParkingId() + " is now available (true).");
            }
        }
    }


    public ParkingBookingEntity createParkingBooking(ParkingBookingEntity parkingBooking) {
        // Save the booking first
        ParkingBookingEntity createdBooking = parkingBookingRepository.save(parkingBooking);

        // Fetch the parking slot using parkingId
        ParkingDto parking = parkingClient.getParkingById(parkingBooking.getParkingId());

        // Check if the booking's start time is in the future
        if (LocalTime.now().isBefore(parkingBooking.getStartTime())) {
            // If the start time is in the future, do nothing (the parking slot remains available)
            System.out.println("Parking slot " + parking.getParkingId() + " is available until " + parkingBooking.getStartTime());
        } else {
            // If the booking is immediate (i.e., the start time is now or in the past), mark the slot as unavailable
            if (parking.isParkingAvailable()) {
                parking.setParkingAvailable(false);
                parkingClient.updateParkingSlot(parking);
                System.out.println("Parking slot " + parking.getParkingId() + " is now booked (false).");
            }
        }
        return createdBooking;
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

            // Fetch user details
            UserDto user = userClient.getUserById(parkingBooking.getUserId());

            // Fetch parking details
            ParkingDto parking = parkingClient.getParkingById(parkingBooking.getParkingId());

            // Prepare the response DTO
            ParkingBookingResponseDto response = new ParkingBookingResponseDto();
            response.setParkingBookingid(parkingBooking.getParkingBookingid());
            response.setParkingBookingDate(parkingBooking.getParkingBookingDate());
            response.setStartTime(parkingBooking.getStartTime());
            response.setEndTime(parkingBooking.getEndTime());
            response.setUserId(parkingBooking.getUserId());
            response.setParkingId(parkingBooking.getParkingId());

            // Set the user and parking details in the response
            response.setUser(user);
            response.setParking(parking);

            return response;
        } else {
            throw new RuntimeException("Parking Booking not found with ID: " + bookingId);
        }
    }
}
