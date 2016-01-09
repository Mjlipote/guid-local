/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Arrays;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.guid.local.entity.InstitutePrefix;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.AssociationRepository;
import tw.guid.local.repository.InstitutePrefixRepository;

@RequestMapping("/institutes")
@Controller
public class WebInstitutePrefixController {

  @Autowired
  AssociationRepository associationRepo;
  @Autowired
  InstitutePrefixRepository institutePrefixRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;

  @RequestMapping("")
  String institutesList(ModelMap map) {
    map.addAttribute("institutePrefixs", institutePrefixRepo.findAll());
    map.addAttribute("allPrefixs", institutePrefixRepo.getAllPrefix());
    map.addAttribute("allInstitutes", institutePrefixRepo.getAllInstitute());
    return "institute-prefix";
  }

  @RequestMapping(method = POST)
  String institutesNew(ModelMap map,
      @RequestParam(value = "institute") String institute,
      @RequestParam(value = "prefix") String prefix) {
    if (institute.equals("") || prefix.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫資料，切勿留空值！！");
      map.addAttribute("link", "/institutes");
      return "error";
    } else {
      if (institutePrefixRepo.findByInstitute(institute) != null) {
        map.addAttribute("errorMessage", "單位名稱已建立！！");
        map.addAttribute("link", "/institutes");
        return "error";
      } else {
        InstitutePrefix institutePrefix = new InstitutePrefix();
        institutePrefix.setPrefix(prefix);
        institutePrefix.setInstitute(institute);
        institutePrefixRepo.save(institutePrefix);

        return "redirect:/institutes";
      }
    }
  }

  @RequestMapping(value = "/{institute}", method = PUT)
  String institutesEdit(ModelMap map,
      @PathVariable("institute") String institute,
      @Param("newInstitute") String newInstitute,
      @Param("prefix") String prefix) {

    InstitutePrefix institutePrefix =
        institutePrefixRepo.findByInstitute(institute);

    institutePrefix.setInstitute(newInstitute);
    institutePrefix.setPrefix(prefix);

    institutePrefixRepo.saveAndFlush(institutePrefix);

    return "redirect:/institutes";
  }

  @RequestMapping(value = "/{institute}", method = GET)
  String institutesEdit(ModelMap map,
      @PathVariable("institute") String institute) {
    map.addAttribute("institutePrefixInfo",
        institutePrefixRepo.findByInstitute(institute));

    return "institute-prefix-info";
  }

  @RequestMapping(value = "/users", method = POST)
  String institutesNewUsers(ModelMap map,
      @RequestParam(value = "institute") String institute) {
    map.addAttribute("institutes", Arrays
        .asList(institutePrefixRepo.findByInstitute(institute).getInstitute()));

    return "register";
  }

  @RequestMapping(value = "/users/{institute}", method = GET)
  String institutesViewUsers(ModelMap map,
      @PathVariable("institute") String institute) {
    map.addAttribute("institute", institute);
    map.addAttribute("prefix",
        institutePrefixRepo.findByInstitute(institute).getPrefix());
    map.addAttribute("users",
        institutePrefixRepo.findByInstitute(institute).getAccountUsers());

    return "institute-prefix-view-users";
  }

  @RequestMapping(value = "/lookup", method = GET)
  String associationLookup(ModelMap map, @Param("prefix") String prefix,
      @Param("institute") String institute) {
    List<InstitutePrefix> institutePrefixs =
        institutePrefixRepo.findAll((root, query, cb) -> {
          Predicate finalPredicate = cb.and();

          if (prefix != null && !prefix.equals("")) {
            finalPredicate =
                cb.and(finalPredicate, cb.equal(root.get("prefix"), prefix));
          }
          if (institute != null && !institute.equals("")) {
            finalPredicate = cb.and(finalPredicate,
                cb.equal(root.get("institute"), institute));
          }

          return finalPredicate;
        });

    map.addAttribute("allPrefixs", institutePrefixRepo.getAllPrefix());
    map.addAttribute("allInstitutes", institutePrefixRepo.getAllInstitute());
    map.addAttribute("institutePrefixs", institutePrefixs);

    return "institute-prefix";
  }

}
