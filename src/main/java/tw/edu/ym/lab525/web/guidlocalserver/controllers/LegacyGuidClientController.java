/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2015 Wei-Ming Wu
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

import static com.google.common.collect.Lists.newArrayList;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tw.edu.ym.lab525.web.guidlocalserver.config.RestfulConfig;
import tw.edu.ym.lab525.web.guidlocalserver.helper.HttpActionHelper;
import tw.edu.ym.lab525.web.guidlocalserver.models.Action;
import tw.edu.ym.lab525.web.guidlocalserver.models.SubprimeGuidRequest;
import tw.edu.ym.lab525.web.guidlocalserver.models.entity.AccountUsers;
import tw.edu.ym.lab525.web.guidlocalserver.models.repo.AccountUsersRepository;

@RequestMapping("/guid/client")
@Controller
public class LegacyGuidClientController {

  @Autowired
  AccountUsersRepository acctUserRepo;

  @RequestMapping("/authenticate")
  @ResponseBody
  String authenticate(HttpRequest httpRequest) {
    HttpHeaders headers = httpRequest.getHeaders();
    String base64Credentials = headers.getFirst("Authorization");
    String credentials = new String(Base64.decode(base64Credentials.getBytes()),
        Charset.forName("UTF-8"));

    final String[] values = credentials.split(":", 2);
    AccountUsers acctUser =
        acctUserRepo.findByUsernameAndPassword(values[0], values[1]);

    return new Gson().toJson(acctUser != null, Boolean.class);
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @ResponseBody
  String create(@RequestParam("prefix") String prefix,
      @RequestParam("jsonHashes") String jsonHashes)
          throws JsonProcessingException, URISyntaxException {
    List<SubprimeGuidRequest> sgrs = buildRequests(prefix, jsonHashes);

    Map<String, Object> flattenJson = JsonFlattener.flattenAsMap(
        HttpActionHelper.toPost(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL),
            Action.CREATE, sgrs, false).getBody());

    if (sgrs.size() == 1) {
      return flattenJson.get("[0].spguid").toString();
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

}
