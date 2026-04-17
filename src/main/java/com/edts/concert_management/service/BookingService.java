package com.edts.concert_management.service;



import java.time.Clock;
import java.time.Instant;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edts.concert_management.enums.BookingStatus;
import com.edts.concert_management.model.Booking;
import com.edts.concert_management.model.Concert;
import com.edts.concert_management.repository.BookingRepository;
import com.edts.concert_management.repository.ConcertRepository;

@Service
public class BookingService {
  private final ConcertRepository concertRepository;
  private final BookingRepository bookingRepository;
  private final Clock clock;

  public BookingService(ConcertRepository concertRepository, BookingRepository bookingRepository, Clock clock) {
    this.concertRepository = concertRepository;
    this.bookingRepository = bookingRepository;
    this.clock = clock;
  }

  @Transactional
  public Booking book(Long concertId, String userId, int quantity) {
    Concert concert =
        concertRepository.findById(concertId).orElseThrow(() -> new EntityNotFoundException("Concert not found"));

    Instant now = Instant.now(clock);
    if (now.isBefore(concert.getBookingOpensAt()) || !now.isBefore(concert.getBookingClosesAt())) {
      return bookingRepository.save(new Booking(concert, userId, quantity, BookingStatus.REJECTED, now));
    }

    try {
      int updated = concertRepository.tryDecrementRemaining(concertId, quantity);
      if (updated != 1) {
        return bookingRepository.save(new Booking(concert, userId, quantity, BookingStatus.REJECTED, now));
      }
      return bookingRepository.save(new Booking(concert, userId, quantity, BookingStatus.CONFIRMED, now));
    } catch (DataAccessException e) {
      return bookingRepository.save(new Booking(concert, userId, quantity, BookingStatus.REJECTED, now));
    }
  }
}

