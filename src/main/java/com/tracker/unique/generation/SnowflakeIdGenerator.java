package com.tracker.unique.generation;


import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdGenerator {
  private final long twepoch = 1288834974657L;

  private final long workerIdBits = 5L;
  private final long datacenterIdBits = 5L;
  private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
  private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
  private final long sequenceBits = 12L;

  private final long workerIdShift = sequenceBits;
  private final long datacenterIdShift = sequenceBits + workerIdBits;
  private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
  private final long sequenceMask = -1L ^ (-1L << sequenceBits);

  private long workerId;
  private long datacenterId;
  private long sequence = 0L;
  private long lastTimestamp = -1L;

  public SnowflakeIdGenerator(long workerId, long datacenterId) {
    if (workerId > maxWorkerId || workerId < 0) {
      throw new IllegalArgumentException("worker Id can't be greater than " + maxWorkerId + " or less than 0");
    }
    if (datacenterId > maxDatacenterId || datacenterId < 0) {
      throw new IllegalArgumentException("datacenter Id can't be greater than " + maxDatacenterId + " or less than 0");
    }
    this.workerId = workerId;
    this.datacenterId = datacenterId;
  }

  public synchronized long generateId() {
    long timestamp = System.currentTimeMillis();

    if (timestamp < lastTimestamp) {
      throw new RuntimeException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
    }

    if (lastTimestamp == timestamp) {
      sequence = (sequence + 1) & sequenceMask;
      if (sequence == 0) {
        timestamp = tilNextMillis(lastTimestamp);
      }
    } else {
      sequence = 0L;
    }

    lastTimestamp = timestamp;

    return ((timestamp - twepoch) << timestampLeftShift) |
        (datacenterId << datacenterIdShift) |
        (workerId << workerIdShift) |
        sequence;
  }

  private long tilNextMillis(long lastTimestamp) {
    long timestamp = System.currentTimeMillis();
    while (timestamp <= lastTimestamp) {
      timestamp = System.currentTimeMillis();
    }
    return timestamp;
  }
}
