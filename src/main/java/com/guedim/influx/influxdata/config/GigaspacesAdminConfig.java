/**
 * PayU Latam - Copyright (c) 2013 - 2014 http://www.payulatam.com Date: 8/10/2014
 */
package com.guedim.influx.influxdata.config;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


/**
 * GigaSpaces Admin service implementation.
 * 
 * @author <a href="manuel.vieda@payulatam.com">Manuel E. Vieda</a>
 * @version 1.0
 * @since 4.5.8
 */
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
