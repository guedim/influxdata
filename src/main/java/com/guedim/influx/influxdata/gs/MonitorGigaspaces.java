package com.guedim.influx.influxdata.gs;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.dto.Point;
import org.openspaces.admin.Admin;
import org.openspaces.admin.gsc.GridServiceContainer;
import org.openspaces.admin.vm.VirtualMachine;
import org.openspaces.admin.vm.VirtualMachineStatistics;
import org.openspaces.admin.zone.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guedim.influx.influxdata.config.InfluxConfig;

@Component
public class MonitorGigaspaces {

  private static final String GIGASPACES_METRIC_NAME = "gs-pps";

  @Autowired
  private Admin admin;

  @Autowired
  private InfluxConfig influxConfig;


  public void writeGigaspacesData() {

    for (GridServiceContainer gsc : admin.getGridServiceContainers()) {

      System.out.println("GSC [" + gsc.getUid() + "] running on Machine " + gsc.getMachine().getHostAddress() + " and Zone: " + getZone(gsc));

      long currentTimeMillis = System.currentTimeMillis();
      String zone = getZone(gsc);
      String machine = null;
      String jvmVersion = null;
      String jvmPid = null;

      final VirtualMachine jvm = gsc.getVirtualMachine();
      if (jvm != null) {
          machine = jvm.getMachine().getHostAddress();
          jvmVersion = jvm.getDetails().getVmVersion();
          jvmPid = String.valueOf(jvm.getDetails().getPid());

        
        final VirtualMachineStatistics jvmStatistics = jvm.getStatistics();
        if (jvmStatistics != null) {

          Point jvmStatsPoint = Point.measurement(GIGASPACES_METRIC_NAME)
              .time(currentTimeMillis, TimeUnit.MILLISECONDS)
              .tag("    ", zone)
              .tag("machine", machine)
              .tag("jvm-version", jvmVersion)
              .tag("jvm-pid", jvmPid)
              .addField("jvm-cpu", jvmStatistics.getCpuPercFormatted())
              .addField("jvm-thread-numbers", jvmStatistics.getThreadCount())
              
              .addField("jvm-heap-memory-used-perc", jvmStatistics.getMemoryHeapUsedPerc())
              .addField("jvm-heap-memory-used", jvmStatistics.getMemoryHeapUsedInMB())
              .addField("jvm-heap-memory-committed", jvmStatistics.getMemoryHeapCommittedInMB())
              .addField("jvm-max-heap-memory", jvmStatistics.getDetails().getMemoryHeapInitInMB())
              
              .addField("jvm-non-heap-memory-used", jvmStatistics.getMemoryNonHeapUsedInMB())
              .addField("jvm-non-heap-memory-commited", jvmStatistics.getMemoryNonHeapCommittedInMB())
              .addField("jvm-max-non-heap-memory", jvmStatistics.getDetails().getMemoryNonHeapMaxInMB())
              
              
              .build();

          influxConfig.getInfluxDB().write(jvmStatsPoint);
          System.out.println("Se escribió el punto:" + jvmStatsPoint);
        }
      }
    }
  }

  private String getZone(GridServiceContainer gsc) {
    String zoneName = null;
    Map<String, Zone> zones = gsc.getZones();
    for (Map.Entry<String, Zone> zone : zones.entrySet()) {
      return zone.getValue() != null ? zone.getValue().getName() : zoneName;
    }
    return zoneName;
  }
}
