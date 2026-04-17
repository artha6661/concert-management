package com.edts.concert_management.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClockProvider {
  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}

