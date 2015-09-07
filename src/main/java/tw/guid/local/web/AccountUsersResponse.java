/*
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
package tw.guid.local.web;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Objects;

import com.google.common.base.MoreObjects;

import tw.guid.local.entity.AccountUser;
import tw.guid.local.entity.InstitutePrefix;

public final class AccountUsersResponse {

  private String username;

  private InstitutePrefix institutePrefix;

  private String email;

  private String jobTitle;

  private String telephone;

  private String address;

  private Role role;

  private AccountUsersResponse() {}

  public static List<AccountUsersResponse> getResponse(
      List<AccountUser> accountusers) {
    List<AccountUsersResponse> aurs = newArrayList();

    for (AccountUser au : accountusers) {
      AccountUsersResponse aur = new AccountUsersResponse();

      aur.username = au.getUsername();
      aur.institutePrefix.setPrefix(au.getInstitutePrefix().getPrefix());
      aur.email = au.getEmail();
      aur.institutePrefix.setInstitute(au.getInstitutePrefix().getInstitute());
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
   * @return the email
   */
  public String getEmail() {
    return email;
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
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof AccountUsersResponse)) return false;
    AccountUsersResponse castOther = (AccountUsersResponse) other;
    return Objects.equals(username, castOther.username)
        && Objects.equals(institutePrefix, castOther.institutePrefix)
        && Objects.equals(email, castOther.email)
        && Objects.equals(jobTitle, castOther.jobTitle)
        && Objects.equals(telephone, castOther.telephone)
        && Objects.equals(address, castOther.address)
        && Objects.equals(role, castOther.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, institutePrefix, email, jobTitle, telephone,
        address, role);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("username", username)
        .add("institutePrefix", institutePrefix).add("email", email)
        .add("jobTitle", jobTitle).add("telephone", telephone)
        .add("address", address).add("role", role).toString();
  }

}
