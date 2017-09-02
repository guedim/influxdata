package com.guedim.influx.influxdata.config;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class GigaspacesAdminConfig {

  /**
   * GigaSpace Space Locator
   */
  @Value("${os.space-locators}")
  private String lookuplocators;

  @Value("${os.space-group}")
  private String group;

  @Bean
  @Scope(value = "singleton")
  public Admin getAdminApiInstance() {
    return new AdminFactory().addLocator(lookuplocators).addGroup(group).createAdmin();
  }
}
