/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tw.guid.local.service.ApiService;
import tw.guid.local.service.ApiServiceImpl;
import tw.guid.local.service.LegacyServiceImpl;

@Configuration
public class ServiceConfig {

  @Bean
  public ApiService apiService() {
    return new ApiServiceImpl();
  }

  @Bean
  public LegacyServiceImpl legacyServiceImpl(
      @Value("${guid.legacy.central.server}") String centralServer,
      @Value("${guid.legacy.client.key}") String clientKey)
          throws URISyntaxException {
    return new LegacyServiceImpl(centralServer, clientKey);
  }

}
