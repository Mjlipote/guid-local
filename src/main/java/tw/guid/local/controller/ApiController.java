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
package tw.guid.local.controller;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.model.Action;
import tw.guid.local.model.RestfulAudit;
import tw.guid.local.model.repo.AccountUsersRepository;
import tw.guid.local.model.repo.AssociationRepository;
import tw.guid.local.model.repo.SubprimeGuidRepository;

@RequestMapping("/guid/api")
@RestController
public class ApiController {

  @Autowired
  RestfulAudit restfulAudit;
  @Autowired
  SubprimeGuidRepository spguidRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  AssociationRepository associationRepo;
  @Autowired
  Environment env;

  @Value("${central_server_url}")
  String centralServerUrl;

  /**
   * Get all prefix List
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/prefix", method = RequestMethod.GET)
  Set<String> prefixLookup() {
    return acctUserRepo.getAllPrefix();
  }

  @ResponseBody
  @RequestMapping(value = "/hospital", method = RequestMethod.GET)
  Set<String> hospitalLookup() {
    return associationRepo.getAllHospital();
  }

  @ResponseBody
  @RequestMapping(value = "/doctor", method = RequestMethod.GET)
  Set<String> doctorLookup() {
    return associationRepo.getAllDoctor();
  }

  @ResponseBody
  @RequestMapping(value = "/validation", method = RequestMethod.GET)
  boolean validation(@RequestParam(value = "spguid") String spguid)
      throws URISyntaxException {

    return HttpActionHelper.toGet(new URI(centralServerUrl), Action.VALIDATION,
        "?spguid=" + spguid, false).getBody().equals("true") ? true : false;
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
  @RequestMapping(value = "/existence", method = RequestMethod.GET)
  boolean existence(@RequestParam("hashcode1") String hashcode1,
      @RequestParam("hashcode2") String hashcode2,
      @RequestParam("hashcode3") String hashcode3)
          throws SQLException, URISyntaxException {

    checkNotNull(hashcode1, "hashcode1 can't be null");
    checkNotNull(hashcode2, "hashcode2 can't be null");
    checkNotNull(hashcode3, "hashcode3 can't be null");

    if (spguidRepo.findByHashcode1AndHashcode2AndHashcode3(hashcode1, hashcode2,
        hashcode3).size() > 0) {
      return true;
    } else {
      return HttpActionHelper.toGet(new URI(centralServerUrl),
          Action.EXISTENCE, "?hashcode1=" + hashcode1 + "&hashcode2="
              + hashcode2 + "&hashcode3=" + hashcode3,
          false).getBody().equals("true") ? true : false;
    }

  }

}