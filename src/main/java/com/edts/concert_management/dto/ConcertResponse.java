package com.edts.concert_management.dto;

import java.time.Instant;

import com.edts.concert_management.model.Concert;

public class ConcertResponse {
    public Long id;
    public String name;
    public String venue;
    public Instant startsAt;
    public Instant bookingOpensAt;
    public Instant bookingClosesAt;
    public int totalTickets;
    public int remainingTickets;

    public static ConcertResponse from(Concert c) {
        ConcertResponse r = new ConcertResponse();
        r.id = c.getId();
        r.name = c.getName();
        r.venue = c.getVenue();
        r.startsAt = c.getStartsAt();
        r.bookingOpensAt = c.getBookingOpensAt();
        r.bookingClosesAt = c.getBookingClosesAt();
        r.totalTickets = c.getTotalTickets();
        r.remainingTickets = c.getRemainingTickets();
        return r;
    }
}
