/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package tw.guid.local.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Wei-Ming Wu
 *
 */
public class HashCodeEncryptorHolder implements InitializingBean {

  private HashCodeEncryptor encryptor;

  private static HashCodeEncryptorHolder instance;

  public static HashCodeEncryptor getHashCodeEncryptor() {
    return instance.encryptor;
  }

  @Autowired
  public void setHashCodeEncryptor(HashCodeEncryptor encryptor) {
    this.encryptor = encryptor;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    HashCodeEncryptorHolder.instance = this;
  }

}
