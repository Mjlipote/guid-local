/**
 *
 * @author Ming-Jheng Li
 *
 *
 *         Copyright 2015 Ming-Jheng Li
 *
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 *
 */
package tw.guid.local.controllers;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import tw.guid.local.helper.HashcodeCreator;
import tw.guid.local.models.Gender;
import tw.guid.local.models.entity.Association;
import tw.guid.local.models.repo.AccountUsersRepository;
import tw.guid.local.models.repo.AssociationRepository;

@RequestMapping("/association")
@Controller
public class WebAssociationController {

  @Autowired
  AssociationRepository associationRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;

  @RequestMapping("")
  String association(ModelMap map) {
    map.addAttribute("associationUsers", associationRepo.findAll());
    return "association";
  }

  @RequestMapping("/login")
  String associationLogin() {

    return "association-login";
  }

  @RequestMapping(value = "", method = RequestMethod.POST)
  String associationAuthentication(ModelMap map,
      @RequestParam(value = "username") String username,
      @RequestParam(value = "password") String password) {

    checkNotNull(username, "username can't be bull");
    checkNotNull(password, "password can't be bull");

    if (username.equals("") || password.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫必填資料，切勿留空值！！");
      map.addAttribute("link", "/association/login");
      return "error";
    } else if (!username.equals("super")) {
      map.addAttribute("errorMessage", "填寫的帳號有誤！！");
      map.addAttribute("link", "/association/login");
      return "error";
    } else if (username.equals("super") && !HashcodeCreator.getSha512(password)
        .equals(acctUserRepo.findByUsername(username).getPassword())) {
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

  }

  @RequestMapping(value = "/lookup", method = RequestMethod.GET)
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

  @RequestMapping(value = "/{spguid}", method = RequestMethod.PUT)
  String associationEdit(ModelMap map, @PathVariable("spguid") String spguid,
      @Param("hospital") String hospital, @Param("doctor") String doctor,
      @Param("telephone") String telephone, @Param("address") String address) {

    Association associationUser = associationRepo.findBySpguid(spguid);

    associationUser.setHospital(hospital);
    associationUser.setDoctor(doctor);
    associationUser.setTelephone(telephone);
    associationUser.setAddress(address);

    associationRepo.saveAndFlush(associationUser);
    map.addAttribute("successMessage",
        "已成功修改 " + associationUser.getName() + " 的個人資料");
    map.addAttribute("link", "/association");
    return "success";

  }

  @RequestMapping(value = "/{spguid}", method = RequestMethod.GET)
  String associationEdit(ModelMap map, @PathVariable("spguid") String spguid) {

    map.addAttribute("associationUserInfo",
        associationRepo.findBySpguid(spguid));

    return "association-info";

  }

}
