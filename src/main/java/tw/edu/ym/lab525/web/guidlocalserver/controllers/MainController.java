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
package tw.edu.ym.lab525.web.guidlocalserver.controllers;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.wnameless.json.flattener.JsonFlattener;

import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Name;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.edu.ym.lab25.web.guidlocalserver.helper.HttpActionHelper;
import tw.edu.ym.lab525.web.guidlocalserver.config.RestfulConfig;
import tw.edu.ym.lab525.web.guidlocalserver.models.AccountUsersResponse;
import tw.edu.ym.lab525.web.guidlocalserver.models.Action;
import tw.edu.ym.lab525.web.guidlocalserver.models.CustomAuthenticationProvider;
import tw.edu.ym.lab525.web.guidlocalserver.models.Role;
import tw.edu.ym.lab525.web.guidlocalserver.models.SubprimeGuidRequest;
import tw.edu.ym.lab525.web.guidlocalserver.models.entity.AccountUsers;
import tw.edu.ym.lab525.web.guidlocalserver.models.entity.SubprimeGuid;
import tw.edu.ym.lab525.web.guidlocalserver.models.repo.AccountUsersRepository;
import tw.edu.ym.lab525.web.guidlocalserver.models.repo.ActionAuditRepository;
import tw.edu.ym.lab525.web.guidlocalserver.models.repo.SubprimeGuidRepository;

@RequestMapping("/guid")
// @RestController
@Controller
public class MainController {

  @Autowired
  ActionAuditRepository actionAuditRepo;
  @Autowired
  SubprimeGuidRepository spguidRepo;
  @Autowired
  AccountUsersRepository userRepo;
  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  /**
   *
   * 單筆產生 GUID
   *
   * @param spGuidCreateRequest
   * @return
   * @throws JsonProcessingException
   * @throws URISyntaxException
   */
  @RequestMapping(value = "/create", method = RequestMethod.POST)
      String create(@RequestBody SubprimeGuidRequest spGuidCreateRequest)
          throws JsonProcessingException, URISyntaxException {

    List<SubprimeGuidRequest> sgrs = newArrayList();
    sgrs.add(spGuidCreateRequest);

    Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(
        HttpActionHelper.toPost(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL), Action.CREATE, sgrs, false).getBody());

    return flattenJson.get("[0].spguid").toString();

  }

  /**
   * 批次產生 GUID
   * 
   * @param spGuidCreateRequestList
   * @return
   * @throws JsonProcessingException
   * @throws URISyntaxException
   */
  @RequestMapping(value = "/batch", method = RequestMethod.POST)
      List<String> batch(@RequestBody List<SubprimeGuidRequest> spGuidCreateRequestList)
          throws JsonProcessingException, URISyntaxException {

    Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(HttpActionHelper
        .toPost(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL), Action.CREATE, spGuidCreateRequestList, false)
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
  @RequestMapping(value = "/web/create", method = RequestMethod.POST)
      String webCreate(ModelMap map, @RequestParam(value = "gender") String gender,
          @RequestParam(value = "birthOfYear") String birthOfYear,
          @RequestParam(value = "birthOfMonth") String birthOfMonth,
          @RequestParam(value = "birthOfDay") String birthOfDay, @RequestParam(value = "sid") String sid,
          @RequestParam(value = "name") String name) throws URISyntaxException, IOException {
    if (gender.equals("") || birthOfYear.equals("") || birthOfMonth.equals("") || birthOfDay.equals("")
        || sid.equals("") || name.equals("")) {
      return "null-error";
    } else {
      PII pii = new PII.Builder(new Name(name.substring(1, 3), name.substring(0, 1)),
          gender.equals("M") ? Sex.MALE : Sex.FEMALE,
          new Birthday(Integer.valueOf(birthOfYear), Integer.valueOf(birthOfMonth), Integer.valueOf(birthOfDay)),
          new TWNationalId(sid)).build();

      List<SubprimeGuidRequest> sgrs = newArrayList();
      SubprimeGuidRequest sgr = new SubprimeGuidRequest();

      sgr.setGuidHash(pii.getHashcodes());
      sgr.setPrefix(userRepo.findByUsername(customAuthenticationProvider.getName()).getPrefix());
      sgrs.add(sgr);

      Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(HttpActionHelper
          .toPost(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL), Action.CREATE, sgrs, false).getBody());

      map.addAttribute("spguids", flattenJson.get("[0].spguid").toString());

      return "create-result";
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
      String register(ModelMap map, @RequestParam(value = "username") String username,
          @RequestParam(value = "password") String password, @RequestParam(value = "email") String email,
          @RequestParam(value = "jobTitle") String jobTitle, @RequestParam(value = "institute") String institute,
          @RequestParam(value = "telephone") String telephone, @RequestParam(value = "address") String address,
          @RequestParam(value = "prefix") String prefix, @RequestParam(value = "authority") String authority) {

    if (username.equals("") || password.equals("") || institute.equals("") || email.equals("") || prefix.equals("")) {
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
      user.setRole(authority.equals("ROLE_ADMIN") ? Role.ROLE_ADMIN : Role.ROLE_USER);

      userRepo.save(user);
      map.addAttribute("users", user);

      return "register-success";
    }
  }

  /**
   * 使用者名單
   * 
   * @return
   */

  @ResponseBody
  @RequestMapping(value = "/user", method = RequestMethod.GET)
      List<AccountUsersResponse> getCurrentUser() {

    return AccountUsersResponse.getResponse(userRepo.findAll());
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
  @RequestMapping(value = "/search/user", method = RequestMethod.GET)
      String searchUser(ModelMap map, @RequestParam(value = "username") String username,
          @RequestParam(value = "prefix") String prefix, @RequestParam(value = "institute") String institute) {

    Set<AccountUsers> aus = newHashSet();
    if (username.equals("") && prefix.equals("") && institute.equals("")) {
      return "null-error";
    } else {
      aus.add(userRepo.findByUsername(username));
      aus.addAll(userRepo.findByInstitute(institute));
      aus.addAll(userRepo.findByPrefix(prefix));
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
  @RequestMapping(value = "web/comparison", method = RequestMethod.POST)
      String webComparison(ModelMap map, @RequestParam(value = "subprimeGuids") String subprimeGuids)
          throws JsonProcessingException, URISyntaxException {
    List<String> list = newArrayList();
    String[] str = subprimeGuids.split(",");
    for (String s : str) {
      list.add(s);
    }
    map.addAttribute("result", HttpActionHelper
        .toPost(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL), Action.COMPARISON, list, false).getBody());

    return "comparison-result";
  }

  /**
   * 確認是否存在於 local server 資料庫
   * 
   * @param hashcode1
   * @param hashcode2
   * @param hashcode3
   * @return
   * @throws SQLException
   * @throws URISyntaxException
   */
  @RequestMapping(value = "/exist", method = RequestMethod.GET)
      boolean isExist(@RequestParam("hashcode1") String hashcode1, @RequestParam("hashcode2") String hashcode2,
          @RequestParam("hashcode3") String hashcode3) throws SQLException, URISyntaxException {

    boolean b = false;
    for (SubprimeGuid spguid : spguidRepo.findAll()) {
      if (spguid.getHashcode1().equals(hashcode1) && spguid.getHashcode2().equals(hashcode2)
          && spguid.getHashcode3().equals(hashcode3)) {
        b = true;
      } else {
        b = HttpActionHelper.toGet(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL), Action.EXIST,
            "?hashcode1=" + hashcode1 + "&hashcode2=" + hashcode2 + "&hashcode3=" + hashcode3, false).equals("true")
                ? true : false;
      }
    }

    return b;
  }

}