/**
 *
 * @author Wei-Ming Wu, Ming-Jheng Li
 *
 *
 * Copyright 2015 Wei-Ming Wu, Ming-Jheng Li
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

import static com.google.common.collect.Lists.newArrayList;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tw.guid.local.helper.Crc32HashcodeCreator;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.models.Action;
import tw.guid.local.models.CustomAuthenticationProvider;
import tw.guid.local.models.SubprimeGuidRequest;
import tw.guid.local.models.entity.AccountUsers;
import tw.guid.local.models.repo.AccountUsersRepository;

@RequestMapping("/guid")
@Controller
public class LegacyGuidClientController {

  @Autowired
  AccountUsersRepository acctUserRepo;

  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;

  private static final Logger log =
      LoggerFactory.getLogger(LegacyGuidClientController.class);

  @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
  @ResponseBody
  String authenticate(HttpServletRequest request) {

    return new Gson().toJson(isValidate(request), Boolean.class);
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @ResponseBody
  String create(@RequestParam("prefix") String prefix,
      @RequestParam("hashes") String jsonHashes, HttpServletRequest request)
          throws URISyntaxException, FileNotFoundException, IOException {

    if (prefix.equals("") && isValidate(request) == true) {
      prefix = getAccountUsers(request).getPrefix();
    } else if (prefix.equals("")) {
      prefix = "PSEUDO";
    }

    List<SubprimeGuidRequest> sgrs = buildRequests(prefix, jsonHashes);

    Properties prop = new Properties();
    prop.load(new FileInputStream("serverhost.properties"));

    Map<String, Object> flattenJson;

    flattenJson = JsonFlattener.flattenAsMap(
        HttpActionHelper.toPost(new URI(prop.getProperty("central_server_url")),
            Action.CREATE, sgrs, false).getBody());

    if (sgrs.size() == 1) {
      return "[" + flattenJson.get("[0].spguid").toString() + "]";
    } else {
      List<String> guids = newArrayList();
      for (String key : flattenJson.keySet()) {
        if (key.matches("^\\[\\d+\\]\\.spguid$")) {
          guids.add(flattenJson.get(key).toString());
        }
      }
      return new Gson().toJson(guids);
    }
  }

  private List<SubprimeGuidRequest> buildRequests(String prefix,
      String jsonHashes) {
    List<SubprimeGuidRequest> sgrs = newArrayList();

    List<String> hash = null;
    List<List<String>> hashes = null;

    try {
      hash = new Gson().fromJson(jsonHashes,
          new TypeToken<List<String>>() {}.getType());
    } catch (Exception e) {
      hashes = new Gson().fromJson(jsonHashes,
          new TypeToken<List<List<String>>>() {}.getType());
    }

    if (hash != null) {
      sgrs.add(new SubprimeGuidRequest(prefix, hash));
    }

    if (hashes != null) {
      for (List<String> h : hashes) {
        sgrs.add(new SubprimeGuidRequest(prefix, h));
      }
    }

    return sgrs;
  }

  private AccountUsers getAccountUsers(HttpServletRequest request) {
    String base64Credentials = request.getHeader("Authorization");
    String credentials = new String(BaseEncoding.base64()
        .decode(base64Credentials.replaceFirst("^Basic\\s+", "")));

    final String[] values = credentials.split(":", 2);

    return acctUserRepo.findByUsernameAndPassword(values[0],
        Crc32HashcodeCreator.getCrc32(values[1]));
  }

  private boolean isValidate(HttpServletRequest request) {

    return getAccountUsers(request) != null ? true : false;
  }

}
