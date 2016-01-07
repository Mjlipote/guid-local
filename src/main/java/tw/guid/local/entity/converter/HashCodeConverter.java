/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package tw.guid.local.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import tw.guid.local.HashCodeEncryptorHolder;

/**
 * 
 * @author Wei-Ming Wu
 *
 */
@Converter
public class HashCodeConverter implements AttributeConverter<String, String> {

  @Override
  public String convertToDatabaseColumn(String hash) {
    return HashCodeEncryptorHolder.getHashCodeEncryptor().encrypt(hash);
  }

  @Override
  public String convertToEntityAttribute(String dbData) {
    return dbData;
  }

}
