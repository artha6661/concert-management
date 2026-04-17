package com.edts.concert_management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.edts.concert_management.dto.ConcertResponse;
import com.edts.concert_management.dto.CreateConcertRequest;
import com.edts.concert_management.model.Concert;
import com.edts.concert_management.service.ConcertService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/admin/concerts")
@Tag(name = "Admin")
@Slf4j
public class AdminConcertController {
  private final ConcertService concertService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public AdminConcertController(ConcertService concertService) {
    this.concertService = concertService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Create a concert (admin helper)")
  public ConcertResponse create(@Valid @RequestBody CreateConcertRequest req) throws JsonProcessingException {
    log.info("Creating concert with name: {}, venue: {}, startsAt: {}, bookingOpensAt: {}, bookingClosesAt: {}, totalTickets: {}",
        req.getName(), req.getVenue(), req.getStartsAt(), req.getBookingOpensAt(), req.getBookingClosesAt(), req.getTotalTickets());
    Concert c =
        concertService.create(
            req.getName(),
            req.getVenue(),
            req.getStartsAt(),
            req.getBookingOpensAt(),
            req.getBookingClosesAt(),
            req.getTotalTickets());
    ConcertResponse conResponse = ConcertResponse.from(c);
    log.info("Response: {}", objectMapper.writeValueAsString(conResponse));
    return conResponse;
  }
}