package com.example.parkingbooking_service.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ParkingDto {
	
	private int parkingId;
    private String parkingNumber;
	private String parkingBuilding;
	private String parkingFloor;
	private String parkingType;
    private boolean parkingAvailable;
}
