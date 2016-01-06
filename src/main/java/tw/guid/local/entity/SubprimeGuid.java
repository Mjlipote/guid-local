/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.google.common.base.MoreObjects;

@Entity
public class SubprimeGuid extends AbstractPersistable<Long> {

  private static final long serialVersionUID = 1L;

  @Column(unique = true, nullable = false)
  private String spguid;

  @Column(nullable = false)
  private String prefix;

  @Column(nullable = false)
  private String hashcode1;

  @Column(nullable = false)
  private String hashcode2;

  @Column(nullable = false)
  private String hashcode3;

  /**
   * @return the spguid
   */
  public String getSpguid() {
    return spguid;
  }

  /**
   * @return the prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * @param prefix
   *          the prefix to set
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  /**
   * @param spguid
   *          the spguid to set
   */
  public void setSpguid(String spguid) {
    this.spguid = spguid;
  }

  /**
   * @return the hashcode1
   */
  public String getHashcode1() {
    return hashcode1;
  }

  /**
   * @param hashcode1
   *          the hashcode1 to set
   */
  public void setHashcode1(String hashcode1) {
    this.hashcode1 = hashcode1;
  }

  /**
   * @return the hashcode2
   */
  public String getHashcode2() {
    return hashcode2;
  }

  /**
   * @param hashcode2
   *          the hashcode2 to set
   */
  public void setHashcode2(String hashcode2) {
    this.hashcode2 = hashcode2;
  }

  /**
   * @return the hashcode3
   */
  public String getHashcode3() {
    return hashcode3;
  }

  /**
   * @param hashcode3
   *          the hashcode3 to set
   */
  public void setHashcode3(String hashcode3) {
    this.hashcode3 = hashcode3;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("spguid", spguid)
        .add("prefix", prefix).add("hashcode1", hashcode1)
        .add("hashcode2", hashcode2).add("hashcode3", hashcode3).toString();
  }

}
