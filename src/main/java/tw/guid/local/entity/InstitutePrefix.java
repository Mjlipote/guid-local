/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
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
