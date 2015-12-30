/*
 *
 * @author Ming-Jheng Li
 *
 *
 * Copyright 2015 Ming-Jheng Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package tw.guid.local.config;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tw.guid.local.service.ApiService;
import tw.guid.local.service.ApiServiceImpl;
import tw.guid.local.service.LegacyServiceImpl;

@Configuration
public class ServicesConfig {

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
