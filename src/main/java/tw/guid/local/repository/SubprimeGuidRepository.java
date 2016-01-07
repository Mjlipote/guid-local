/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.guid.central.core.PrefixedHashBundle;
import tw.guid.local.HashCodeEncryptorHolder;
import tw.guid.local.entity.SubprimeGuid;

public interface SubprimeGuidRepository
    extends JpaRepository<SubprimeGuid, Long> {

  public SubprimeGuid findByHashcode1AndHashcode2AndHashcode3AndPrefix(
      String hashcode1, String hashcode2, String hashcode3, String prefix);

  default public SubprimeGuid findByHashcodesAndPrefix(String hashcode1,
      String hashcode2, String hashcode3, String prefix) {
    return findByHashcode1AndHashcode2AndHashcode3AndPrefix(
        HashCodeEncryptorHolder.getHashCodeEncryptor().encrypt(hashcode1),
        HashCodeEncryptorHolder.getHashCodeEncryptor().encrypt(hashcode2),
        HashCodeEncryptorHolder.getHashCodeEncryptor().encrypt(hashcode3),
        prefix);
  }

  public SubprimeGuid findBySpguid(String spguid);

  default public boolean isExist(PrefixedHashBundle prefixedHashBundle) {
    return findByHashcodesAndPrefix(
        prefixedHashBundle.getHash1().substring(0, 128),
        prefixedHashBundle.getHash2().substring(0, 128),
        prefixedHashBundle.getHash3().substring(0, 128),
        prefixedHashBundle.getPrefix()) != null;
  }

  default public String getSubprimeGuidBySubprimeGuidRequest(
      PrefixedHashBundle prefixedHashBundle) {
    return findByHashcodesAndPrefix(
        prefixedHashBundle.getHash1().substring(0, 128),
        prefixedHashBundle.getHash2().substring(0, 128),
        prefixedHashBundle.getHash3().substring(0, 128),
        prefixedHashBundle.getPrefix()).getSpguid();
  }

}
