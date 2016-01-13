/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.InstitutePrefixRepository;
import tw.guid.local.repository.PaperTrailRepository;

@Controller
public class MainController {

  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  InstitutePrefixRepository institutePrefixRepository;
  @Autowired
  PaperTrailRepository paperTrailRepo;

  @RequestMapping(value = "/home", method = GET)
  String home() {
    return "home";
  }

  @RequestMapping(value = "/register", method = GET)
  String usersRegister(ModelMap map) {
    map.addAttribute("institutes", institutePrefixRepository.getAllInstitute());
    return "register";
  }

  @RequestMapping(value = "/guids", method = GET)
  String guidsNew() {
    return "guids";
  }

  @RequestMapping(value = "/comparison", method = GET)
  String guidsComparison() {
    return "comparison";
  }

  @RequestMapping(value = "/batch/comparison", method = GET)
  String guidsBatchComparison() {
    return "batch-comparison";
  }

  @RequestMapping(value = "/guids/batch", method = GET)
  String guidsBatchNew() {
    return "batch-guids";
  }

  @RequestMapping(value = "/remove", method = GET)
  String usersRemove() {
    return "users-remove";
  }

  @RequestMapping(value = "/login", method = GET)
  String login() {
    return "login";
  }

  @RequestMapping(value = "/lineChart", method = GET)
  String lineChart() {
    return "google-line-chart";
  }

}
