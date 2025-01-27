package com.example.parkingbooking_service.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.parkingbooking_service.repository.entity.ParkingBookingEntity;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingBookingResponseDto {
	
private int parkingBookingid;
	
	private int userId;
	
	
	private int parkingId;
	
	private LocalDate parkingBookingDate;

	private LocalTime startTime;

	private LocalTime endTime;
	
    private UserDto user;
    private ParkingDto parking;

}
