/*
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

import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.jsonapi.JsonApi;
import com.github.wnameless.jsonapi.ResourceDocument;
import com.google.common.io.BaseEncoding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.rubycollect4j.RubyArray;
import tw.guid.local.entity.AccountUser;
import tw.guid.local.entity.SubprimeGuid;
import tw.guid.local.helper.HashcodeCreator;
import tw.guid.local.model.PrefixedHashBundle;
import tw.guid.local.model.PublicGuid;
import tw.guid.local.repository.AccountUsersRepository;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.web.LegacyGuidException;

public class LegacyServiceImpl implements LegacyService {

  RestTemplate restTemplate = new TestRestTemplate();
  ObjectMapper mapper = new ObjectMapper();

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
    if (!isValidate(request)) throw new LegacyGuidException(UNAUTHORIZED);

    if (prefix.equals("") && isValidate(request)) {
      prefix = getPrefixFromCurrentLoginUser(request);
    } else if (prefix.equals("")) {
      prefix = "PSEUDO";
    }

    List<PrefixedHashBundle> sgrs = buildRequests(prefix, jsonHashes);

    RubyArray<Entry<Boolean, PrefixedHashBundle>> boolSgrs =
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
        PrefixedHashBundle prefixedHashBundle = new PrefixedHashBundle();
        prefixedHashBundle.setHash1(bs.getValue().getHash1().substring(0, 128));
        prefixedHashBundle.setHash2(bs.getValue().getHash2().substring(0, 128));
        prefixedHashBundle.setHash3(bs.getValue().getHash3().substring(0, 128));
        prefixedHashBundle.setPrefix(bs.getValue().getPrefix());

        try {

          ResourceDocument<PrefixedHashBundle> body =
              JsonApi.resourceDocument(prefixedHashBundle, "encodables");

          HttpHeaders headers = new HttpHeaders();
          headers.setContentType(MediaType.valueOf("application/vnd.api+json"));

          HttpEntity<String> req =
              new HttpEntity<String>(mapper.writeValueAsString(body), headers);
          ResponseEntity<String> res = restTemplate.postForEntity(
              centralServerUrl + "/api/v1/guids", req, String.class);

          ResourceDocument<PublicGuid> acutal = mapper.readValue(res.getBody(),
              new TypeReference<ResourceDocument<PublicGuid>>() {});

          result = acutal.getData().getAttributes().getPrefix() + "-"
              + acutal.getData().getAttributes().getCode();
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }

        String[] str = result.split("-");
        SubprimeGuid subprimeGuid = new SubprimeGuid();
        subprimeGuid.setSpguid(result);
        subprimeGuid.setPrefix(str[0]);
        subprimeGuid.setHashcode1(bs.getValue().getHash1().substring(0, 128));
        subprimeGuid.setHashcode2(bs.getValue().getHash2().substring(0, 128));
        subprimeGuid.setHashcode3(bs.getValue().getHash3().substring(0, 128));
        subprimeGuidRepo.save(subprimeGuid);

        return result;
      }
    });

    return new Gson().toJson(correctGuids);
  }

  private List<PrefixedHashBundle> buildRequests(String prefix,
      String jsonHashes) {
    List<PrefixedHashBundle> prefixedHashBundles = newArrayList();

    List<String> hash = null;
    List<List<String>> hashes = null;
    try {
      hash = new Gson().fromJson(jsonHashes,
          new TypeToken<List<String>>() {}.getType());
    } catch (Exception e) {
      hashes = new Gson().fromJson(jsonHashes,
          new TypeToken<List<List<String>>>() {}.getType());
    }

    if (hash != null)

    {
      prefixedHashBundles.add(new PrefixedHashBundle(prefix, hash.get(0),
          hash.get(1), hash.get(2)));
    }

    if (hashes != null)

    {
      for (List<String> h : hashes) {
        prefixedHashBundles
            .add(new PrefixedHashBundle(prefix, h.get(0), h.get(1), h.get(2)));
      }
    }

    return prefixedHashBundles;

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
    return getAccountUsers(request).getInstitutePrefix().getPrefix();
  }

}
