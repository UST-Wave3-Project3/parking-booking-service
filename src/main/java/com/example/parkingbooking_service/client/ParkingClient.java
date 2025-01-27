package com.example.parkingbooking_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.parkingbooking_service.dto.ParkingDto;
import com.example.parkingbooking_service.dto.ParkingEntity;

@FeignClient(name = "parking-service", url = "http://localhost:3333/api/parkings")
public interface ParkingClient {
	
	@GetMapping("{parkingId}")
    ParkingDto getParkingById(@PathVariable int parkingId);
	
	@PutMapping
	ParkingDto updateParkingSlot(@RequestBody ParkingDto parking);
	
	
}
