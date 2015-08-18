/**
 * 
 */
package tw.guid.local.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import tw.guid.local.models.entity.AccountUsers;
import tw.guid.local.models.repo.AccountUsersRepository;

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

@Controller
public class MainController {

  @Autowired
  AccountUsersRepository acctUserRepo;

  @RequestMapping("/home")

  String home() {
    return "home";
  }

  @RequestMapping("/register")

  String usersRegister() {
    return "register";
  }

  @RequestMapping("/guids")

  String guidsNew() {
    return "guids";
  }

  @RequestMapping("/comparison")

  String guidsComparison() {
    return "comparison";
  }

  @RequestMapping("/remove")

  String usersRemove() {
    return "users-remove";
  }

  @RequestMapping("/users")
  String usersList(ModelMap map, @Param("page") Integer page) {

    PageRequest pageReq =
        new PageRequest(0, 10, new Sort(new Order(Direction.ASC, "username")));

    Page<AccountUsers> accPage;

    accPage = acctUserRepo.findAll(pageReq);

    map.addAttribute("accPage", accPage);
    return "users";

  }

  @RequestMapping("/login")

  String login() {
    return "login";
  }
}
