package com.edts.concert_management.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.edts.concert_management.model.Concert;
import com.edts.concert_management.repository.ConcertRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ConcertService {
  private final ConcertRepository concertRepository;

  public ConcertService(ConcertRepository concertRepository) {
    this.concertRepository = concertRepository;
  }

  public List<Concert> search(String q, Instant startsFrom, Instant startsTo) {
    log.info("Searching concerts with query: {}, startsFrom: {}, startsTo: {}", q, startsFrom, startsTo);
    return concertRepository.search(q, startsFrom, startsTo);
  }

  @Transactional
  public Concert create(
      String name,
      String venue,
      Instant startsAt,
      Instant bookingOpensAt,
      Instant bookingClosesAt,
      int totalTickets) {
    Concert concert = Concert.create(name, venue, startsAt, bookingOpensAt, bookingClosesAt, totalTickets);
    return concertRepository.save(concert);
  }
}
