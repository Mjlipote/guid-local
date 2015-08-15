/**
 * 
 */
package tw.guid.local.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

  @RequestMapping("/home")

  String home() {
    return "home";
  }

  @RequestMapping("/register")

  String register() {
    return "register";
  }

  @RequestMapping("/create")

  String create() {
    return "create";
  }

  @RequestMapping("/comparison")

  String comparison() {
    return "comparison";
  }

  @RequestMapping("/deleteuser")

  String deleteuser() {
    return "deleteuser";
  }

  @RequestMapping("/hello")

  String hello() {
    return "hello";
  }

  @RequestMapping("/login")

  String login() {
    return "login";
  }
}
