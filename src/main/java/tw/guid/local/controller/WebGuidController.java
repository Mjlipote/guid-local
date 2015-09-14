/*
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
import static com.google.common.collect.Sets.newHashSet;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.guid.local.entity.Association;
import tw.guid.local.entity.Association.Gender;
import tw.guid.local.entity.SubprimeGuid;
import tw.guid.local.helper.BatchSubprimeGuidCreator;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.AssociationRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.util.NameSplitter;
import tw.guid.local.validateion.BirthdayValidator;
import tw.guid.local.web.Action;
import tw.guid.local.web.CustomAuthenticationProvider;
import tw.guid.local.web.SubprimeGuidRequest;
import wmw.validate.TWNationalIdValidator;

@RequestMapping("/guids")
@Controller
public class WebGuidController {

  private static final Logger log =
      LoggerFactory.getLogger(WebGuidController.class);

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

  @Value("${guid.central.server.url}")
  String centralServerUrl;

  /**
   * 網頁版批次產生 GUID
   * 
   * @param map
   * @param file
   * @return
   * @throws IOException
   * @throws InvalidFormatException
   */
  @RequestMapping(value = "/batch", method = RequestMethod.POST)
  String guidsBatchNew(ModelMap map, @RequestParam("file") MultipartFile file)
      throws OpenXML4JException, IOException {
    if (!file.getOriginalFilename().endsWith("xlsx")
        && !file.getOriginalFilename().endsWith("xls")) {
      map.addAttribute("errorMessage", "上傳檔案必須為 Excel (.xlsx 或 .xls)");
      map.addAttribute("link", "/guids/batch");
      return "error";
    } else {
      WorkbookReader reader = WorkbookReader.open(
          WorkbookFactory.create(new ByteArrayInputStream(file.getBytes())));

      if (!BatchSubprimeGuidCreator.isContainsItem(reader)) {
        map.addAttribute("errorMessage", "上傳檔案內的標頭格式有誤");
        map.addAttribute("link", "/guids/batch");
        return "error";
      }

      Authentication auth =
          SecurityContextHolder.getContext().getAuthentication();
      String prefix = acctUserRepo.findByUsername(auth.getName())
          .getInstitutePrefix().getPrefix();
      List<String> correctGuids = newArrayList();

      for (Map<String, String> row : reader.toMaps()) {
        if (BatchSubprimeGuidCreator.isEmptyOnEachRow(row)) {
          map.addAttribute("errorMessage", "填寫的欄位不可有空格");
          map.addAttribute("link", "/guids/batch");
          return "error";
        } else if (!BatchSubprimeGuidCreator.isValidateSocialId(row)) {
          map.addAttribute("errorMessage", "請填入合法的身份證字號");
          map.addAttribute("link", "/guids/batch");
          return "error";
        } else {
          PII pii = BatchSubprimeGuidCreator.rowToPII(row);
          String hashcode1 = pii.getHashcodes().get(0).substring(0, 128);
          String hashcode2 = pii.getHashcodes().get(1).substring(0, 128);
          String hashcode3 = pii.getHashcodes().get(2).substring(0, 128);

          SubprimeGuid sg =
              subprimeGuidRepo.findByHashcode1AndHashcode2AndHashcode3AndPrefix(
                  hashcode1, hashcode2, hashcode3, prefix);

          if (sg != null) {
            correctGuids.add(sg.getSpguid());
            Association existAssociation =
                associationRepo.findBySpguid(sg.getSpguid());
            existAssociation.setMrn(row.get("MRN"));
            existAssociation.setHospital(row.get("HP"));
            existAssociation.setDoctor(row.get("Dr"));
            associationRepo.saveAndFlush(existAssociation);
          } else {

            List<SubprimeGuidRequest> sgrs = newArrayList();
            SubprimeGuidRequest sgr = new SubprimeGuidRequest();

            sgr.setGuidHash(Arrays.asList(hashcode1, hashcode2, hashcode3));
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

            correctGuids.add(flattenJson.get("[0].spguid").toString());

            SubprimeGuid spGuid = new SubprimeGuid();
            spGuid.setSpguid(flattenJson.get("[0].spguid").toString());
            spGuid.setHashcode1(hashcode1);
            spGuid.setHashcode2(hashcode2);
            spGuid.setHashcode3(hashcode3);
            spGuid.setPrefix(prefix);
            subprimeGuidRepo.save(spGuid);

            Association association = new Association();
            association.setSpguid(flattenJson.get("[0].spguid").toString());
            association.setSubjectId(row.get("SUBJECTID"));
            association.setMrn(row.get("MRN"));
            association.setName(row.get("FULLNAME"));
            association.setSid(row.get("GIID"));
            association.setBirthOfYear(Integer.valueOf(row.get("YOB")));
            association.setBirthOfMonth(Integer.valueOf(row.get("MOB")));
            association.setBirthOfDay(Integer.valueOf(row.get("DOB")));
            association.setGender(
                row.get("SEX").equals("M") ? Gender.MALE : Gender.FEMALE);
            association.setHospital(row.get("HP"));
            association.setDoctor(row.get("Dr"));
            associationRepo.save(association);
          }
        }
      }

      map.addAttribute("number", correctGuids.size());
      map.addAttribute("spguids", correctGuids);
      return "batch-guids";
    }
  }

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
    } else if (!TWNationalIdValidator.validate(sid)) {
      map.addAttribute("errorMessage", "填寫的身分證字號有誤！！");
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

        String hashcode1 = pii.getHashcodes().get(0).substring(0, 128);
        String hashcode2 = pii.getHashcodes().get(1).substring(0, 128);
        String hashcode3 = pii.getHashcodes().get(2).substring(0, 128);

        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();

        String prefix = acctUserRepo.findByUsername(auth.getName())
            .getInstitutePrefix().getPrefix();
        SubprimeGuid sg =
            subprimeGuidRepo.findByHashcode1AndHashcode2AndHashcode3AndPrefix(
                hashcode1, hashcode2, hashcode3, prefix);

        if (sg != null) {
          map.addAttribute("spguids", sg.getSpguid());
          Association existAssociation =
              associationRepo.findBySpguid(sg.getSpguid());
          existAssociation.setSubjectId(subjectId);
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

          sgr.setGuidHash(Arrays.asList(hashcode1, hashcode2, hashcode3));
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
          spGuid.setHashcode1(hashcode1);
          spGuid.setHashcode2(hashcode2);
          spGuid.setHashcode3(hashcode3);
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

      WorkbookReader reader =
          WorkbookReader.open(new ByteArrayInputStream(file.getBytes()));

      List<String> list = newArrayList();

      for (List<String> str : reader.withoutHeader().toLists()) {
        if (!str.contains("")) {
          list.addAll(str);
        } else {
          map.addAttribute("errorMessage", "上傳檔案內容欄位不可空白");
          map.addAttribute("link", "/batch/comparison");
          return "error";
        }
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

  @RequestMapping(value = "/repeat", method = RequestMethod.GET)
  String guidsRepeat(ModelMap map) {

    Set<Set<String>> hhs = newHashSet();
    SetMultimap<List<String>, String> multimap = HashMultimap.create();

    for (SubprimeGuid subprimeGuid : subprimeGuidRepo.findAll()) {
      multimap.put(
          Arrays.asList(subprimeGuid.getHashcode1(),
              subprimeGuid.getHashcode2(), subprimeGuid.getHashcode3()),
          subprimeGuid.getSpguid());
    }

    for (List<String> key : multimap.keySet()) {
      Set<String> values = multimap.get(key);

      if (key != null && values.size() > 1) {
        hhs.add(values);
      }
    }

    map.addAttribute("result", hhs);
    map.addAttribute("number", hhs.size());

    return "repeat";
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