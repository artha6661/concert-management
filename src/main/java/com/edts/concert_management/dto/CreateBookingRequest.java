package com.edts.concert_management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateBookingRequest {
  @NotBlank
  private String userId;
  @Min(1)
  private int quantity;
}

