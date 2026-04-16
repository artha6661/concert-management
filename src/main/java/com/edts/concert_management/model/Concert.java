package com.edts.concert_management.model;


import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "concerts")
@Getter
@Setter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Concert {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 200)
  private String name;

  @Column(nullable = false, length = 200)
  private String venue;

  @Column(name = "starts_at", nullable = false)
  private Instant startsAt;

  @Column(name = "booking_opens_at", nullable = false)
  private Instant bookingOpensAt;

  @Column(name = "booking_closes_at", nullable = false)
  private Instant bookingClosesAt;

  @Column(name = "total_tickets", nullable = false)
  private int totalTickets;

  @Column(name = "remaining_tickets", nullable = false)
  private int remainingTickets;

  @Version
  @Column(nullable = false)
  private long version;

  public static Concert create(
      String name,
      String venue,
      Instant startsAt,
      Instant bookingOpensAt,
      Instant bookingClosesAt,
      int totalTickets) {
    Concert c = new Concert();
    c.name = name;
    c.venue = venue;
    c.startsAt = startsAt;
    c.bookingOpensAt = bookingOpensAt;
    c.bookingClosesAt = bookingClosesAt;
    c.totalTickets = totalTickets;
    c.remainingTickets = totalTickets;
    return c;
  }

}
