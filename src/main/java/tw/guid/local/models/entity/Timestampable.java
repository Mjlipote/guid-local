/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2014 Wei-Ming Wu
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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * {@link Timestampable} is a super class for any entity to extend from it. It
 * provides the automatic timestamp mechanism by JPA annotations.
 *
 */
@MappedSuperclass
public abstract class Timestampable {

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created_at")
  private Date createdAt;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "updated_at")
  private Date updatedAt;

  /**
   * Returns the creation date of this entity.
   * 
   * @return the creation date
   */
  public Date getCreatedAt() {
    return new Date(createdAt.getTime());
  }

  /**
   * Returns the last updated date of this entity.
   * 
   * @return the last updated date
   */
  public Date getUpdatedAt() {
    return new Date(updatedAt.getTime());
  }

  @PrePersist
  protected void onCreate() {
    updatedAt = createdAt = new Date();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = new Date();
  }

}
