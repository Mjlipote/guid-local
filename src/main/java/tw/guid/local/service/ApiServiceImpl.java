/**
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
package tw.guid.local.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.AssociationRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.web.Action;

public class ApiServiceImpl implements ApiService {

  @Autowired
  SubprimeGuidRepository spguidRepo;
  @Autowired
  AccountUsersRepository acctUserRepo;
  @Autowired
  AssociationRepository associationRepo;
  @Autowired
  Environment env;

  @Value("${central_server_url}")
  String centralServerUrl;

  /**
   * Get all prefix List
   * 
   * @return
   */
  @Override
  public Set<String> prefixLookup() {
    return acctUserRepo.getAllPrefix();
  }

  @Override
  public Set<String> hospitalLookup() {
    return associationRepo.getAllHospital();
  }

  @Override
  public Set<String> doctorLookup() {
    return associationRepo.getAllDoctor();
  }

  @Override
  public boolean validation(String spguid) throws URISyntaxException {
    return HttpActionHelper.toGet(new URI(centralServerUrl), Action.VALIDATION,
        "?spguid=" + spguid, false).getBody().equals("true") ? true : false;
  }

  /**
   * 確認是否存在於 local server 資料庫
   * 
   * @param hashcode1
   * @param hashcode2
   * @param hashcode3
   * @return
   * @throws SQLException
   * @throws URISyntaxException
   */
  @Override
  public boolean existence(String hashcode1, String hashcode2, String hashcode3)
      throws URISyntaxException {
    checkNotNull(hashcode1, "hashcode1 can't be null");
    checkNotNull(hashcode2, "hashcode2 can't be null");
    checkNotNull(hashcode3, "hashcode3 can't be null");

    if (spguidRepo.findByHashcode1AndHashcode2AndHashcode3(hashcode1, hashcode2,
        hashcode3).size() > 0) {
      return true;
    } else {
      return HttpActionHelper.toGet(new URI(centralServerUrl),
          Action.EXISTENCE, "?hashcode1=" + hashcode1 + "&hashcode2="
              + hashcode2 + "&hashcode3=" + hashcode3,
          false).getBody().equals("true") ? true : false;
    }
  }

}
