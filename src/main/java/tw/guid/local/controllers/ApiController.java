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

import static com.google.common.collect.Sets.newHashSet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.models.Action;
import tw.guid.local.models.RestfulAudit;
import tw.guid.local.models.entity.AccountUsers;
import tw.guid.local.models.repo.AccountUsersRepository;
import tw.guid.local.models.repo.SubprimeGuidRepository;

@RequestMapping("/guid/api")
@RestController

public class ApiController {

  private static final Logger log =
      LoggerFactory.getLogger(ApiController.class);

  @Autowired
  RestfulAudit restfulAudit;
  @Autowired
  SubprimeGuidRepository spguidRepo;
  @Autowired
  AccountUsersRepository accUserRepo;

  /**
   * Get all prefix List
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/prefix", method = RequestMethod.GET)
  Set<String> prefixLookup() {
    Set<String> prefixSet = newHashSet();

    for (AccountUsers acctUser : accUserRepo.findAll()) {
      prefixSet.add(acctUser.getPrefix());
    }
    return prefixSet;
  }

  @ResponseBody
  @RequestMapping(value = "/validate", method = RequestMethod.GET)
  boolean validate(@RequestParam(value = "spguid") String spguid)
      throws URISyntaxException {
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("serverhost.properties"));
    } catch (FileNotFoundException e) {
      log.error(e.getMessage(), e);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }

    return HttpActionHelper
        .toGet(new URI(prop.getProperty("central_server_url")), Action.VALIDATE,
            "?spguid=" + spguid, false)
        .getBody().equals("true") ? true : false;
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

    boolean b;

    if (spguidRepo.findByHashcode1AndHashcode2AndHashcode3(hashcode1, hashcode2,
        hashcode3).size() > 0) {
      b = true;
    } else {
      Properties prop = new Properties();
      try {
        prop.load(new FileInputStream("serverhost.properties"));
      } catch (FileNotFoundException e) {
        log.error(e.getMessage(), e);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
      b = HttpActionHelper
          .toGet(new URI(prop.getProperty("central_server_url")), Action.EXIST,
              "?hashcode1=" + hashcode1 + "&hashcode2=" + hashcode2
                  + "&hashcode3=" + hashcode3,
              false)
          .getBody().equals("true") ? true : false;
    }

    return b;
  }

}