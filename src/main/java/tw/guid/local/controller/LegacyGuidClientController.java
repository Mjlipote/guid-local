/**
 *
 * @author Wei-Ming Wu, Ming-Jheng Li
 *
 *
 * Copyright 2015 Wei-Ming Wu, Ming-Jheng Li
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

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.guid.local.service.LegacyService;

@RequestMapping("/guid")
@Controller
public class LegacyGuidClientController {

  @Autowired
  LegacyService legacyService;

  @RequestMapping(value = "/authenticate", method = RequestMethod.GET)
  @ResponseBody
  String authenticate(HttpServletRequest request) {
    return legacyService.authenticate(request);
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @ResponseBody
  String create(@RequestParam("prefix") String prefix,
      @RequestParam("hashes") String jsonHashes, HttpServletRequest request) {
    return legacyService.create(prefix, jsonHashes, request);
  }

}
