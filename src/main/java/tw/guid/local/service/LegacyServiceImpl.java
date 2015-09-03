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

import static com.google.common.collect.Lists.newArrayList;
import static net.sf.rubycollect4j.RubyCollections.hp;
import static net.sf.rubycollect4j.RubyCollections.ra;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.rubycollect4j.RubyArray;
import tw.guid.local.entity.AccountUser;
import tw.guid.local.entity.SubprimeGuid;
import tw.guid.local.helper.HashcodeCreator;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.web.Action;
import tw.guid.local.web.GuidException;
import tw.guid.local.web.SubprimeGuidRequest;

public class LegacyServiceImpl implements LegacyService {

  private static final Logger log =
      LoggerFactory.getLogger(LegacyServiceImpl.class);

  @Value("${guid.central.server.url}")
  private String centralServerUrl;

  @Autowired
  private AccountUsersRepository acctUserRepo;

  @Autowired
  private SubprimeGuidRepository subprimeGuidRepo;

  @Override
  public String authenticate(HttpServletRequest request) {
    return new Gson().toJson(isValidate(request), Boolean.class);
  }

  @Override
  public String create(String prefix, String jsonHashes,
      HttpServletRequest request) {
    if (!isValidate(request)) throw new GuidException(UNAUTHORIZED);

    if (prefix.equals("") && isValidate(request)) {
      prefix = getPrefixFromCurrentLoginUser(request);
    } else if (prefix.equals("")) {
      prefix = "PSEUDO";
    }

    List<SubprimeGuidRequest> sgrs = buildRequests(prefix, jsonHashes);

    RubyArray<Entry<Boolean, SubprimeGuidRequest>> boolSgrs =
        ra(sgrs).map((sgq) -> {
          if (subprimeGuidRepo.isExist(sgq))
            return hp(true, sgq);
          else
            return hp(false, sgq);
        });

    List<String> correctGuids = boolSgrs.map((bs) -> {
      if (bs.getKey())
        return subprimeGuidRepo
            .getSubprimeGuidBySubprimeGuidRequest(bs.getValue());
      else {
        String result = null;

        try {
          result = (String) JsonFlattener
              .flattenAsMap(HttpActionHelper.toPost(new URI(centralServerUrl),
                  Action.NEW, Arrays.asList(bs.getValue()), false).getBody())
              .get("[0].spguid");
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }

        String[] str = result.split("-");
        SubprimeGuid subprimeGuid = new SubprimeGuid();
        subprimeGuid.setSpguid(result);
        subprimeGuid.setPrefix(str[0]);
        subprimeGuid.setHashcode1(bs.getValue().getGuidHash().get(0));
        subprimeGuid.setHashcode2(bs.getValue().getGuidHash().get(1));
        subprimeGuid.setHashcode3(bs.getValue().getGuidHash().get(2));
        subprimeGuidRepo.save(subprimeGuid);

        return result;
      }
    });

    return new Gson().toJson(correctGuids);
  }

  private List<SubprimeGuidRequest> buildRequests(String prefix,
      String jsonHashes) {
    List<SubprimeGuidRequest> sgrs = newArrayList();

    List<String> hash = null;
    List<List<String>> hashes = null;
    try {
      hash = new Gson().fromJson(jsonHashes,
          new TypeToken<List<String>>() {}.getType());
    } catch (Exception e) {
      hashes = new Gson().fromJson(jsonHashes,
          new TypeToken<List<List<String>>>() {}.getType());
    }

    if (hash != null) {
      sgrs.add(new SubprimeGuidRequest(prefix, hash));
    }

    if (hashes != null) {
      for (List<String> h : hashes) {
        sgrs.add(new SubprimeGuidRequest(prefix, h));
      }
    }

    return sgrs;
  }

  private AccountUser getAccountUsers(HttpServletRequest request) {
    String base64Credentials = request.getHeader("Authorization");
    String credentials = new String(BaseEncoding.base64()
        .decode(base64Credentials.replaceFirst("^Basic\\s+", "")));

    final String[] values = credentials.split(":", 2);

    return acctUserRepo.findByUsernameAndPassword(values[0],
        HashcodeCreator.getSha512(values[1]));
  }

  private boolean isValidate(HttpServletRequest request) {
    return getAccountUsers(request) != null ? true : false;
  }

  private String getPrefixFromCurrentLoginUser(HttpServletRequest request) {
    return getAccountUsers(request).getPrefix();
  }

}
