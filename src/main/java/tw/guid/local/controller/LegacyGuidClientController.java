/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.guid.local.service.LegacyService;

@RequestMapping("/guid")
@Controller
public class LegacyGuidClientController {

  @Autowired
  LegacyService legacyService;

  @RequestMapping("/authenticate")
  @ResponseBody
  String authenticate(HttpServletRequest request) {
    return legacyService.authenticate(request);
  }

  @RequestMapping("/create")
  @ResponseBody
  String create(@RequestParam("prefix") String prefix,
      @RequestParam("hashes") String jsonHashes, HttpServletRequest request) {
    return legacyService.create(prefix, jsonHashes, request);
  }

}
