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

import java.util.List;

import javax.persistence.criteria.Predicate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tw.guid.local.entity.Association;
import tw.guid.local.entity.Association.Gender;
import tw.guid.local.helper.HashcodeCreator;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.AssociationRepository;
import tw.guid.local.web.Role;

@RequestMapping("/association")
@Controller
public class WebAssociationController {

  @Autowired
  AssociationRepository associationRepo;

  @Autowired
  AccountUsersRepository acctUserRepo;

  @RequestMapping("")
  String associationList(ModelMap map) {
    map.addAttribute("associationUsers", associationRepo.findAll());
    map.addAttribute("hospitals", associationRepo.getAllHospital());
    map.addAttribute("doctors", associationRepo.getAllDoctor());
    return "association";
  }

  @RequestMapping("/login")
  String associationLogin() {
    return "association-login";
  }

  @RequestMapping(value = "", method = POST)
  String associationAuthentication(ModelMap map,
      @RequestParam(value = "username") @Valid @NotNull String username,
      @RequestParam(value = "password") @Valid @NotNull String password) {
    Authentication auth =
        SecurityContextHolder.getContext().getAuthentication();

    if (!auth.getName().equals("super")) {
      if (username.equals("") || password.equals("")) {
        map.addAttribute("errorMessage", "請確實填寫必填資料，切勿留空值！！");
        map.addAttribute("link", "/association/login");
        return "error";
      } else if (!username.equals("super")) {
        map.addAttribute("errorMessage", "填寫的帳號有誤！！");
        map.addAttribute("link", "/association/login");
        return "error";
      } else if (!HashcodeCreator.getSha512(password)
          .equals(acctUserRepo.findByUsername("super").getPassword())) {
        map.addAttribute("errorMessage", "填寫的密碼有誤！！");
        map.addAttribute("link", "/association/login");
        return "error";
      } else if (username.equals("super") && HashcodeCreator.getSha512(password)
          .equals(acctUserRepo.findByUsername(username).getPassword())) {
        return "redirect:/association";
      } else {
        map.addAttribute("errorMessage", "填寫的帳號密碼有誤！！");
        map.addAttribute("link", "/association/login");
        return "error";
      }
    } else {
      if (username.equals("") || password.equals("")) {
        map.addAttribute("errorMessage", "請確實填寫必填資料，切勿留空值！！");
        map.addAttribute("link", "/association/login");
        return "error";
      } else if (acctUserRepo.findByUsername(username) == null || !acctUserRepo
          .findByUsername(username).getRole().equals(Role.ROLE_ADMIN)) {
        map.addAttribute("errorMessage", "填寫的帳號有誤，或是權限不足！！");
        map.addAttribute("link", "/association/login");
        return "error";
      } else if (!HashcodeCreator.getSha512(password)
          .equals(acctUserRepo.findByUsername(username).getPassword())) {
        map.addAttribute("errorMessage", "填寫的密碼有誤！！");
        map.addAttribute("link", "/association/login");
        return "error";
      } else if (!username.equals("super")
          && HashcodeCreator.getSha512(password)
              .equals(acctUserRepo.findByUsername(username).getPassword())) {
        return "redirect:/association";
      } else {
        map.addAttribute("errorMessage", "填寫的帳號密碼有誤！！");
        map.addAttribute("link", "/association/login");
        return "error";
      }
    }

  }

  @RequestMapping(value = "/lookup", method = GET)
  String associationLookup(ModelMap map, @Param("spguid") String spguid,
      @Param("name") String name, @Param("subjectId") String subjectId,
      @Param("mrn") String mrn, @Param("gender") String gender,
      @Param("birthDay") String birthDay, @Param("sid") String sid,
      @Param("hospital") String hospital, @Param("doctor") String doctor,
      @Param("telephone") String telephone, @Param("address") String address) {
    List<Association> associationUsers =
        associationRepo.findAll((root, query, cb) -> {
          Predicate finalPredicate = cb.and();

          if (spguid != null && !spguid.equals("")) {
            finalPredicate =
                cb.and(finalPredicate, cb.equal(root.get("spguid"), spguid));
          }
          if (name != null && !name.equals("")) {
            finalPredicate =
                cb.and(finalPredicate, cb.equal(root.get("name"), name));
          }
          if (subjectId != null && !subjectId.equals("")) {
            finalPredicate = cb.and(finalPredicate,
                cb.equal(root.get("subjectId"), subjectId));
          }
          if (mrn != null && !mrn.equals("")) {
            finalPredicate =
                cb.and(finalPredicate, cb.equal(root.get("mrn"), mrn));
          }
          if (gender != null && !gender.equals("")) {
            finalPredicate = cb.and(finalPredicate, cb.equal(root.get("gender"),
                gender.equals("M") ? Gender.MALE : Gender.FEMALE));
          }
          if (birthDay != null && !birthDay.equals("")) {
            finalPredicate = cb.and(finalPredicate,
                cb.equal(root.get("birthOfYear"), birthDay.split("/")[0]));
          }
          if (birthDay != null && !birthDay.equals("")) {
            finalPredicate = cb.and(finalPredicate,
                cb.equal(root.get("birthOfMonth"), birthDay.split("/")[1]));
          }
          if (birthDay != null && !birthDay.equals("")) {
            finalPredicate = cb.and(finalPredicate,
                cb.equal(root.get("birthOfDay"), birthDay.split("/")[2]));
          }
          if (sid != null && !sid.equals("")) {
            finalPredicate =
                cb.and(finalPredicate, cb.equal(root.get("sid"), sid));
          }
          if (hospital != null && !hospital.equals("")) {
            finalPredicate = cb.and(finalPredicate,
                cb.equal(root.get("hospital"), hospital));
          }
          if (doctor != null && !doctor.equals("")) {
            finalPredicate =
                cb.and(finalPredicate, cb.equal(root.get("doctor"), doctor));
          }
          if (telephone != null && !telephone.equals("")) {
            finalPredicate = cb.and(finalPredicate,
                cb.equal(root.get("telephone"), telephone));
          }
          if (address != null && !address.equals("")) {
            finalPredicate =
                cb.and(finalPredicate, cb.equal(root.get("address"), address));
          }

          return finalPredicate;
        });
    map.addAttribute("hospitals", associationRepo.getAllHospital());
    map.addAttribute("doctors", associationRepo.getAllDoctor());
    map.addAttribute("associationUsers", associationUsers);

    return "association";
  }

  @RequestMapping(value = "/{spguid}", method = PUT)
  String associationEdit(ModelMap map, @PathVariable("spguid") String spguid,
      @Param("hospital") String hospital, @Param("doctor") String doctor,
      @Param("telephone") String telephone, @Param("address") String address) {
    Association associationUser = associationRepo.findBySpguid(spguid);

    associationUser.setHospital(hospital);
    associationUser.setDoctor(doctor);
    associationUser.setTelephone(telephone);
    associationUser.setAddress(address);

    associationRepo.saveAndFlush(associationUser);

    return "redirect:/association";
  }

  @RequestMapping(value = "/{spguid}", method = GET)
  String associationEdit(ModelMap map, @PathVariable("spguid") String spguid) {
    map.addAttribute("associationUserInfo",
        associationRepo.findBySpguid(spguid));

    return "association-info";
  }

}
