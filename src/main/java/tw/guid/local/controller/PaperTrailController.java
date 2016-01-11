/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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

  @RequestMapping(method = GET)
  String trail(ModelMap map) {
    map.addAttribute("remoteAddrs", paperTrailRepo.getAllRemoteAddr());
    map.addAttribute("userIds", paperTrailRepo.getAllUserId());
    map.addAttribute("requestURIs", paperTrailRepo.getAllRequestURI());
    map.addAttribute("httpStatuses", paperTrailRepo.getAllHttpStatus());
    map.addAttribute("paperTrails", paperTrailRepo.findAll());
    return "trail";
  }

  @RequestMapping(value = "/lookup", method = GET)
  String trailLookup(ModelMap map, @Param("userId") String userId,
      @Param("createdAt") String createdAt,
      @Param("createdEnd") String createdEnd) throws ParseException {
    int year = Integer.valueOf(createdAt.split("/")[0]);
    int month = Integer.valueOf(createdAt.split("/")[1]);
    int day = Integer.valueOf(createdAt.split("/")[2]);

    int yearEnd = Integer.valueOf(createdEnd.split("/")[0]);
    int monthEnd = Integer.valueOf(createdEnd.split("/")[1]);
    int dayEnd = Integer.valueOf(createdEnd.split("/")[2]);

    Date startDate = RubyCollections.date(year, month, day).beginningOfDay();
    Date endDate = RubyCollections.date(yearEnd, monthEnd, dayEnd).endOfDay();

    map.addAttribute("userIds", paperTrailRepo.getAllUserId());
    map.addAttribute("paperTrails",
        userId.equals("")
            ? paperTrailRepo.findByCreatedAtBetween(startDate, endDate)
            : paperTrailRepo.findByUserIdAndCreatedAtBetween(userId, startDate,
                endDate));

    return "trail";
  }

}
