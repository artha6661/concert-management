package com.edts.concert_management.model;

import java.time.Instant;

import com.edts.concert_management.enums.BookingStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bookings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  @JoinColumn(name = "concert_id", nullable = false)
  private Concert concert;

  @Column(name = "user_id", nullable = false, length = 100)
  private String userId;

  @Column(nullable = false)
  private int quantity;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private BookingStatus status;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

   public Booking(Concert concert, String userId, int quantity, BookingStatus status, Instant createdAt) {
    this.concert = concert;
    this.userId = userId;
    this.quantity = quantity;
    this.status = status;
    this.createdAt = createdAt;
  }

  public Long getConcertId() {
    return concert.getId();
  }
 
}
