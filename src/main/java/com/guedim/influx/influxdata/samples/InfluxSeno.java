package com.guedim.influx.influxdata.samples;

import static com.guedim.influx.influxdata.utils.InfluxUtils.sleep;

import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guedim.influx.influxdata.config.InfluxConfig;

@Component
public class InfluxSeno {

  @Autowired
  private InfluxConfig influxConfig; 
  
  public void influxSeno() {

    for (int i = 0; i < 3600; i++) {
      Point senoPoint =
          Point.measurement("sinwave").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
              .addField("valor", Math.sin(Math.toRadians(i % 360))).build();

      influxConfig.getInfluxDB().write(senoPoint);
      System.out.println("SENO:  Written point " + senoPoint);

      sleep(300);
    }

  }
}