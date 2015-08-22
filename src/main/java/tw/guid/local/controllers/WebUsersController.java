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

import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tw.guid.local.helper.Crc32HashcodeCreator;
import tw.guid.local.models.CustomAuthenticationProvider;
import tw.guid.local.models.RestfulAudit;
import tw.guid.local.models.Role;
import tw.guid.local.models.entity.AccountUsers;
import tw.guid.local.models.repo.AccountUsersRepository;
import tw.guid.local.models.repo.SubprimeGuidRepository;

@RequestMapping("/users")
@Controller
public class WebUsersController {

  @Autowired
  RestfulAudit restfulAudit;
  @Autowired
  SubprimeGuidRepository subprimeGuidRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  /**
   * Get all user list
   * 
   * @param map
   * @param page
   * @return
   */
  @RequestMapping(value = "", method = RequestMethod.GET)
  String usersList(ModelMap map) {

    List<AccountUsers> acctUsers;

    acctUsers = acctUserRepo.findAll();

    map.addAttribute("acctUsers", acctUsers);
    return "users";

  }

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
      @Param("role") String role, @Param("prefix") String prefix,
      @Param("institute") String institute, @Param("jobTitle") String jobTitle,
      @Param("email") String email, @Param("telephone") String telephone,
      @Param("address") String address) {

    List<AccountUsers> acctUsers = acctUserRepo.findAll((root, query, cb) -> {
      Predicate finalPredicate = cb.and();

      if (username != null && !username.equals("")) {
        finalPredicate =
            cb.and(finalPredicate, cb.equal(root.get("username"), username));
      }
      if (role != null && !role.equals("")) {
        finalPredicate = cb.and(finalPredicate, cb.equal(root.get("role"),
            role.equals("ROLE_ADMIN") ? Role.ROLE_ADMIN : Role.ROLE_USER));
      }
      if (prefix != null && !prefix.equals("")) {
        finalPredicate =
            cb.and(finalPredicate, cb.equal(root.get("prefix"), prefix));
      }
      if (institute != null && !institute.equals("")) {
        finalPredicate =
            cb.and(finalPredicate, cb.equal(root.get("institute"), institute));
      }
      if (jobTitle != null && !jobTitle.equals("")) {
        finalPredicate =
            cb.and(finalPredicate, cb.equal(root.get("jobTitle"), jobTitle));
      }
      if (email != null && !email.equals("")) {
        finalPredicate =
            cb.and(finalPredicate, cb.equal(root.get("email"), email));
      }
      if (telephone != null && !telephone.equals("")) {
        finalPredicate =
            cb.and(finalPredicate, cb.equal(root.get("telephone"), telephone));
      }
      if (address != null && !address.equals("")) {
        finalPredicate =
            cb.and(finalPredicate, cb.equal(root.get("address"), address));
      }

      return finalPredicate;
    });

    map.addAttribute("acctUsers", acctUsers);

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
  @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
  String usersRemove(ModelMap map, @PathVariable("username") String username) {

    if (username.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫資料，切勿留空值！！");
      return "error";
    } else if (acctUserRepo.findByUsername(username).getRole()
        .equals(Role.ROLE_ADMIN)) {
      map.addAttribute("errorMessage", "無法刪除系統管理員帳號！！");
      return "error";
    } else {
      AccountUsers acctUser =
          acctUserRepo.findByUsernameAndRole(username, Role.ROLE_USER);

      acctUserRepo.delete(acctUser);
      map.addAttribute("successMessage",
          "已成功刪除一筆使用者：" + acctUser.getUsername());
      return "success";

    }
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
      map.addAttribute("errorMessage", "請確實填寫資料，切勿留空值！！");
      return "error";
    } else if (acctUserRepo.findByUsername(username) != null) {
      map.addAttribute("errorMessage", "填寫的帳號名稱已被註冊，請替換使用者名稱！！");
      return "error";
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
      map.addAttribute("successMessage", "已成功新增一筆使用者：" + user.getUsername());

      return "success";
    }
  }

  /**
   * Change users's password
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
   * Get users's personal information
   * 
   * 
   * @param map
   * @param username
   * @return
   */
  @RequestMapping(value = "/{username}", method = RequestMethod.GET)
  String usersGetPersonalInfo(ModelMap map,
      @PathVariable("username") String username) {

    AccountUsers acctUser = acctUserRepo.findByUsername(username);

    map.addAttribute("usersInfo", acctUser);
    return "users-info";

  }

  /**
   * Edit user's personal information
   * 
   * 
   * @param map
   * @param username
   * @return
   */
  @RequestMapping(value = "/{username}", method = RequestMethod.PUT)
  String usersEdit(ModelMap map, @PathVariable("username") String username,
      @Param(value = "email") String email,
      @Param(value = "jobTitle") String jobTitle,
      @Param(value = "telephone") String telephone,
      @Param(value = "address") String address) {

    if (username.equals("") || username.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫資料，切勿留空值！！");
      return "error";
    } else {
      AccountUsers acctUser = acctUserRepo.findByUsername(username);

      acctUser.setEmail(email);
      acctUser.setJobTitle(jobTitle);
      acctUser.setTelephone(telephone);
      acctUser.setAddress(address);

      acctUserRepo.saveAndFlush(acctUser);
      map.addAttribute("successMessage",
          "已成功修改 " + acctUser.getUsername() + " 的個人資料");
      return "success";

    }
  }

  @RequestMapping(value = "/changepassword/{username}",
      method = RequestMethod.PUT)
  String usersChangePassword(ModelMap map,
      @PathVariable("username") String username,
      @Param(value = "oldpassword") String oldpassword,
      @Param(value = "checkpassword") String checkpassword,
      @Param(value = "newpassword") String newpassword) {

    if (username.equals("") || oldpassword.equals("")
        || checkpassword.equals("") || newpassword.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫資料，切勿留空值！！");
      return "error";
    } else if (acctUserRepo.findByUsernameAndPassword(username,
        Crc32HashcodeCreator.getCrc32(oldpassword)) == null
        || acctUserRepo.findByUsernameAndPassword(username,
            Crc32HashcodeCreator.getCrc32(checkpassword)) == null) {
      map.addAttribute("errorMessage", "所填寫的舊密碼有誤，請您再次確認！！");
      return "error";
    } else {
      AccountUsers acctUser = acctUserRepo.findByUsername(username);
      acctUser.setPassword(newpassword);
      acctUserRepo.saveAndFlush(acctUser);
      map.addAttribute("successMessage",
          "已成功修改 " + acctUser.getUsername() + " 的登入密碼");
      return "success";
    }
  }

}