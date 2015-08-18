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
import static com.google.common.collect.Lists.newArrayList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.wnameless.json.flattener.JsonFlattener;

import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.models.Action;
import tw.guid.local.models.CustomAuthenticationProvider;
import tw.guid.local.models.Role;
import tw.guid.local.models.SubprimeGuidRequest;
import tw.guid.local.models.entity.AccountUsers;
import tw.guid.local.models.entity.SubprimeGuid;
import tw.guid.local.models.repo.AccountUsersRepository;
import tw.guid.local.models.repo.ActionAuditRepository;
import tw.guid.local.models.repo.SubprimeGuidRepository;
import tw.guid.local.util.NameSplitter;

@Controller
public class WebController {

  private static final Logger log =
      LoggerFactory.getLogger(WebController.class);

  @Autowired
  ActionAuditRepository actionAuditRepo;
  @Autowired
  SubprimeGuidRepository subprimeGuidRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  /**
   * 網頁版產生 GUID
   * 
   * @param map
   * @param gender
   * @param boy
   * @param bom
   * @param bod
   * @param sid
   * @param name
   * @return
   * @throws FileNotFoundException
   * @throws URISyntaxException
   * @throws IOException
   */
  @RequestMapping(value = "/guids/new", method = RequestMethod.POST)
  String guidsNew(ModelMap map, @RequestParam(value = "gender") String gender,
      @RequestParam(value = "birthOfYear") String birthOfYear,
      @RequestParam(value = "birthOfMonth") String birthOfMonth,
      @RequestParam(value = "birthOfDay") String birthOfDay,
      @RequestParam(value = "sid") String sid,
      @RequestParam(value = "name") String name)
          throws FileNotFoundException, IOException {
    if (gender.equals("") || birthOfYear.equals("") || birthOfMonth.equals("")
        || birthOfDay.equals("") || sid.equals("") || name.equals("")) {
      return "null-error";
    } else {
      PII pii = new PII.Builder(NameSplitter.split(name),
          gender.equals("M") ? Sex.MALE : Sex.FEMALE,
          new Birthday(Integer.valueOf(birthOfYear),
              Integer.valueOf(birthOfMonth), Integer.valueOf(birthOfDay)),
          new TWNationalId(sid)).build();

      Authentication auth =
          SecurityContextHolder.getContext().getAuthentication();

      String prefix = acctUserRepo.findByUsername(auth.getName()).getPrefix();
      SubprimeGuid sg =
          subprimeGuidRepo.findByHashcode1AndHashcode2AndHashcode3AndPrefix(
              pii.getHashcodes().get(0), pii.getHashcodes().get(1),
              pii.getHashcodes().get(2), prefix);

      if (sg != null) {
        map.addAttribute("spguids", "REPEAT:" + sg.getSpguid());
        return "guids-new-result";
      } else {

        List<SubprimeGuidRequest> sgrs = newArrayList();
        SubprimeGuidRequest sgr = new SubprimeGuidRequest();

        sgr.setGuidHash(pii.getHashcodes());
        sgr.setPrefix(prefix);
        sgrs.add(sgr);

        Properties prop = new Properties();
        prop.load(new FileInputStream("serverhost.properties"));

        Map<String, Object> flattenJson = null;
        try {
          flattenJson = JsonFlattener.flattenAsMap(HttpActionHelper
              .toPost(new URI(prop.getProperty("central_server_url")),
                  Action.CREATE, sgrs, false)
              .getBody());
        } catch (JsonProcessingException e) {
          log.error(e.getMessage(), e);
        } catch (URISyntaxException e) {
          log.error(e.getMessage(), e);
        }

        map.addAttribute("spguids", flattenJson.get("[0].spguid").toString());

        SubprimeGuid spGuid = new SubprimeGuid();
        spGuid.setSpguid(flattenJson.get("[0].spguid").toString());
        spGuid.setHashcode1(pii.getHashcodes().get(0));
        spGuid.setHashcode2(pii.getHashcodes().get(1));
        spGuid.setHashcode3(pii.getHashcodes().get(2));
        spGuid.setPrefix(prefix);
        subprimeGuidRepo.save(spGuid);

        return "guids-new-result";
      }
    }
  }

  @RequestMapping(value = "/users/lookup", method = RequestMethod.GET)
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

    map.addAttribute("accPage", accPage);
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
  @RequestMapping(value = "/users/new", method = RequestMethod.POST)
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
   * 刪除一般使用者
   * 
   * 
   * @param map
   * @param username
   * @return
   */
  @RequestMapping(value = "/users", method = RequestMethod.DELETE)
  String usersRemove(ModelMap map,
      @RequestParam(value = "username") String username) {

    if (username.equals("")) {
      return "null-error";
    } else {
      AccountUsers acctUser =
          acctUserRepo.findByUsernameAndRole(username, Role.ROLE_USER);

      // 待補！！應該使用下拉選單，以避免找不到要刪除的 User
      acctUserRepo.delete(acctUser);
      map.addAttribute("users", acctUser);
      return "users-remove-success";

    }
  }

  /**
   * 使用者名單
   * 
   * @return
   */

  /**
   * 在 Web 進行二次編碼比對
   * 
   * @param map
   * @param subprimeGuids
   * @return
   * @throws IOException
   * @throws URISyntaxException
   */
  @RequestMapping(value = "/guids/comparison", method = RequestMethod.POST)
  String guidsComparison(ModelMap map,
      @RequestParam(value = "subprimeGuids") String subprimeGuids)
          throws IOException, URISyntaxException {

    if (subprimeGuids.equals("")) {
      return "null-error";
    } else {
      List<String> list = newArrayList();
      String[] str = subprimeGuids.trim().split(",");
      for (String s : str) {
        list.add(s);
      }
      Properties prop = new Properties();
      prop.load(new FileInputStream("serverhost.properties"));

      Map<String, Object> flattenJson =
          JsonFlattener.flattenAsMap(HttpActionHelper
              .toPost(new URI(prop.getProperty("central_server_url")),
                  Action.COMPARISON, list, false)
              .getBody());

      List<List<String>> lls = newArrayList();

      for (int i = 0; i < flattenJson.size(); i++) {
        List<String> ls = newArrayList();
        for (int j = 0; j < flattenJson.size(); j++) {
          if (flattenJson.get("[" + i + "]" + "[" + j + "]") != null) {
            ls.add(flattenJson.get("[" + i + "]" + "[" + j + "]").toString());
          }
        }
        if (ls.size() > 0) lls.add(ls);
      }
      map.addAttribute("result", lls);
      map.addAttribute("number", lls.size());

      return "comparison-result";
    }
  }

}