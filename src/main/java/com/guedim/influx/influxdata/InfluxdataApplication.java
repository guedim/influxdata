package com.guedim.influx.influxdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class InfluxdataApplication {

  @Autowired
  private InfluxCpuDisk cpu;
  @Autowired
  private InfluxSeno seno;

  public static void main(String[] args) {
    SpringApplication.run(InfluxdataApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  void writeInflux() {
    new Thread(() -> seno.influxSeno()).start();
    new Thread(() -> cpu.influxCpuDisk()).start();
  }
}