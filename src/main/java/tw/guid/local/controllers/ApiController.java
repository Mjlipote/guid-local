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

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.guid.local.config.RestfulConfig;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.models.AccountUsersResponse;
import tw.guid.local.models.Action;
import tw.guid.local.models.CustomAuthenticationProvider;
import tw.guid.local.models.repo.AccountUsersRepository;
import tw.guid.local.models.repo.ActionAuditRepository;
import tw.guid.local.models.repo.SubprimeGuidRepository;

@RequestMapping("/guid/api")
// @RestController
@Controller
public class ApiController {

  @Autowired
  ActionAuditRepository actionAuditRepo;
  @Autowired
  SubprimeGuidRepository spguidRepo;
  @Autowired
  AccountUsersRepository userRepo;
  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  /**
   * 使用者名單
   * 
   * @return
   */

  @ResponseBody
  @RequestMapping(value = "/users", method = RequestMethod.GET)
  List<AccountUsersResponse> users() {

    return AccountUsersResponse.getResponse(userRepo.findAll());
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
  boolean exist(@RequestParam("hashcode1") String hashcode1,
      @RequestParam("hashcode2") String hashcode2,
      @RequestParam("hashcode3") String hashcode3)
          throws SQLException, URISyntaxException {

    boolean b = false;

    if (spguidRepo.findByHashcode1AndHashcode2AndHashcode3(hashcode1, hashcode2,
        hashcode3) != null) {
      b = true;
    } else {
      b = HttpActionHelper
          .toGet(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL), Action.EXIST,
              "?hashcode1=" + hashcode1 + "&hashcode2=" + hashcode2
                  + "&hashcode3=" + hashcode3,
              false)
          .equals("true") ? true : false;
    }

    return b;
  }

}