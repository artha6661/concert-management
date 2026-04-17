package com.edts.concert_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.edts.concert_management.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

