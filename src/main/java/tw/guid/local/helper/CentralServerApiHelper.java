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
package tw.guid.local.helper;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.jsonapi.JsonApi;
import com.github.wnameless.jsonapi.ResourceDocument;
import com.github.wnameless.jsonapi.ResourceObject;
import com.github.wnameless.jsonapi.ResourcesDocument;

import tw.guid.local.model.GuidSet;
import tw.guid.local.model.PrefixedHashBundle;
import tw.guid.local.model.PublicGuid;

public final class CentralServerApiHelper {

  private static final RestTemplate restTemplate = new RestTemplate();

  private static final ObjectMapper mapper = new ObjectMapper();

  private static final HttpHeaders headers = new HttpHeaders();

  private CentralServerApiHelper() {}

  public static String guids(URI uri, PrefixedHashBundle prefixedHashBundle)
      throws IOException {

    return guids(uri, Arrays.asList(prefixedHashBundle)).get(0);
  }

  public static List<String> guids(URI uri,
      List<PrefixedHashBundle> prefixedHashBundles) throws IOException {

    List<String> lists = newArrayList();

    for (PrefixedHashBundle prefixedHashBundle : prefixedHashBundles) {

      ResourceDocument<PrefixedHashBundle> body =
          JsonApi.resourceDocument(prefixedHashBundle, "encodables");

      headers.setContentType(MediaType.valueOf("application/vnd.api+json"));

      HttpEntity<String> req =
          new HttpEntity<String>(mapper.writeValueAsString(body), headers);
      ResponseEntity<String> res =
          restTemplate.postForEntity(uri, req, String.class);

      ResourceDocument<PublicGuid> acutal = mapper.readValue(res.getBody(),
          new TypeReference<ResourceDocument<PublicGuid>>() {});

      lists.add(acutal.getData().getAttributes().getPrefix() + "-"
          + acutal.getData().getAttributes().getCode());
    }

    return lists;
  }

  public static Collection<Set<String>> groupings(URI uri,
      Collection<PublicGuid> subprimeGuids)
          throws JsonParseException, JsonMappingException, IOException {

    ResourceDocument<GuidSet<PublicGuid>> body =
        JsonApi.resourceDocument(new GuidSet<>(subprimeGuids), "lists");

    headers.setContentType(MediaType.valueOf("application/vnd.api+json"));

    HttpEntity<String> request =
        new HttpEntity<String>(mapper.writeValueAsString(body), headers);
    ResponseEntity<String> res =
        restTemplate.postForEntity(uri, request, String.class);

    ResourcesDocument<GuidSet<PublicGuid>> acutal =
        mapper.readValue(res.getBody(),
            new TypeReference<ResourcesDocument<GuidSet<PublicGuid>>>() {});

    Collection<Set<String>> sets = newHashSet();

    for (ResourceObject<GuidSet<PublicGuid>> ros : acutal.getData()) {
      Set<String> set = newHashSet();
      for (PublicGuid pg : ros.getAttributes().getSet()) {
        set.add(pg.getPrefix() + "-" + pg.getCode());
      }
      if (set.size() > 1) sets.add(set);
    }

    return sets;
  }
}
