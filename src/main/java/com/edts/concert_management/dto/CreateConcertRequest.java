package com.edts.concert_management.dto;

import java.time.Instant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateConcertRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String venue;

    @NotNull
    private Instant startsAt;
    @NotNull
    private Instant bookingOpensAt;
    @NotNull
    private Instant bookingClosesAt;

    @Min(0)
    private int totalTickets;
}