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

package tw.guid.local.repository;

import static com.google.common.collect.Sets.newHashSet;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.guid.local.entity.AccountUser;
import tw.guid.local.web.Role;

public interface AccountUsersRepository extends
    JpaRepository<AccountUser, Long>, JpaSpecificationExecutor<AccountUser> {

  // 一組
  public AccountUser findByUsername(String username);

  public Page<AccountUser> findByUsername(String username, Pageable pageable);

  public Set<AccountUser> findByInstitute(String institute);

  public Set<AccountUser> findByRole(Role role);

  public Set<AccountUser> findByPassword(String password);

  public Set<AccountUser> findByPrefix(String prefix);

  public Page<AccountUser> findByRole(Role role, Pageable pageable);

  public default Set<String> getAllPrefix() {
    Set<String> prefixSet = newHashSet();

    for (AccountUser acctUser : findAll()) {
      prefixSet.add(acctUser.getPrefix());
    }
    return prefixSet;
  }

  public default Set<String> getAllInstitute() {
    Set<String> instituteSet = newHashSet();

    for (AccountUser acctUser : findAll()) {
      instituteSet.add(acctUser.getInstitute());
    }
    return instituteSet;
  }

  // 兩組
  public AccountUser findByUsernameAndPassword(String username,
      String password);

  public AccountUser findByUsernameAndRole(String username, Role role);

  public Page<AccountUser> findByUsernameAndRole(String username, Role role,
      Pageable pageable);

  public Page<AccountUser> findByRoleAndPrefix(Role role, String prefix,
      Pageable pageable);

  public Page<AccountUser> findByUsernameAndPrefix(String username,
      String prefix, Pageable pageable);

  // 三組
  public Page<AccountUser> findByUsernameAndRoleAndPrefix(String username,
      Role role, String prefix, Pageable pageable);

  public AccountUser findByUsernameAndPasswordAndRole(String username,
      String password, Role role);

}
