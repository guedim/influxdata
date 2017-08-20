package com.guedim.influx.influxdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.guedim.influx.influxdata.gs.MonitorGigaspaces;
import com.guedim.influx.influxdata.samples.InfluxCpuDisk;
import com.guedim.influx.influxdata.samples.InfluxSeno;

@SpringBootApplication
@EnableScheduling
public class InfluxdataApplication {

  @Autowired
  private InfluxCpuDisk cpu;
  @Autowired
  private InfluxSeno seno;
  @Autowired
  private MonitorGigaspaces monitor;


  public static void main(String[] args) {
    SpringApplication.run(InfluxdataApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  void writeInflux() {
    new Thread(() -> seno.influxSeno()).start();
    new Thread(() -> cpu.influxCpuDisk()).start();
  }
  
  @Scheduled(cron = "${cron.expression}")
  void monitor() {
    monitor.writeGigaspacesData();
  }
}