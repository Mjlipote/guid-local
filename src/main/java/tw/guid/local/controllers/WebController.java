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
import static com.google.common.collect.Sets.newHashSet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.wnameless.json.flattener.JsonFlattener;

import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Name;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.guid.local.config.RestfulConfig;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.models.AccountUsersResponse;
import tw.guid.local.models.Action;
import tw.guid.local.models.CustomAuthenticationProvider;
import tw.guid.local.models.Role;
import tw.guid.local.models.SubprimeGuidRequest;
import tw.guid.local.models.entity.AccountUsers;
import tw.guid.local.models.entity.SubprimeGuid;
import tw.guid.local.models.repo.AccountUsersRepository;
import tw.guid.local.models.repo.ActionAuditRepository;
import tw.guid.local.models.repo.SubprimeGuidRepository;

@RequestMapping("/guid/web")
@Controller
public class WebController {

  @Autowired
  ActionAuditRepository actionAuditRepo;
  @Autowired
  SubprimeGuidRepository spguidRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  /**
   * 批次產生 GUID
   * 
   * @param spGuidCreateRequestList
   * @return
   * @throws JsonProcessingException
   * @throws URISyntaxException
   */
  @RequestMapping(value = "/batch", method = RequestMethod.POST)
  List<String> batch(
      @RequestBody List<SubprimeGuidRequest> spGuidCreateRequestList)
          throws JsonProcessingException, URISyntaxException {

    Map<String, Object> flattenJson =
        JsonFlattener
            .flattenAsMap(
                HttpActionHelper
                    .toPost(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL),
                        Action.CREATE, spGuidCreateRequestList, false)
                    .getBody());

    List<String> list = newArrayList();

    for (int i = 0; i < flattenJson.size(); i++) {
      if (flattenJson.get("[" + i + "].spguid") != null)
        list.add(flattenJson.get("[" + i + "].spguid").toString());
    }
    return list;
  }

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
   * @throws URISyntaxException
   * @throws IOException
   */
  @RequestMapping(value = "/create", method = RequestMethod.POST)
  String webCreate(ModelMap map, @RequestParam(value = "gender") String gender,
      @RequestParam(value = "birthOfYear") String birthOfYear,
      @RequestParam(value = "birthOfMonth") String birthOfMonth,
      @RequestParam(value = "birthOfDay") String birthOfDay,
      @RequestParam(value = "sid") String sid,
      @RequestParam(value = "name") String name)
          throws URISyntaxException, IOException {
    if (gender.equals("") || birthOfYear.equals("") || birthOfMonth.equals("")
        || birthOfDay.equals("") || sid.equals("") || name.equals("")) {
      return "null-error";
    } else {
      PII pii =
          new PII.Builder(new Name(name.substring(1, 3), name.substring(0, 1)),
              gender.equals("M") ? Sex.MALE : Sex.FEMALE,
              new Birthday(Integer.valueOf(birthOfYear),
                  Integer.valueOf(birthOfMonth), Integer.valueOf(birthOfDay)),
          new TWNationalId(sid)).build();

      String prefix = acctUserRepo
          .findByUsername(customAuthenticationProvider.getName()).getPrefix();
      SubprimeGuid sg =
          spguidRepo.findByHashcode1AndHashcode2AndHashcode3AndPrefix(
              pii.getHashcodes().get(0), pii.getHashcodes().get(1),
              pii.getHashcodes().get(2), prefix);

      if (sg != null) {
        map.addAttribute("spguids", "REPEAT:" + sg.getSpguid());
        return "create-result";
      } else {

        List<SubprimeGuidRequest> sgrs = newArrayList();
        SubprimeGuidRequest sgr = new SubprimeGuidRequest();

        sgr.setGuidHash(pii.getHashcodes());
        sgr.setPrefix(prefix);
        sgrs.add(sgr);

        Map<String, Object> flattenJson =
            JsonFlattener.flattenAsMap(HttpActionHelper
                .toPost(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL),
                    Action.CREATE, sgrs, false)
                .getBody());

        map.addAttribute("spguids", flattenJson.get("[0].spguid").toString());

        SubprimeGuid spGuid = new SubprimeGuid();
        spGuid.setSpguid(flattenJson.get("[0].spguid").toString());
        spGuid.setHashcode1(pii.getHashcodes().get(0));
        spGuid.setHashcode2(pii.getHashcodes().get(1));
        spGuid.setHashcode3(pii.getHashcodes().get(2));
        spGuid.setPrefix(prefix);
        spguidRepo.save(spGuid);

        return "create-result";
      }
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
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  String register(ModelMap map,
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

      return "register-success";
    }
  }

  /**
   * 刪除一般使用者
   * 
   * @param map
   * @param username
   * @return
   */
  @RequestMapping(value = "/deleteuser", method = RequestMethod.POST)
  String deleteuser(ModelMap map,
      @RequestParam(value = "username") String username) {

    if (username.equals("")) {
      return "null-error";
    } else {
      AccountUsers acctUser =
          acctUserRepo.findByUsernameAndRole(username, Role.ROLE_USER);

      // 待補！！應該使用下拉選單，以避免找不到要刪除的 User
      acctUserRepo.delete(acctUser);
      map.addAttribute("users", acctUser);
      return "deleteuser-success";

    }
  }

  /**
   * 使用者名單
   * 
   * @return
   */

  @RequestMapping(value = "/users", method = RequestMethod.GET)
  List<AccountUsersResponse> users() {

    // 未補完
    return AccountUsersResponse.getResponse(acctUserRepo.findAll());
  }

  /**
   * 搜尋使用者
   * 
   * @param map
   * @param username
   * @param prefix
   * @param institute
   * @return
   */
  @RequestMapping(value = "/user", method = RequestMethod.GET)
  String user(ModelMap map, @RequestParam(value = "username") String username,
      @RequestParam(value = "prefix") String prefix,
      @RequestParam(value = "institute") String institute) {

    Set<AccountUsers> aus = newHashSet();
    if (username.equals("") && prefix.equals("") && institute.equals("")) {
      return "null-error";
    } else {
      aus.add(acctUserRepo.findByUsername(username));
      aus.addAll(acctUserRepo.findByInstitute(institute));
      aus.addAll(acctUserRepo.findByPrefix(prefix));
      map.addAttribute("accountUsersSet", aus);
      return "search";
    }
  }

  /**
   * 在 Web 進行二次編碼比對
   * 
   * @param map
   * @param subprimeGuids
   * @return
   * @throws JsonProcessingException
   * @throws URISyntaxException
   */
  @RequestMapping(value = "/comparison", method = RequestMethod.POST)
  String comparison(ModelMap map,
      @RequestParam(value = "subprimeGuids") String subprimeGuids)
          throws JsonProcessingException, URISyntaxException {
    List<String> list = newArrayList();
    String[] str = subprimeGuids.trim().split(",");
    for (String s : str) {
      list.add(s);
    }
    map.addAttribute("result",
        HttpActionHelper.toPost(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL),
            Action.COMPARISON, list, false).getBody());

    return "comparison-result";
  }

}