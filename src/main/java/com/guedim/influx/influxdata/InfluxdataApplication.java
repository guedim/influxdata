package com.guedim.influx.influxdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.guedim.influx.influxdata.gs.MonitorGigaspaces;
import com.payulatam.chronos.Chronos;
import com.payulatam.chronos.processor.influxdb.InfluxDbProcessor;

@SpringBootApplication
@EnableScheduling
public class InfluxdataApplication {

  @Value("${influx.host}")
  private String host;
  @Value("${influx.user}")
  private String user;
  @Value("${influx.password}")
  private String password;
  @Value("${influx.database}")
  private String database;

  @Autowired
  private MonitorGigaspaces monitor;


  public static void main(String[] args) {
    SpringApplication.run(InfluxdataApplication.class, args);    
  }

  @EventListener(ApplicationReadyEvent.class)
  void writeInflux() {  
    Chronos.start(new  InfluxDbProcessor(host, user, password, database, 100, null), 60000);
  }
  
  @Scheduled(cron = "${cron.expression}")
  void monitor() {
    monitor.writeGigaspacesData();
  }
  
}