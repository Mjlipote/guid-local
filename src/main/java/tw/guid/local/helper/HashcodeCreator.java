/*
 *
 * @author Ming-Jheng Li
 *
 *
 *         Copyright 2015 Ming-Jheng Li
 *
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 *
 */

package tw.guid.local.helper;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

public final class HashcodeCreator {

  private HashcodeCreator() {}

  public static String convertToHex(byte[] data) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < data.length; i++) {
      int halfbyte = (data[i] >>> 4) & 0x0F;
      int two_halfs = 0;
      do {
        if ((0 <= halfbyte) && (halfbyte <= 9))
          buf.append((char) ('0' + halfbyte));
        else
          buf.append((char) ('a' + (halfbyte - 10)));
        halfbyte = data[i] & 0x0F;
      } while (two_halfs++ < 1);
    }
    return buf.toString();
  }

  public static String getSha512(String text) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA-512");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    byte[] sha512hash = new byte[40];
    try {
      md.update(text.getBytes("ISO-8859-1"), 0, text.length());
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    sha512hash = md.digest();
    return convertToHex(sha512hash);
  }

  public static String getCrc32(String str) {
    byte[] bytes = (str).getBytes();
    CRC32 x = new CRC32();
    x.update(bytes);

    String crc = Long.toHexString(x.getValue());

    while (crc.length() != 8) {
      crc = "0" + crc;
    }

    return crc;
  }

}
