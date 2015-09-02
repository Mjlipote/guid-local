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
package tw.guid.local.controller;

import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tw.guid.local.entity.PaperTrail;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.PaperTrailRepository;

@RequestMapping("/trail")
@Controller
public class PaperTrailController {

  @Autowired
  AccountUsersRepository acctUserRepo;

  @Autowired
  PaperTrailRepository paperTrailRepo;

  @RequestMapping(method = RequestMethod.GET)
  String trail(ModelMap map) {
    map.addAttribute("remoteAddrs", paperTrailRepo.getAllRemoteAddr());
    map.addAttribute("userIds", paperTrailRepo.getAllUserId());
    map.addAttribute("requestURIs", paperTrailRepo.getAllRequestURI());
    map.addAttribute("httpStatuses", paperTrailRepo.getAllHttpStatus());
    map.addAttribute("paperTrails", paperTrailRepo.findAll());
    return "trail";
  }

  @RequestMapping(value = "/lookup", method = RequestMethod.GET)
  String usersLookup(ModelMap map, @Param("userId") String userId,
      @Param("remoteAddr") String remoteAddr,
      @Param("requestUri") String requestUri,
      @Param("httpStatus") String httpStatus) {

    List<PaperTrail> paperTrails = paperTrailRepo.findAll((root, query, cb) -> {
      Predicate finalPredicate = cb.and();

      if (userId != null && !userId.equals("")) {
        finalPredicate =
            cb.and(finalPredicate, cb.equal(root.get("userId"), userId));
      }
      if (remoteAddr != null && !remoteAddr.equals("")) {
        finalPredicate = cb.and(finalPredicate,
            cb.equal(root.get("remoteAddr"), remoteAddr));
      }
      if (requestUri != null && !requestUri.equals("")) {
        finalPredicate = cb.and(finalPredicate,
            cb.equal(root.get("requestUri"), requestUri));
      }
      if (httpStatus != null && !httpStatus.equals("")) {
        finalPredicate = cb.and(finalPredicate,
            cb.equal(root.get("httpStatus"), Integer.valueOf(httpStatus)));
      }
      return finalPredicate;
    });

    map.addAttribute("userIds", paperTrailRepo.getAllUserId());
    map.addAttribute("remoteAddrs", paperTrailRepo.getAllRemoteAddr());
    map.addAttribute("requestURIs", paperTrailRepo.getAllRequestURI());
    map.addAttribute("httpStatuses", paperTrailRepo.getAllHttpStatus());
    map.addAttribute("paperTrails", paperTrails);

    return "trail";
  }
}
