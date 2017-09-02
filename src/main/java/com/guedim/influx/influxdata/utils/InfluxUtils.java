package com.guedim.influx.influxdata.utils;

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
  
  public static Double formatCpuPerc(String cpuFormated) {  
    try {
       return Double.valueOf((cpuFormated.substring(0, cpuFormated.length()-1)))*100;  
    } catch (Exception e) {
      return 0D;
    }
  }
  
  public static Double formatDouble(Double plainNumber) {  
    try {
       return ((int)(plainNumber * 100.0d))/100.0d;  
    } catch (Exception e) {
      return 0D;
    }
  }
  

  
  public static void main(String[] args) {
    System.out.println(formatDouble(-2.790148E9));
  }
}
