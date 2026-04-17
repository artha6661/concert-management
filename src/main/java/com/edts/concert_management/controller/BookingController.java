package com.edts.concert_management.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.edts.concert_management.dto.BookingResponse;
import com.edts.concert_management.dto.CreateBookingRequest;
import com.edts.concert_management.model.Booking;
import com.edts.concert_management.service.BookingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/concerts/{concertId}/bookings")
@Tag(name = "Bookings")
@Slf4j
public class BookingController {
  private final BookingService bookingService;
  //private final ObjectMapper objectMapper = new ObjectMapper();

  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Book tickets for a concert (concurrency-safe)")
  public BookingResponse create(
          @PathVariable("concertId") Long concertId,
          @Valid @RequestBody CreateBookingRequest req) throws JsonProcessingException {

      //log.info("Request: {}", objectMapper.writeValueAsString(req));

      Booking booking = bookingService.book(
              concertId,
              req.getUserId(),
              req.getQuantity());

      BookingResponse response = BookingResponse.from(booking);

      //log.info("Response: {}", objectMapper.writeValueAsString(response));

      return response;
  }
}

