/**
 *
 * @author Ming-Jheng Li
 *
 *
 * Copyright 2015 Ming-Jheng Li
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package tw.guid.local.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.google.common.base.MoreObjects;

@Entity
public class SubprimeGuid extends AbstractPersistable<Long> {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
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
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
    result = prime * result + ((spguid == null) ? 0 : spguid.hashCode());
    result = prime * result + ((hashcode1 == null) ? 0 : hashcode1.hashCode());
    result = prime * result + ((hashcode2 == null) ? 0 : hashcode2.hashCode());
    result = prime * result + ((hashcode3 == null) ? 0 : hashcode3.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    SubprimeGuid other = (SubprimeGuid) obj;
    if (prefix == null) {
      if (other.prefix != null) return false;
    } else if (!prefix.equals(other.prefix)) return false;
    if (spguid == null) {
      if (other.spguid != null) return false;
    } else if (!spguid.equals(other.spguid)) return false;
    if (hashcode1 == null) {
      if (other.hashcode1 != null) return false;
    } else if (!hashcode1.equals(other.hashcode1)) return false;
    if (hashcode2 == null) {
      if (other.hashcode2 != null) return false;
    } else if (!hashcode2.equals(other.hashcode2)) return false;
    if (hashcode3 == null) {
      if (other.hashcode3 != null) return false;
    } else if (!hashcode3.equals(other.hashcode3)) return false;
    return true;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("spguid", spguid)
        .add("prefix", prefix).add("hashcode1", hashcode1)
        .add("hashcode2", hashcode2).add("hashcode3", hashcode3).toString();
  }

}
