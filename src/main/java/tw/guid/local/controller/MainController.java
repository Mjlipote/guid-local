/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.controller;

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

  @RequestMapping("/home")
  String home() {
    return "home";
  }

  @RequestMapping("/register")
  String usersRegister(ModelMap map) {
    map.addAttribute("institutes", institutePrefixRepository.getAllInstitute());
    return "register";
  }

  @RequestMapping("/guids")
  String guidsNew() {
    return "guids";
  }

  @RequestMapping("/comparison")
  String guidsComparison() {
    return "comparison";
  }

  @RequestMapping("/batch/comparison")
  String guidsBatchComparison() {
    return "batch-comparison";
  }

  @RequestMapping("/guids/batch")
  String guidsBatchNew() {
    return "batch-guids";
  }

  @RequestMapping("/remove")
  String usersRemove() {
    return "users-remove";
  }

  @RequestMapping("/login")
  String login() {
    return "login";
  }

}
