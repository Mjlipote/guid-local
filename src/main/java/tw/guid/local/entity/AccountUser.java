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

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.AbstractPersistable;

import com.google.common.base.MoreObjects;

import tw.guid.local.helper.HashcodeCreator;
import tw.guid.local.web.Role;

@Entity
public class AccountUser extends AbstractPersistable<Long> {

  private static final long serialVersionUID = 1L;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String email;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "INSTITUTEPREFIX_ID", nullable = false)
  private InstitutePrefix institutePrefix;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(nullable = true)
  private String jobTitle;

  @Column(nullable = true)
  private String telephone;

  @Column(nullable = true)
  private String address;

  public AccountUser() {}

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username
   *          the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @param password
   *          the password to set
   */
  public void setPassword(String password) {

    this.password = HashcodeCreator.getSha512(checkNotNull(password));
  }

  /**
   * @param password
   *          the password to set
   */
  public void setHashPassword(String sha512hash) {

    this.password = sha512hash;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email
   *          the email to set
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the role
   */
  public Role getRole() {
    return role;
  }

  /**
   * @param role
   *          the role to set
   */
  public void setRole(Role role) {
    this.role = role;
  }

  /**
   * @return the jobTitle
   */
  public String getJobTitle() {
    return jobTitle;
  }

  /**
   * @param jobTitle
   *          the jobTitle to set
   */
  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  /**
   * @return the telephone
   */
  public String getTelephone() {
    return telephone;
  }

  /**
   * @param telephone
   *          the telephone to set
   */
  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  /**
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * @param address
   *          the address to set
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * @return the institutePrefix
   */
  public InstitutePrefix getInstitutePrefix() {
    return institutePrefix;
  }

  /**
   * @param institutePrefix
   *          the institutePrefix to set
   */
  public void setInstitutePrefix(InstitutePrefix institutePrefix) {
    this.institutePrefix = institutePrefix;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("username", username)
        .add("password", password).add("email", email)
        .add("institutePrefix", institutePrefix).add("role", role)
        .add("jobTitle", jobTitle).add("telephone", telephone)
        .add("address", address).toString();
  }

}
