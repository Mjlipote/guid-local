/**
 *
 * @author Ming-Jheng Li
 *
 *
 * Copyright 2014 Ming-Jheng Li
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.edu.ym.guid.client.GuidClient;
import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Name;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.edu.ym.lab525.web.guidlocalserver.models.entity.AccountUsers;
import tw.edu.ym.lab525.web.guidlocalserver.models.repo.AccountUsersRepository;
import tw.edu.ym.lab525.web.guidlocalserver.models.repo.ActionAuditRepository;
import tw.edu.ym.lab525.web.guidlocalserver.models.repo.SPGuidRepository;

@RequestMapping("/guid")
// @RestController
@Controller
public class MainController {

  @Autowired
  ActionAuditRepository actionAuditRepo;
  @Autowired
  SPGuidRepository spguidRepo;
  @Autowired
  AccountUsersRepository userRepo;

  // @RequestMapping(value = "/create", method = RequestMethod.POST)
  // String create(@RequestBody ) {
  // List<SPGuidCreateRequest> spGuidCreateRequestList = newArrayList();
  // RestTemplate restTemplate = new TestRestTemplate();
  // ObjectMapper mapper = new ObjectMapper();
  // HttpEntity<String> jsonRequest =
  // new HttpEntity<String>(mapper.writeValueAsString(spGuidCreateRequestList),
  // headers);
  // ResponseEntity<String> res =
  // restTemplate.postForEntity("http://localhost:8080/guid/create",
  // jsonRequest, String.class);
  //
  // return "spguids";
  // }

  @RequestMapping(value = "/web/create", method = RequestMethod.POST)
      String webcreate(ModelMap map, @RequestParam(value = "Gender") String gender,
          @RequestParam(value = "BOY") String boy, @RequestParam(value = "BOM") String bom,
          @RequestParam(value = "BOD") String bod, @RequestParam(value = "SID") String sid,
          @RequestParam(value = "Name") String name) throws URISyntaxException, IOException {

    PII pii = new PII.Builder(new Name(name.substring(1, 3), name.substring(0, 1)),
        gender.equals("M") ? Sex.MALE : Sex.FEMALE,
        new Birthday(Integer.valueOf(boy), Integer.valueOf(bom), Integer.valueOf(bod)), new TWNationalId(sid)).build();
    GuidClient gc = new GuidClient(new URI("https://120.126.47.138:8443"), "guid1", "12345", "TEST");

    map.addAttribute("spguids", gc.create(pii));

    return "spguids";
  }

  @RequestMapping(value = "/user", method = RequestMethod.POST)
      String addUser(ModelMap map, @RequestParam(value = "username") String username,
          @RequestParam(value = "password") String password, @RequestParam(value = "email") String email,
          @RequestParam(value = "jobTitle") String jobTitle, @RequestParam(value = "institute") String institute,
          @RequestParam(value = "telephone") String telephone, @RequestParam(value = "address") String address,
          @RequestParam(value = "prefix") String prefix) {

    AccountUsers user = new AccountUsers();
    user.setUsername(checkNotNull(username));
    user.setPassword(checkNotNull(password));
    user.setInstitute(checkNotNull(institute));
    user.setEmail(checkNotNull(email));
    user.setPrefix(checkNotNull(prefix));
    user.setTelephone(telephone);
    user.setJobTitle(jobTitle);
    user.setAddress(address);
    userRepo.save(user);
    map.addAttribute("users", user);

    return "users";
  }

  @ResponseBody
  @RequestMapping(value = "/user", method = RequestMethod.GET)
      List<AccountUsers> user() {

    return userRepo.findAll();
  }

}