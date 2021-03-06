/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.controller;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.guid.local.entity.AccountUser;
import tw.guid.local.entity.InstitutePrefix;
import tw.guid.local.helper.HashcodeCreator;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.InstitutePrefixRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.web.CustomAuthenticationProvider;
import tw.guid.local.web.Role;

@RequestMapping("/users")
@Controller
public class WebUserController {

  @Autowired
  SubprimeGuidRepository subprimeGuidRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  InstitutePrefixRepository institutePrefixRepo;
  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  /**
   * Get all user list
   * 
   * @param map
   * @param page
   * @return
   */
  @RequestMapping(method = GET)
  String usersList(ModelMap map) {

    map.addAttribute("prefixs", institutePrefixRepo.getAllPrefix());
    map.addAttribute("institutes", institutePrefixRepo.getAllInstitute());
    map.addAttribute("acctUsers", acctUserRepo.findAll());
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
  @RequestMapping(value = "/lookup", method = GET)
  String usersLookup(ModelMap map, @Param("username") String username,
      @Param("role") String role, @Param("institute") String institute,
      @Param("jobTitle") String jobTitle, @Param("email") String email,
      @Param("telephone") String telephone, @Param("address") String address) {

    List<AccountUser> acctUsers = acctUserRepo.findAll((root, query, cb) -> {
      Predicate finalPredicate = cb.and();

      if (username != null && !username.equals("")) {
        finalPredicate =
            cb.and(finalPredicate, cb.equal(root.get("username"), username));
      }
      if (role != null && !role.equals("")) {
        finalPredicate = cb.and(finalPredicate, cb.equal(root.get("role"),
            role.equals("ROLE_ADMIN") ? Role.ROLE_ADMIN : Role.ROLE_USER));
      }
      if (institute != null && !institute.equals("")) {

        InstitutePrefix institutePrefix =
            institutePrefixRepo.findByInstitute(institute);
        finalPredicate = cb.and(finalPredicate,
            cb.equal(root.get("institutePrefix"), institutePrefix));
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

    map.addAttribute("institutes", institutePrefixRepo.getAllInstitute());
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
  @RequestMapping(value = "/{username}", method = DELETE)
  String usersRemove(ModelMap map, @PathVariable("username") String username) {

    if (username.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫資料，切勿留空值！！");
      map.addAttribute("link", "/users");
      return "error";
    } else if (acctUserRepo.findByUsername(username).getRole()
        .equals(Role.ROLE_ADMIN)) {
      map.addAttribute("errorMessage", "無法刪除系統管理員帳號！！");
      map.addAttribute("link", "/users");
      return "error";
    } else {
      AccountUser acctUser =
          acctUserRepo.findByUsernameAndRole(username, Role.ROLE_USER);

      institutePrefixRepo
          .findByInstitute(acctUser.getInstitutePrefix().getInstitute())
          .getAccountUsers().remove(acctUser);

      acctUser.setInstitutePrefix(new InstitutePrefix());

      acctUserRepo.delete(acctUser);

      return "redirect:/users";

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
  @RequestMapping(method = POST)
  String usersNew(ModelMap map,
      @RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password,
      @RequestParam(value = "email") String email,
      @RequestParam(value = "jobTitle") String jobTitle,
      @RequestParam(value = "institute") String institute,
      @RequestParam(value = "telephone") String telephone,
      @RequestParam(value = "address") String address,
      @RequestParam(value = "authority") String authority) {

    if (username.equals("") || password.equals("") || institute.equals("")
        || email.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫資料，切勿留空值！！");
      map.addAttribute("link", "/register");
      return "error";
    } else if (acctUserRepo.findByUsername(username) != null) {
      map.addAttribute("errorMessage", "填寫的帳號名稱已被註冊，請替換使用者名稱！！");
      map.addAttribute("link", "/register");
      return "error";
    } else {
      InstitutePrefix institutePrefix =
          institutePrefixRepo.findByInstitute(institute);

      AccountUser user = new AccountUser();
      user.setUsername(checkNotNull(username));
      user.setPassword(checkNotNull(password));
      user.setInstitutePrefix(checkNotNull(institutePrefix));
      user.setEmail(checkNotNull(email));
      user.setTelephone(telephone);
      user.setJobTitle(jobTitle);
      user.setAddress(address);
      user.setRole(
          authority.equals("ROLE_ADMIN") ? Role.ROLE_ADMIN : Role.ROLE_USER);

      acctUserRepo.save(user);

      Set<AccountUser> aus = newHashSet();
      aus.add(user);
      institutePrefix.setAccountUsers(aus);
      institutePrefixRepo.save(institutePrefix);

      return "redirect:/users";
    }
  }

  /**
   * Get users's personal information
   * 
   * 
   * @param map
   * @param username
   * @return
   */
  @RequestMapping(value = "/{username}", method = GET)
  String usersGetPersonalInfo(ModelMap map,
      @PathVariable("username") String username) {

    AccountUser acctUser = acctUserRepo.findByUsername(username);

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
  @RequestMapping(value = "/{username}", method = PUT)
  String usersEdit(ModelMap map, @PathVariable("username") String username,
      @Param(value = "email") String email,
      @Param(value = "jobTitle") String jobTitle,
      @Param(value = "telephone") String telephone,
      @Param(value = "address") String address) {

    if (username.equals("") || username.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫資料，切勿留空值！！");
      map.addAttribute("link", "/users/" + "username");
      return "error";
    } else {
      AccountUser acctUser = acctUserRepo.findByUsername(username);

      acctUser.setEmail(email);
      acctUser.setJobTitle(jobTitle);
      acctUser.setTelephone(telephone);
      acctUser.setAddress(address);

      acctUserRepo.saveAndFlush(acctUser);
      map.addAttribute("successMessage",
          "已成功修改 " + acctUser.getUsername() + " 的個人資料");
      map.addAttribute("link", "/home");
      return "success";

    }
  }

  @RequestMapping(value = "/changepassword", method = GET)
  String usersChangePasswordWebpage() {

    return "change-password";

  }

  @RequestMapping(value = "/changepassword/{username}", method = PUT)
  String usersChangePassword(ModelMap map,
      @PathVariable("username") String username,
      @Param(value = "oldpassword") String oldpassword,
      @Param(value = "checkpassword") String checkpassword,
      @Param(value = "newpassword") String newpassword) {

    if (username.equals("") || oldpassword.equals("")
        || checkpassword.equals("") || newpassword.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫資料，切勿留空值！！");
      map.addAttribute("link", "/users/changepassword");
      return "error";
    } else if (acctUserRepo.findByUsernameAndPassword(username,
        HashcodeCreator.getSha512(oldpassword)) == null
        || acctUserRepo.findByUsernameAndPassword(username,
            HashcodeCreator.getSha512(checkpassword)) == null) {
      map.addAttribute("errorMessage", "所填寫的舊密碼有誤，請您再次確認！！");
      map.addAttribute("link", "/users/changepassword");
      return "error";
    } else {
      AccountUser acctUser = acctUserRepo.findByUsername(username);
      acctUser.setPassword(newpassword);
      acctUserRepo.saveAndFlush(acctUser);
      map.addAttribute("successMessage",
          "已成功修改 " + acctUser.getUsername() + " 的登入密碼");
      map.addAttribute("link", "/home");
      return "success";
    }
  }

}