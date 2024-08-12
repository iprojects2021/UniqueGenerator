package com.tracker.unique.generation;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SnowflakeConfig {
  @Bean
  public SnowflakeIdGenerator snowflakeIdGenerator() {

    long workerId = 1L;
    long datacenterId = 1L;
    return new SnowflakeIdGenerator(workerId, datacenterId);
  }
}
