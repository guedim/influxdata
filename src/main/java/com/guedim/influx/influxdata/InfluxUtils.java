package com.guedim.influx.influxdata;

import java.util.Random;

public final class InfluxUtils {

  private static final Random random = new Random();
  private static final int FROM = 1;
  private static final int TO = 100;


  public static void sleep(long milliseconds) {
    try {
      Thread.sleep(milliseconds);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static int nexValue() {
    return random.nextInt(TO - FROM) + FROM;
  }

}
