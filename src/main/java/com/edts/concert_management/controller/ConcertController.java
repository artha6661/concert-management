package com.edts.concert_management.controller;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.edts.concert_management.dto.ConcertResponse;
import com.edts.concert_management.model.Concert;
import com.edts.concert_management.service.ConcertService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/concerts")
@Tag(name = "Concerts")
@Slf4j
public class ConcertController {
  
  @Autowired
  ConcertService concertService;

  @GetMapping
  @Operation(summary = "Search available concerts")
  public List<ConcertResponse> search(
      @RequestParam(value = "q", required = false) String q,
      @RequestParam(value = "startsFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          Instant startsFrom,
      @RequestParam(value = "startsTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
          Instant startsTo) {
    List<Concert> concerts = concertService.search(q, startsFrom, startsTo);
    log.info("Found {} concerts for query: {}, startsFrom: {}, startsTo: {}", concerts.size(), q, startsFrom, startsTo);
    return concerts.stream().map(ConcertResponse::from).collect(Collectors.toList());
  }
}
