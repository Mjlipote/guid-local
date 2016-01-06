/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.repository;

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

  public Set<AccountUser> findByRole(Role role);

  public Set<AccountUser> findByPassword(String password);

  public Page<AccountUser> findByRole(Role role, Pageable pageable);

  // 兩組
  public AccountUser findByUsernameAndPassword(String username,
      String password);

  public AccountUser findByUsernameAndRole(String username, Role role);

  public Page<AccountUser> findByUsernameAndRole(String username, Role role,
      Pageable pageable);

  // 三組

  public AccountUser findByUsernameAndPasswordAndRole(String username,
      String password, Role role);

}
