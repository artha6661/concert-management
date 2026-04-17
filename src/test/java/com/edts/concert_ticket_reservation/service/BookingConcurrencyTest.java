package com.edts.concert_ticket_reservation.service;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.edts.concert_management.ConcertTicketReservationApplication;
import com.edts.concert_management.enums.BookingStatus;
import com.edts.concert_management.model.Booking;
import com.edts.concert_management.repository.ConcertRepository;
import com.edts.concert_management.service.BookingService;
import com.edts.concert_ticket_reservation.testsupport.FixedClockTestConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ConcertTicketReservationApplication.class)
@Import(FixedClockTestConfig.class)
class BookingConcurrencyTest {
  @Autowired BookingService bookingService;
  @Autowired ConcertRepository concertRepository;

  @Test
  void onlyCapacityWorthOfBookingsCanConfirm() throws Exception {
    long concertId = 2L; // seeded with total=10, remaining=10

    int attempts = 50;
    ExecutorService exec = Executors.newFixedThreadPool(12);
    try {
      List<Callable<Booking>> tasks = new ArrayList<Callable<Booking>>();
      for (int i = 0; i < attempts; i++) {
        final int n = i;
        tasks.add(() -> bookingService.book(concertId, "u-" + n, 1));
      }

      List<Future<Booking>> futures = exec.invokeAll(tasks);
      int confirmed = 0;
      int rejected = 0;
      for (Future<Booking> f : futures) {
        Booking b = f.get();
        if (b.getStatus() == BookingStatus.CONFIRMED) confirmed++;
        if (b.getStatus() == BookingStatus.REJECTED) rejected++;
      }

      assertEquals(10, confirmed);
      assertEquals(attempts - 10, rejected);
      assertEquals(0, concertRepository.findById(concertId).get().getRemainingTickets());
    } finally {
      exec.shutdownNow();
    }
  }
}

