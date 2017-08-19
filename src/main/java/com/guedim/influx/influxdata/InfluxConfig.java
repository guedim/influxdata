package com.guedim.influx.influxdata;

import javax.annotation.PostConstruct;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxConfig {

  @Value("${influx.host}")
  private String INFLUXDB_HOST;
  @Value("${influx.user}")
  private String INFLUXDB_USER;
  @Value("${influx.password}")
  private String INFLUXDB_PASS;
  @Value("${influx.database}")
  private String INFLUXDB_DB;

  private InfluxDB influxDB;

  @PostConstruct
  public void init() {
    influxDB = InfluxDBFactory.connect(getINFLUXDB_HOST(), getINFLUXDB_PASS(), getINFLUXDB_PASS());
    
    if(!influxDB.databaseExists(getINFLUXDB_DB())){
      influxDB.createDatabase(getINFLUXDB_DB());
    }
    
    influxDB.setDatabase(getINFLUXDB_DB());
  }

  public String getINFLUXDB_HOST() {
    return INFLUXDB_HOST;
  }

  public String getINFLUXDB_USER() {
    return INFLUXDB_USER;
  }

  public String getINFLUXDB_PASS() {
    return INFLUXDB_PASS;
  }

  public String getINFLUXDB_DB() {
    return INFLUXDB_DB;
  }

  public InfluxDB getInfluxDB() {
    return influxDB;
  }
}