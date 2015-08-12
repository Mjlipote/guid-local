package tw.guid.local.models;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import tw.guid.local.models.entity.AccountUsers;

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
public final class AccountUsersResponse {

  private String username;

  private String prefix;

  private String email;

  private String institute;

  private String jobTitle;

  private String telephone;

  private String address;

  private Role role;

  private AccountUsersResponse() {

  }

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
  public String toString() {
    return "AccountUsersResponse ["
        + (username != null ? "username=" + username + ", " : "")
        + (prefix != null ? "prefix=" + prefix + ", " : "")
        + (email != null ? "email=" + email + ", " : "")
        + (institute != null ? "institute=" + institute + ", " : "")
        + (jobTitle != null ? "jobTitle=" + jobTitle + ", " : "")
        + (telephone != null ? "telephone=" + telephone + ", " : "")
        + (address != null ? "address=" + address + ", " : "")
        + (role != null ? "role=" + role : "") + "]";
  }

}
