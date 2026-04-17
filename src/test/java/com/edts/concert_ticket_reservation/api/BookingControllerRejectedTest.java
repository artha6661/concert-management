package com.edts.concert_ticket_reservation.api;

import com.edts.concert_management.ConcertTicketReservationApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.edts.concert_ticket_reservation.testsupport.BeforeWindowClockTestConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ConcertTicketReservationApplication.class)
@AutoConfigureMockMvc
@Import(BeforeWindowClockTestConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BookingControllerRejectedTest {
  @Autowired MockMvc mvc;

  @Test
  void bookingOutsideWindowIsRejected() throws Exception {
    mvc.perform(
            post("/api/concerts/{concertId}/bookings", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"u-2\",\"quantity\":1}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("REJECTED"));
  }
}

