package com.guedim.influx.influxdata.gs;

import java.util.Map;

import org.openspaces.admin.Admin;
import org.openspaces.admin.gsc.GridServiceContainer;
import org.openspaces.admin.vm.VirtualMachine;
import org.openspaces.admin.vm.VirtualMachineStatistics;
import org.openspaces.admin.zone.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.guedim.influx.influxdata.utils.InfluxUtils;
import com.payulatam.chronos.ChronoRecord;
import com.payulatam.chronos.Chronos;
import com.payulatam.chronos.Field;
import com.payulatam.chronos.Tag;

@Component
public class MonitorGigaspaces {

  private static final String GIGASPACES_METRIC_NAME = "gs-pps";

  @Autowired
  private Admin admin;


  public void writeGigaspacesData() {

    for (GridServiceContainer gsc : admin.getGridServiceContainers()) {

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

          Tag[] tags = new Tag [4];
          tags[0] = new Tag("gsc-zone", zone);
          tags[1] = new Tag("machine", machine);
          tags[2] = new Tag("jvm-version", jvmVersion);
          tags[3] = new Tag("jvm-pid", jvmPid);
          
          Field[] fields = new Field.Builder()
              
              .field("jvm-cpu", InfluxUtils.formatCpuPerc(jvmStatistics.getCpuPercFormatted()))
              .field("jvm-thread-numbers", jvmStatistics.getThreadCount())
              
              .field("jvm-heap-memory-used-perc", jvmStatistics.getMemoryHeapUsedPerc())
              .field("jvm-heap-memory-used", jvmStatistics.getMemoryHeapUsedInMB())
              .field("jvm-heap-memory-committed", jvmStatistics.getMemoryHeapCommittedInMB())
              .field("jvm-max-heap-memory", jvmStatistics.getDetails().getMemoryHeapMaxInMB())
              
              .field("jvm-non-heap-memory-used-perc", InfluxUtils.formatDouble(jvmStatistics.getMemoryNonHeapUsedPerc()))
              .field("jvm-non-heap-memory-used", jvmStatistics.getMemoryNonHeapUsedInMB())
              .field("jvm-non-heap-memory-commited", jvmStatistics.getMemoryNonHeapCommittedInMB())
              .field("jvm-max-non-heap-memory",  InfluxUtils.formatDouble(jvmStatistics.getDetails().getMemoryNonHeapMaxInMB()))
              .build();
          
          
          System.out.println("cpu:" + InfluxUtils.formatCpuPerc(jvmStatistics.getCpuPercFormatted()));
          System.out.println("non-heap-%:" + InfluxUtils.formatDouble(jvmStatistics.getMemoryNonHeapUsedPerc()));
          System.out.println("max-non-heap:" + InfluxUtils.formatDouble(jvmStatistics.getDetails().getMemoryNonHeapMaxInMB()));
          System.out.println("***********");
          
          ChronoRecord record = new ChronoRecord(null, GIGASPACES_METRIC_NAME, currentTimeMillis, currentTimeMillis, 0, null, 0L, tags, fields);
          Chronos.record(record); 
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
