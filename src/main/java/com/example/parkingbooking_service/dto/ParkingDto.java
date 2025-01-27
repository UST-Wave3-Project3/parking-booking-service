package com.example.parkingbooking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ParkingDto {
	
	private int parkingId;
    private String parkingNumber;
    private boolean parkingAvailable;
}
