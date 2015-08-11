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

package tw.edu.ym.lab525.web.guidlocalserver.models.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.edu.ym.lab525.web.guidlocalserver.models.Role;
import tw.edu.ym.lab525.web.guidlocalserver.models.entity.AccountUsers;

public interface AccountUsersRepository extends JpaRepository<AccountUsers, Long> {

  public AccountUsers findByUsername(String username);

  public Set<AccountUsers> findByPassword(String password);

  public Set<AccountUsers> findByPrefix(String prefix);

  public Set<AccountUsers> findByInstitute(String institute);

  public Set<AccountUsers> findByRole(Role role);

}
