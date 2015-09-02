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

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import net.sf.rubycollect4j.RubyCollections;
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
  String trailLookup(ModelMap map, @Param("userId") String userId,
      @Param("createdAt") String createdAt) throws ParseException {

    int year = Integer.valueOf(createdAt.split("/")[0]);
    int month = Integer.valueOf(createdAt.split("/")[1]);
    int day = Integer.valueOf(createdAt.split("/")[2]);

    Date startDate = RubyCollections.date(year, month, day).beginningOfDay();
    Date endDate = RubyCollections.date(year, month, day).endOfDay();

    map.addAttribute("userIds", paperTrailRepo.getAllUserId());
    map.addAttribute("paperTrails",
        userId.equals("")
            ? paperTrailRepo.findByCreatedAtBetween(startDate, endDate)
            : paperTrailRepo.findByUserIdAndCreatedAtBetween(userId, startDate,
                endDate));

    return "trail";
  }
}
