package com.example.parkingbooking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ParkingbookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParkingbookingServiceApplication.class, args);
	}

}
