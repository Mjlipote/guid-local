/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.repository;

import static tw.guid.local.LocalConfig.HASH_ENCRYPTOR;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.guid.central.core.PrefixedHashBundle;
import tw.guid.local.entity.SubprimeGuid;

public interface SubprimeGuidRepository
    extends JpaRepository<SubprimeGuid, Long> {

  public SubprimeGuid findByHashcode1AndHashcode2AndHashcode3AndPrefix(
      String hashcode1, String hashcode2, String hashcode3, String prefix);

  default public SubprimeGuid findByHashcodesAndPrefix(String hashcode1,
      String hashcode2, String hashcode3, String prefix) {
    return findByHashcode1AndHashcode2AndHashcode3AndPrefix(
        HASH_ENCRYPTOR.encrypt(hashcode1), HASH_ENCRYPTOR.encrypt(hashcode2),
        HASH_ENCRYPTOR.encrypt(hashcode3), prefix);
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
