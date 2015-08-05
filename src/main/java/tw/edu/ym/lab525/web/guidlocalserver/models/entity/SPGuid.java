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

package tw.edu.ym.lab525.web.guidlocalserver.models.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
public class SPGuid extends AbstractPersistable<Long> {

  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private String spguid;

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

}
