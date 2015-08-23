/**
 *
 * @author Ming-Jheng Li
 *
 *
 *         Copyright 2015 Ming-Jheng Li
 *
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 *
 */
package tw.guid.local.models;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Objects;

import com.google.common.base.MoreObjects;

import tw.guid.local.models.entity.AccountUsers;

public final class AccountUsersResponse {

  private String username;

  private String prefix;

  private String email;

  private String institute;

  private String jobTitle;

  private String telephone;

  private String address;

  private Role role;

  private AccountUsersResponse() {}

  public static List<AccountUsersResponse> getResponse(
      List<AccountUsers> accountusers) {
    List<AccountUsersResponse> aurs = newArrayList();

    for (AccountUsers au : accountusers) {
      AccountUsersResponse aur = new AccountUsersResponse();
      aur.username = au.getUsername();
      aur.prefix = au.getPrefix();
      aur.email = au.getEmail();
      aur.institute = au.getInstitute();
      aur.jobTitle = au.getJobTitle();
      aur.telephone = au.getTelephone();
      aur.address = au.getAddress();
      aur.role = au.getRole();
      aurs.add(aur);
    }

    return aurs;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * @return the prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @return the institute
   */
  public String getInstitute() {
    return institute;
  }

  /**
   * @return the jobTitle
   */
  public String getJobTitle() {
    return jobTitle;
  }

  /**
   * @return the telephone
   */
  public String getTelephone() {
    return telephone;
  }

  /**
   * @return the address
   */
  public String getAddress() {
    return address;
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

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof AccountUsersResponse)) return false;
    AccountUsersResponse castOther = (AccountUsersResponse) other;
    return Objects.equals(username, castOther.username)
        && Objects.equals(prefix, castOther.prefix)
        && Objects.equals(email, castOther.email)
        && Objects.equals(institute, castOther.institute)
        && Objects.equals(jobTitle, castOther.jobTitle)
        && Objects.equals(telephone, castOther.telephone)
        && Objects.equals(address, castOther.address)
        && Objects.equals(role, castOther.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, prefix, email, institute, jobTitle, telephone,
        address, role);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("username", username)
        .add("prefix", prefix).add("email", email).add("institute", institute)
        .add("jobTitle", jobTitle).add("telephone", telephone)
        .add("address", address).add("role", role).toString();
  }

}
