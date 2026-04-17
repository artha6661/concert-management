package com.edts.concert_ticket_reservation.api;

import com.edts.concert_management.ConcertTicketReservationApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ConcertTicketReservationApplication.class)
@AutoConfigureMockMvc
class AdminConcertControllerTest {
  @Autowired MockMvc mvc;

  @Test
  void canCreateConcert() throws Exception {
    String body =
        "{"
            + "\"name\":\"Admin Created\","
            + "\"venue\":\"Hall\","
            + "\"startsAt\":\"2026-05-01T19:00:00Z\","
            + "\"bookingOpensAt\":\"2026-04-15T10:00:00Z\","
            + "\"bookingClosesAt\":\"2026-04-15T10:20:00Z\","
            + "\"totalTickets\":123"
            + "}";

    mvc.perform(post("/api/admin/concerts").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.remainingTickets").value(123));
  }
}

