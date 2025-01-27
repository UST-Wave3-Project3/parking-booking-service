package com.example.parkingbooking_service.dto;





import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ParkingEntity {
	
	private int parkingId;
	
	private String parkingNumber;
	private String parkingBuilding;
	
	private String parkingFloor;
	
	private String parkingType;
	
	private boolean parkingAvailable;

}
