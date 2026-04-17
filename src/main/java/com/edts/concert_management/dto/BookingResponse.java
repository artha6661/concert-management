package com.edts.concert_management.dto;

import java.time.Instant;

import com.edts.concert_management.enums.BookingStatus;
import com.edts.concert_management.model.Booking;

public class BookingResponse {
  public Long id;
  public Long concertId;
  public String userId;
  public int quantity;
  public BookingStatus status;
  public Instant createdAt;

  public static BookingResponse from(Booking b) {
    BookingResponse r = new BookingResponse();
    r.id = b.getId();
    r.concertId = b.getConcertId();
    r.userId = b.getUserId();
    r.quantity = b.getQuantity();
    r.status = b.getStatus();
    r.createdAt = b.getCreatedAt();
    return r;
  }
}

