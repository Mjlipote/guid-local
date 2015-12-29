/*
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

package tw.guid.local.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.google.common.base.MoreObjects;

@Entity
public class InstitutePrefix extends AbstractPersistable<Long> {

  private static final long serialVersionUID = 1L;

  @OneToMany(mappedBy = "institutePrefix", cascade = { CascadeType.ALL },
      fetch = FetchType.EAGER)
  @Column(nullable = false)
  private Set<AccountUser> accountUsers;

  @Column(unique = true, nullable = false)
  private String institute;

  @Column(nullable = false)
  private String prefix;

  public InstitutePrefix() {}

  /**
   * @return the institute
   */
  public String getInstitute() {
    return institute;
  }

  /**
   * @param institute
   *          the institute to set
   */
  public void setInstitute(String institute) {
    this.institute = institute;
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
   * @return the accountUsers
   */
  public Set<AccountUser> getAccountUsers() {
    return accountUsers;
  }

  /**
   * @param accountUsers
   *          the accountUsers to set
   */
  public void setAccountUsers(Set<AccountUser> accountUsers) {
    this.accountUsers = accountUsers;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("institute", institute)
        .add("prefix", prefix).add("accountUsers", accountUsers).toString();
  }

}
