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

package tw.guid.local.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

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

  @Column(nullable = false)
  private String institute;

  @Column(nullable = false)
  private String prefix;

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

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof AccountUser)) return false;
    AccountUser castOther = (AccountUser) other;
    return Objects.equals(username, castOther.username)
        && Objects.equals(password, castOther.password)
        && Objects.equals(email, castOther.email)
        && Objects.equals(institute, castOther.institute)
        && Objects.equals(prefix, castOther.prefix)
        && Objects.equals(role, castOther.role)
        && Objects.equals(jobTitle, castOther.jobTitle)
        && Objects.equals(telephone, castOther.telephone)
        && Objects.equals(address, castOther.address);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, email, institute, prefix, role,
        jobTitle, telephone, address);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("username", username)
        .add("password", password).add("email", email)
        .add("institute", institute).add("prefix", prefix).add("role", role)
        .add("jobTitle", jobTitle).add("telephone", telephone)
        .add("address", address).toString();
  }

}
