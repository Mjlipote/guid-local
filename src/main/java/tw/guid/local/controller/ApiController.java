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

import java.net.URISyntaxException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.AssociationRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.service.ApiService;

@RequestMapping("/guid/api")
@RestController
public class ApiController {

  @Autowired
  SubprimeGuidRepository spguidRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  AssociationRepository associationRepo;
  @Autowired
  ApiService apiService;
  @Autowired
  Environment env;

  @Value("${central_server_url}")
  String centralServerUrl;

  @ResponseBody
  @RequestMapping(value = "/prefix", method = RequestMethod.GET)
  Set<String> prefixLookup() {
    return apiService.prefixLookup();
  }

  @ResponseBody
  @RequestMapping(value = "/hospital", method = RequestMethod.GET)
  Set<String> hospitalLookup() {
    return apiService.hospitalLookup();
  }

  @ResponseBody
  @RequestMapping(value = "/doctor", method = RequestMethod.GET)
  Set<String> doctorLookup() {
    return apiService.doctorLookup();
  }

  @ResponseBody
  @RequestMapping(value = "/validation", method = RequestMethod.GET)
  boolean validation(@RequestParam(value = "spguid") String spguid)
      throws URISyntaxException {

    return apiService.validation(spguid);
  }

  @RequestMapping(value = "/existence", method = RequestMethod.GET)
  boolean existence(@RequestParam("hashcode1") String hashcode1,
      @RequestParam("hashcode2") String hashcode2,
      @RequestParam("hashcode3") String hashcode3) throws URISyntaxException {

    return apiService.existence(hashcode1, hashcode2, hashcode3);

  }

}