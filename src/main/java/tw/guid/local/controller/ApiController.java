/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.controller;

import java.net.URISyntaxException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.AssociationRepository;
import tw.guid.local.repository.InstitutePrefixRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.service.ApiService;

@RequestMapping("/guids/api")
@RestController
public class ApiController {

  @Autowired
  SubprimeGuidRepository spguidRepo;

  @Autowired
  AccountUsersRepository acctUserRepo;

  @Autowired
  AssociationRepository associationRepo;

  @Autowired
  InstitutePrefixRepository institutePrefixRepo;

  @Autowired
  ApiService apiService;

  @Autowired
  Environment env;

  @ResponseBody
  @RequestMapping("/prefix")
  Set<String> prefixLookup() {
    return apiService.prefixLookup();
  }

  @ResponseBody
  @RequestMapping("/hospital")
  Set<String> hospitalLookup() {
    return apiService.hospitalLookup();
  }

  @ResponseBody
  @RequestMapping("/doctor")
  Set<String> doctorLookup() {
    return apiService.doctorLookup();
  }

  @ResponseBody
  @RequestMapping("/validation")
  boolean validation(@RequestParam("spguid") String spguid)
      throws URISyntaxException {
    return apiService.validation(spguid);
  }

  @RequestMapping("/existence")
  boolean existence(@RequestParam("subprimeGuid") String subprimeGuid) {
    return apiService.existence(subprimeGuid);
  }

}