/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package tw.guid.local;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tw.guid.client.GuidClient;
import tw.guid.local.security.HashCodeEncryptor;
import tw.guid.local.security.HashCodeEncryptorHolder;

@Configuration
public class LocalConfig {

  @Bean
  public HashCodeEncryptor hashCodeEncryptor(
      @Value("${guid.legacy.client.key}") String clientKey) {
    return new HashCodeEncryptor(clientKey);
  }

  @Bean
  public HashCodeEncryptorHolder hashCodeEncryptorHolder() {
    return new HashCodeEncryptorHolder();
  }

  @Bean
  public GuidClient guidClient(
      @Value("${guid.legacy.central.server}") String centralServerUrl,
      @Value("${guid.legacy.client.key}") String clientKey)
          throws URISyntaxException {
    return new GuidClient(new URI(centralServerUrl), clientKey);
  }

}
