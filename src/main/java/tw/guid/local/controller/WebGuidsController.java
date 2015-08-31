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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.workbookaccessor.WorkbookReader;

import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.model.Action;
import tw.guid.local.model.CustomAuthenticationProvider;
import tw.guid.local.model.Gender;
import tw.guid.local.model.RestfulAudit;
import tw.guid.local.model.SubprimeGuidRequest;
import tw.guid.local.model.entity.Association;
import tw.guid.local.model.entity.SubprimeGuid;
import tw.guid.local.model.repo.AccountUsersRepository;
import tw.guid.local.model.repo.AssociationRepository;
import tw.guid.local.model.repo.SubprimeGuidRepository;
import tw.guid.local.util.NameSplitter;
import tw.guid.local.validateion.BirthdayValidator;

@RequestMapping("/guids")
@Controller
public class WebGuidsController {

  private static final Logger log =
      LoggerFactory.getLogger(WebGuidsController.class);

  @Autowired
  RestfulAudit restfulAudit;
  @Autowired
  SubprimeGuidRepository subprimeGuidRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  CustomAuthenticationProvider customAuthenticationProvider;
  @Autowired
  AssociationRepository associationRepo;
  @Autowired
  Environment env;

  @Value("${central_server_url}")
  String centralServerUrl;

  /**
   * 網頁版產生 GUID
   * 
   * @param map
   * @param gender
   * @param boy
   * @param bom
   * @param bod
   * @param sid
   * @param name
   * @return
   * @throws FileNotFoundException
   * @throws URISyntaxException
   * @throws IOException
   */
  @RequestMapping(method = RequestMethod.POST)
  String guidsNew(ModelMap map, @RequestParam(value = "gender") String gender,
      @RequestParam(value = "birthDay") String birthDay,
      @RequestParam(value = "sid") String sid,
      @RequestParam(value = "name") String name,
      @RequestParam(value = "subjectId") String subjectId,
      @RequestParam(value = "mrn") String mrn,
      @RequestParam(value = "hospital") String hospital,
      @RequestParam(value = "doctor") String doctor,
      @RequestParam(value = "telephone") String telephone,
      @RequestParam(value = "address") String address) {

    checkNotNull(gender, "gender can't be null");
    checkNotNull(birthDay, "birthDay can't be null");
    checkNotNull(sid, "sid can't be null");
    checkNotNull(name, "name can't be null");
    checkNotNull(subjectId, "subjectId can't be null");
    checkNotNull(mrn, "mrn can't be null");

    if (birthDay.equals("") || gender.equals("") || sid.equals("")
        || name.equals("") || subjectId.equals("") || mrn.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫必填資料，切勿留空值！！");
      map.addAttribute("link", "/guids");
      return "error";
    } else {
      String[] birthday = birthDay.split("/");
      int birthOfYear = Integer.valueOf(birthday[0]);
      int birthOfMonth = Integer.valueOf(birthday[1]);
      int birthOfDay = Integer.valueOf(birthday[2]);
      BirthdayValidator birthdayValidator =
          new BirthdayValidator(birthOfYear, birthOfMonth, birthOfDay);

      if (!birthdayValidator.isValidate()) {
        map.addAttribute("errorMessage", birthdayValidator.getMeg());
        map.addAttribute("link", "/guids");
        return "error";
      } else {
        PII pii = new PII.Builder(NameSplitter.split(name),
            gender.equals("M") ? Sex.MALE : Sex.FEMALE,
            new Birthday(birthOfYear, birthOfMonth, birthOfDay),
            new TWNationalId(sid)).build();

        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();

        String prefix = acctUserRepo.findByUsername(auth.getName()).getPrefix();
        SubprimeGuid sg =
            subprimeGuidRepo.findByHashcode1AndHashcode2AndHashcode3AndPrefix(
                pii.getHashcodes().get(0), pii.getHashcodes().get(1),
                pii.getHashcodes().get(2), prefix);

        if (sg != null) {
          map.addAttribute("spguids", "(REPEAT): " + sg.getSpguid());
          Association existAssociation =
              associationRepo.findBySpguid(sg.getSpguid());
          existAssociation.setMrn(mrn);
          existAssociation.setHospital(hospital);
          existAssociation.setDoctor(doctor);
          existAssociation.setTelephone(telephone);
          existAssociation.setAddress(address);
          associationRepo.saveAndFlush(existAssociation);

          return "guids-result";
        } else {

          List<SubprimeGuidRequest> sgrs = newArrayList();
          SubprimeGuidRequest sgr = new SubprimeGuidRequest();

          sgr.setGuidHash(pii.getHashcodes());
          sgr.setPrefix(prefix);
          sgrs.add(sgr);

          Map<String, Object> flattenJson = null;
          try {
            flattenJson = JsonFlattener.flattenAsMap(HttpActionHelper
                .toPost(new URI(centralServerUrl), Action.NEW, sgrs, false)
                .getBody());
          } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
          } catch (URISyntaxException e) {
            log.error(e.getMessage(), e);
          }

          map.addAttribute("spguids", flattenJson.get("[0].spguid").toString());

          SubprimeGuid spGuid = new SubprimeGuid();
          spGuid.setSpguid(flattenJson.get("[0].spguid").toString());
          spGuid.setHashcode1(pii.getHashcodes().get(0));
          spGuid.setHashcode2(pii.getHashcodes().get(1));
          spGuid.setHashcode3(pii.getHashcodes().get(2));
          spGuid.setPrefix(prefix);
          subprimeGuidRepo.save(spGuid);

          Association association = new Association();
          association.setSpguid(flattenJson.get("[0].spguid").toString());
          association.setSubjectId(subjectId);
          association.setMrn(mrn);
          association.setName(name);
          association.setSid(sid);
          association.setBirthOfYear(Integer.valueOf(birthday[0]));
          association.setBirthOfMonth(Integer.valueOf(birthday[1]));
          association.setBirthOfDay(Integer.valueOf(birthday[2]));
          association
              .setGender(gender.equals("M") ? Gender.MALE : Gender.FEMALE);
          association.setHospital(hospital);
          association.setDoctor(doctor);
          association.setTelephone(telephone);
          association.setAddress(address);
          associationRepo.save(association);

          return "guids-result";
        }
      }
    }
  }

  @RequestMapping(value = "/batch/comparison", method = RequestMethod.POST)
  String guidsBatchComparison(ModelMap map,
      @RequestParam("file") MultipartFile file)
          throws IOException, OpenXML4JException, URISyntaxException {

    if (!file.getOriginalFilename().endsWith("xlsx")
        && !file.getOriginalFilename().endsWith("xls")) {
      map.addAttribute("errorMessage", "上傳檔案必須為 Excel (.xlsx 或 .xls)");
      map.addAttribute("link", "/batch/comparison");
      return "error";
    } else {

      WorkbookReader reader = WorkbookReader.open(
          WorkbookFactory.create(new ByteArrayInputStream(file.getBytes())));

      List<String> list = newArrayList();

      for (List<String> str : reader.withoutHeader().toLists()) {
        list.addAll(str);
      }

      if (!isValidateLength(list)) {
        map.addAttribute("errorMessage", "填寫值不是 GUID 的標準格式，請您再次確認！！");
        map.addAttribute("link", "/batch/comparison");
        return "error";
      } else {
        Map<String, Object> flattenJson = JsonFlattener
            .flattenAsMap(HttpActionHelper.toPost(new URI(centralServerUrl),
                Action.COMPARISON, list, false).getBody());

        List<List<String>> lls = newArrayList();

        for (int i = 0; i < flattenJson.size(); i++) {
          List<String> ls = newArrayList();
          for (int j = 0; j < flattenJson.size(); j++) {
            if (flattenJson.get("[" + i + "]" + "[" + j + "]") != null) {
              ls.add(flattenJson.get("[" + i + "]" + "[" + j + "]").toString());
            }
          }
          if (ls.size() > 0) lls.add(ls);
        }
        map.addAttribute("result", lls);
        map.addAttribute("number", lls.size());
        return "batch-comparison";
      }
    }
  }

  /**
   * 在 Web 進行二次編碼比對
   * 
   * @param map
   * @param subprimeGuids
   * @return
   * @throws IOException
   * @throws URISyntaxException
   */
  @RequestMapping(value = "/comparison", method = RequestMethod.POST)
  String guidsComparison(ModelMap map,
      @RequestParam(value = "subprimeGuids") String subprimeGuids)
          throws IOException, URISyntaxException {

    if (subprimeGuids.equals("")) {
      map.addAttribute("errorMessage", "請確實填寫資料，切勿留空值！！");
      map.addAttribute("link", "/comparison");
      return "error";
    } else if (!subprimeGuids.trim().contains(",")) {
      map.addAttribute("errorMessage", "請遵照格式填寫 (需填入 \",\" 做為區隔)");
      map.addAttribute("link", "/comparison");
      return "error";
    } else if (!isValidateLength(subprimeGuids)) {
      map.addAttribute("errorMessage", "填寫值不是 GUID 的標準格式，請您再次確認！！");
      map.addAttribute("link", "/comparison");
      return "error";
    } else {
      List<String> list = newArrayList();
      String[] str = subprimeGuids.trim().split(",");

      for (String s : str) {
        list.add(s);
      }

      Map<String, Object> flattenJson =
          JsonFlattener.flattenAsMap(HttpActionHelper
              .toPost(new URI(centralServerUrl), Action.COMPARISON, list, false)
              .getBody());

      List<List<String>> lls = newArrayList();

      for (int i = 0; i < flattenJson.size(); i++) {
        List<String> ls = newArrayList();
        for (int j = 0; j < flattenJson.size(); j++) {
          if (flattenJson.get("[" + i + "]" + "[" + j + "]") != null) {
            ls.add(flattenJson.get("[" + i + "]" + "[" + j + "]").toString());
          }
        }
        if (ls.size() > 0) lls.add(ls);
      }
      map.addAttribute("result", lls);
      map.addAttribute("number", lls.size());

      return "comparison";
    }
  }

  private boolean isValidateLength(List<String> spguids) {
    for (String s : spguids) {
      if (!s.contains("-")) {
        return false;
      } else if (s.toUpperCase().split("-")[1].length() != 8) {
        return false;
      } else {
        return true;
      }
    }
    return true;
  }

  private boolean isValidateLength(String spguid) {
    String[] str = spguid.split(",");
    for (String s : str) {
      if (!s.contains("-")) {
        return false;
      } else if (s.toUpperCase().split("-")[1].length() != 8) {
        return false;
      } else {
        return true;
      }
    }
    return true;
  }

}