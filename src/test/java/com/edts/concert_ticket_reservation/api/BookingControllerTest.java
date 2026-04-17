package com.edts.concert_ticket_reservation.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.edts.concert_ticket_reservation.testsupport.FixedClockTestConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(FixedClockTestConfig.class)
class BookingControllerTest {
  @Autowired MockMvc mvc;

  @Test
  void bookingWithinWindowCanConfirm() throws Exception {
    mvc.perform(
            post("/api/concerts/{concertId}/bookings", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":\"u-1\",\"quantity\":2}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("CONFIRMED"))
        .andExpect(jsonPath("$.concertId").value(1))
        .andExpect(jsonPath("$.quantity").value(2));
  }
}

