package com.example.parkingbooking_service.repository.entity;

import java.time.LocalDate;
import java.time.LocalTime;

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

@Entity
@Table(name="parkingbooking_service")
public class ParkingBookingEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="p_booking_id")
	private int parkingBookingid;
	
	@Column(name="user_id")
	private int userId;
	
	@Column(name="parking_id")
	private int parkingId;
	
	@Column(name="p_booking_date")
	private LocalDate parkingBookingDate;
	
	@Column(name="p_start_time")
	private LocalTime startTime;
	
	@Column(name="p_end_time")
	private LocalTime endTime;

}
