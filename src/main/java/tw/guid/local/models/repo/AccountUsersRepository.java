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

package tw.guid.local.models.repo;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tw.guid.local.models.Role;
import tw.guid.local.models.entity.AccountUsers;

public interface AccountUsersRepository extends
    JpaRepository<AccountUsers, Long>, JpaSpecificationExecutor<AccountUsers> {

  // 一組
  public AccountUsers findByUsername(String username);

  public Page<AccountUsers> findByUsername(String username, Pageable pageable);

  public Set<AccountUsers> findByInstitute(String institute);

  public Set<AccountUsers> findByRole(Role role);

  public Set<AccountUsers> findByPassword(String password);

  public Set<AccountUsers> findByPrefix(String prefix);

  public Page<AccountUsers> findByRole(Role role, Pageable pageable);

  // 兩組
  public AccountUsers findByUsernameAndPassword(String username,
      String password);

  public AccountUsers findByUsernameAndRole(String username, Role role);

  public Page<AccountUsers> findByUsernameAndRole(String username, Role role,
      Pageable pageable);

  public Page<AccountUsers> findByRoleAndPrefix(Role role, String prefix,
      Pageable pageable);

  public Page<AccountUsers> findByUsernameAndPrefix(String username,
      String prefix, Pageable pageable);

  // 三組
  public Page<AccountUsers> findByUsernameAndRoleAndPrefix(String username,
      Role role, String prefix, Pageable pageable);

  public AccountUsers findByUsernameAndPasswordAndRole(String username,
      String password, Role role);

}
