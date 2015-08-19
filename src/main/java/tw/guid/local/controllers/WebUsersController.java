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
package tw.guid.local.controllers;

import static com.google.common.base.Preconditions.checkNotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tw.guid.local.models.CustomAuthenticationProvider;
import tw.guid.local.models.Role;
import tw.guid.local.models.entity.AccountUsers;
import tw.guid.local.models.repo.AccountUsersRepository;
import tw.guid.local.models.repo.ActionAuditRepository;
import tw.guid.local.models.repo.SubprimeGuidRepository;

@RequestMapping("/users")
@Controller
public class WebUsersController {

  private static final Logger log =
      LoggerFactory.getLogger(WebUsersController.class);

  @Autowired
  ActionAuditRepository actionAuditRepo;
  @Autowired
  SubprimeGuidRepository subprimeGuidRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  /**
   * Users Lookup
   * 
   * @param map
   * @param username
   * @param role
   * @param page
   * @return
   */
  @RequestMapping(value = "/lookup", method = RequestMethod.GET)
  String usersLookup(ModelMap map, @Param("username") String username,
      @Param("role") String role, @Param("page") Integer page) {

    PageRequest pageReq =
        new PageRequest(0, 10, new Sort(new Order(Direction.ASC, "username")));

    Page<AccountUsers> accPage;

    if (username != null) {
      if (username.equals("") && role.equals("")) {
        accPage = acctUserRepo.findAll(pageReq);
      } else if (username.equals("") && role != null) {
        accPage = acctUserRepo.findByRole(
            role.equals("ROLE_ADMIN") ? Role.ROLE_ADMIN : Role.ROLE_USER,
            pageReq);
      } else if (!username.equals("") && role != null) {
        accPage = acctUserRepo.findByUsernameAndRole(username,
            role.equals("ROLE_ADMIN") ? Role.ROLE_ADMIN : Role.ROLE_USER,
            pageReq);
      } else {
        accPage = acctUserRepo.findByUsername(username, pageReq);
      }
    } else {
      accPage = acctUserRepo.findAll(pageReq);
    }

    map.addAttribute("contactsPage", accPage);

    return "users";

  }

  /**
   * 新增使用者
   * 
   * @param map
   * @param username
   * @param password
   * @param email
   * @param jobTitle
   * @param institute
   * @param telephone
   * @param address
   * @param prefix
   * @return
   */
  @RequestMapping(value = "/new", method = RequestMethod.POST)
  String usersNew(ModelMap map,
      @RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password,
      @RequestParam(value = "email") String email,
      @RequestParam(value = "jobTitle") String jobTitle,
      @RequestParam(value = "institute") String institute,
      @RequestParam(value = "telephone") String telephone,
      @RequestParam(value = "address") String address,
      @RequestParam(value = "prefix") String prefix,
      @RequestParam(value = "authority") String authority) {

    if (username.equals("") || password.equals("") || institute.equals("")
        || email.equals("") || prefix.equals("")) {
      return "null-error";
    } else {
      AccountUsers user = new AccountUsers();
      user.setUsername(checkNotNull(username));
      user.setPassword(checkNotNull(password));
      user.setInstitute(checkNotNull(institute));
      user.setEmail(checkNotNull(email));
      user.setPrefix(checkNotNull(prefix));
      user.setTelephone(telephone);
      user.setJobTitle(jobTitle);
      user.setAddress(address);
      user.setRole(
          authority.equals("ROLE_ADMIN") ? Role.ROLE_ADMIN : Role.ROLE_USER);

      acctUserRepo.save(user);
      map.addAttribute("users", user);

      return "users-new-success";
    }
  }

  /**
   * Get all user list
   * 
   * @param map
   * @param page
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.GET)
  String usersList(ModelMap map, @Param("page") Integer page) {

    PageRequest pageReq =
        new PageRequest(0, 10, new Sort(new Order(Direction.ASC, "username")));

    Page<AccountUsers> accPage;

    accPage = acctUserRepo.findAll(pageReq);

    map.addAttribute("accPage", accPage);
    return "users";

  }

  /**
   * 刪除一般使用者
   * 
   * 
   * @param map
   * @param username
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.DELETE)
  String usersRemove(ModelMap map,
      @RequestParam(value = "username") String username) {

    if (username.equals("")) {
      return "null-error";
    } else {
      AccountUsers acctUser =
          acctUserRepo.findByUsernameAndRole(username, Role.ROLE_USER);

      acctUserRepo.delete(acctUser);
      map.addAttribute("users", acctUser);
      return "users-remove-success";

    }
  }

  /**
   * Get users's presonal information
   * 
   * 
   * @param map
   * @param username
   * @return
   */
  @RequestMapping(value = "/changepassword", method = RequestMethod.GET)
  String usersPasswordChange() {

    return "change-password";

  }

  /**
   * Get users's presonal information
   * 
   * 
   * @param map
   * @param username
   * @return
   */
  @RequestMapping(value = "/{username}", method = RequestMethod.GET)
  String usersInfo(ModelMap map, @PathVariable("username") String username) {

    AccountUsers acctUser = acctUserRepo.findByUsername(username);

    map.addAttribute("usersInfo", acctUser);
    return "users-info";

  }

  /**
   * 
   * 
   * 
   * @param map
   * @param username
   * @return
   */
  @RequestMapping(value = "/{username}", method = RequestMethod.PUT)
  String usersPut(ModelMap map, @PathVariable("username") String username,
      @Param(value = "username") String usernameRequest,
      @Param(value = "email") String email,
      @Param(value = "jobTitle") String jobTitle,
      @Param(value = "telephone") String telephone,
      @Param(value = "address") String address) {

    if (username.equals("")) {
      return "null-error";
    } else {
      AccountUsers acctUser = acctUserRepo.findByUsername(username);

      acctUser.setEmail(email);
      acctUser.setJobTitle(jobTitle);
      acctUser.setTelephone(telephone);
      acctUser.setAddress(address);

      acctUserRepo.saveAndFlush(acctUser);
      map.addAttribute("users", acctUser);
      return "users-edit-success";

    }
  }

}