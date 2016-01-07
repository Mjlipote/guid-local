/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package tw.guid.local.security;

import static com.google.common.io.BaseEncoding.base64Url;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;

import tw.guid.local.LocalConfig;

/**
 * 
 * @author Wei-Ming Wu
 *
 */
public final class HashCodeEncryptor {

  private final PublicKey pubKey;
  private final Cipher pkCipher;

  public HashCodeEncryptor() {
    byte[] publicBytes = base64Url().decode(LocalConfig.CLIENT_KEY);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
    KeyFactory keyFactory;
    try {
      keyFactory = KeyFactory.getInstance("RSA");
      pubKey = keyFactory.generatePublic(keySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }

    try {
      pkCipher = Cipher.getInstance("RSA");
      pkCipher.init(Cipher.ENCRYPT_MODE, pubKey);
    } catch (NoSuchAlgorithmException | NoSuchPaddingException
        | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  public String encrypt(String hash) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    CipherOutputStream os = new CipherOutputStream(baos, pkCipher);
    try {
      os.write(hash.getBytes());
      os.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return base64Url().encode(baos.toByteArray());
  }

}
