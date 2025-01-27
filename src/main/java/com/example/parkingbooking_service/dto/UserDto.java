package com.example.parkingbooking_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {
	
	private Long userId;
    private String userFirstName;
    private String userEmail;

}
