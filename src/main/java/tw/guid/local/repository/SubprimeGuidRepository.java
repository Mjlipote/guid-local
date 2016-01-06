/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.guid.central.core.PrefixedHashBundle;
import tw.guid.local.entity.SubprimeGuid;

public interface SubprimeGuidRepository
    extends JpaRepository<SubprimeGuid, Long> {

  public SubprimeGuid findByHashcode1AndHashcode2AndHashcode3AndPrefix(
      String hashcode1, String hashcode2, String hashcode3, String prefix);

  public SubprimeGuid findBySpguid(String spguid);

  public Set<SubprimeGuid> findByHashcode1AndHashcode2AndHashcode3(
      String hashcode1, String hashcode2, String hashcode3);

  public default boolean isExist(PrefixedHashBundle prefixedHashBundle) {

    return findByHashcode1AndHashcode2AndHashcode3AndPrefix(
        prefixedHashBundle.getHash1().substring(0, 128),
        prefixedHashBundle.getHash2().substring(0, 128),
        prefixedHashBundle.getHash3().substring(0, 128),
        prefixedHashBundle.getPrefix()) != null;
  }

  public default String getSubprimeGuidBySubprimeGuidRequest(
      PrefixedHashBundle prefixedHashBundle) {

    return findByHashcode1AndHashcode2AndHashcode3AndPrefix(
        prefixedHashBundle.getHash1().substring(0, 128),
        prefixedHashBundle.getHash2().substring(0, 128),
        prefixedHashBundle.getHash3().substring(0, 128),
        prefixedHashBundle.getPrefix()).getSpguid();

  }

}
