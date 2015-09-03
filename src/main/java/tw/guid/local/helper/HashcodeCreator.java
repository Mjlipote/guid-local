/**
 * 
 * @author Ming-Jheng Li
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
