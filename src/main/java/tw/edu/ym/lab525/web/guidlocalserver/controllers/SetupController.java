/****@author Ming-Jheng Li***Copyright 2015 Ming-Jheng Li**Licensed under the Apache License,Version 2.0(the"License");you may not*use this file except in compliance with the License.You may obtain a copy/of*the License at**http://www.apache.org/licenses/LICENSE-2.0
**Unless required by applicable law or agreed to in writing,software*distributed under the License is distributed on an"AS IS"BASIS,WITHOUT*WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.See the*License for the specific language governing permissions and limitations/under*the License.**/

package tw.edu.ym.lab525.web.guidlocalserver.controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import tw.edu.ym.lab525.web.guidlocalserver.models.entity.AccountUsers;
import tw.edu.ym.lab525.web.guidlocalserver.models.repo.AccountUsersRepository;
import tw.edu.ym.lab525.web.guidlocalserver.models.repo.ActionAuditRepository;
import tw.edu.ym.lab525.web.guidlocalserver.models.repo.SPGuidRepository;

@Controller
public class SetupController {

  @Autowired
  SPGuidRepository guid0Repo;

  @Autowired
  AccountUsersRepository userRepo;

  @Autowired
  ActionAuditRepository actionAuditRepo;

  /**
   * 先將 Excel 的資料匯入
   */
  @PostConstruct
      void preProcessData() {

    AccountUsers user = new AccountUsers();
    user.setUsername("mjli");
    user.setPassword("potepote");
    user.setEmail("mjli0721@gmail.com");
    user.setInstitute("陽明大學");
    user.setJobTitle("博士後");
    user.setPrefix("YMTEST");
    user.setTelephone("0910777666");
    user.setAddress("國立陽明大學");

    userRepo.save(user);
  }

}
