/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package tw.guid.local;

import org.springframework.context.annotation.Configuration;

import tw.guid.local.security.HashCodeEncryptor;

@Configuration
public class LocalConfig {

  public static final String CLIENT_KEY =
      "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkQDWTHqpKFNIGzHxcEBwNZhMGWBbNy_RuS0_-U1cOErUw93YAt5K9_NpqYqRqJzOQ_1pgfrpj_JvQH1ytwQISA5GAR6kgJxYkr7U-aK4YcW2ep7WxNPkEV9eB-sx5VF5SkMuMJJsquy9D_wRx2KiCtSd_NqQhE60ZRBkxSb55QMoIMDYxLkg8Vrvmj4pKqhV8RsSLZA9qwMs8vnFuXiy5OkWre9okbdyyM7imVtGTebh7cfSxbWwUQQ-E6XxNlJe1NJ-46OyEV5LxHTbcCkBdaYz7bLIKPPIHOSPEnVBjWiHJDxQEsnWfzg42vGHUuXkJdi1rtLS8tC4Hgx8FJ3U4wIDAQAB";

  public static final HashCodeEncryptor HASH_ENCRYPTOR =
      new HashCodeEncryptor();

}
