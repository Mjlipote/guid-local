/*
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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import tw.guid.local.entity.AccountUser;
import tw.guid.local.entity.SubprimeGuid;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.web.Role;

@Controller
public class SetupController {

  @Autowired
  SubprimeGuidRepository spGuidRepo;

  @Autowired
  AccountUsersRepository userRepo;

  /**
   * 先將 Excel 的資料匯入
   */
  @PostConstruct
  void preProcessData() {

    AccountUser superAdmin = new AccountUser();
    superAdmin.setUsername("super");
    superAdmin.setPassword("1qaz$RFV");
    superAdmin.setEmail("super@ym.com");
    superAdmin.setInstitute("國立陽明大學");
    superAdmin.setJobTitle("生物資訊主管");
    superAdmin.setPrefix("AdminTest");
    superAdmin.setTelephone("0910333222");
    superAdmin.setAddress("國立陽明大學");
    superAdmin.setRole(Role.ROLE_ADMIN);

    AccountUser admin = new AccountUser();
    admin.setUsername("admin");
    admin.setPassword("password");
    admin.setEmail("admin@ym.com");
    admin.setInstitute("國立陽明大學");
    admin.setJobTitle("系統管理員");
    admin.setPrefix("AdminTest");
    admin.setTelephone("0910777666");
    admin.setAddress("國立陽明大學");
    admin.setRole(Role.ROLE_ADMIN);

    AccountUser user = new AccountUser();
    user.setUsername("user");
    user.setPassword("password");
    user.setEmail("user@ym.com");
    user.setInstitute("國立陽明大學");
    user.setJobTitle("使用者");
    user.setPrefix("UserTest");
    user.setTelephone("0910777000");
    user.setAddress("國立陽明大學");
    user.setRole(Role.ROLE_USER);

    userRepo.save(superAdmin);
    userRepo.save(admin);
    userRepo.save(user);

    SubprimeGuid spguid = new SubprimeGuid();
    spguid.setHashcode1(
        "f3042960fc9351d1ad99550817f892968207c6cb2539c6fd11b3258e815dedfe4f8f3f2a95c846b32aacf6201282921e2b93812587cc19752cfc9c0cf236a57b00");
    spguid.setHashcode2(
        "e92e7cf25a726bb9f7aff7c36c31fa4a96b0014a3a7ce5018c6b84bc459df512653253d01e0742878ca7ddd7bd9c5179273fa915761a9ba84948fd85007cc8f900");
    spguid.setHashcode3(
        "636ce21c211c33e6ee8e2f7590034fef8a3a5b3263c6d83af9c54b490175d649f11937e855509f57c986d1882cb5259372a37697899660afff8db6c8049de6a900");
    spguid.setPrefix("TEST");
    spguid.setSpguid("TEST-Y3XZU2NG");

    spGuidRepo.save(spguid);
  }

}
