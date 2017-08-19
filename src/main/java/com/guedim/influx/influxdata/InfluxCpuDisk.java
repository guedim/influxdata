package com.guedim.influx.influxdata;

import static com.guedim.influx.influxdata.InfluxUtils.*;

import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InfluxCpuDisk {


  @Autowired
  private InfluxConfig influxConfig;
  
  public void influxCpuDisk() {

    for (int i = 0; i < 5000; i++) {
      Point cpuPoint = Point.measurement("cpu")
          .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS).addField("idle", nexValue())
          .addField("user", nexValue()).addField("system", nexValue()).build();

      Point diskPoint =
          Point.measurement("disk").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
              .addField("used", nexValue()).addField("free", nexValue()).build();

      influxConfig.getInfluxDB().write(cpuPoint);
      influxConfig.getInfluxDB().write(diskPoint);
      System.out.println("CpuDisk:  Written point " + i);
      sleep(500);
    }
  }
}
