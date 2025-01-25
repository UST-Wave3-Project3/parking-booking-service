package com.example.parkingbooking_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.parkingbooking_service.dto.ParkingDto;

@FeignClient(name = "parking-service", url = "http://localhost:3333/api/parkings")
public interface ParkingClient {
	
	@GetMapping("{parkingId}")
    ParkingDto getParkingById(@PathVariable int parkingId);
	
}
