package com.tracker.unique.generation;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/generate")
public class GenerationController {

  private final SnowflakeIdGenerator idGenerator;

  public GenerationController(SnowflakeIdGenerator idGenerator) {
    this.idGenerator = idGenerator;
  }

  @GetMapping("/unique")
  public String generateTrackingNumber() {
    long id = idGenerator.generateId();
    return String.valueOf(id);
  }
}
