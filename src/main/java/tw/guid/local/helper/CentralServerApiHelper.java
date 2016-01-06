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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.jsonapi.JsonApi;
import com.github.wnameless.jsonapi.ResourceDocument;
import com.github.wnameless.jsonapi.ResourceObject;
import com.github.wnameless.jsonapi.ResourcesDocument;

import tw.guid.central.core.GuidSet;
import tw.guid.central.core.PublicGuid;
import tw.guid.client.BasicAuthSSLClient;
import tw.guid.client.GuidClient;
import tw.guid.client.PII;

public final class CentralServerApiHelper {

  private static final ObjectMapper mapper = new ObjectMapper();

  private static HttpClient httpClient;

  private static final String API_ENDPOINT = "/api/v1";

  private CentralServerApiHelper() {

  }

  public static String guids(URI uri, String publicKey, String prefix, PII pii)
      throws IOException {

    return guids(uri, publicKey, prefix, Arrays.asList(pii)).get(0);
  }

  public static List<String> guids(URI uri, String publicKey, String prefix,
      List<PII> piis) throws IOException {

    List<String> lists = newArrayList();

    GuidClient guidClient = new GuidClient(uri, publicKey);

    for (PII p : piis) {
      lists.add(guidClient.compute(prefix, p).getPrefix() + "-"
          + guidClient.compute(prefix, p).getCode());
    }

    return lists;

  }

  public static Collection<Set<String>> groupings(URI uri, String publicKey,
      Collection<PublicGuid> subprimeGuids)
          throws JsonParseException, JsonMappingException, IOException {

    httpClient = BasicAuthSSLClient.create("token", checkNotNull(publicKey));

    HttpPost post = new HttpPost(uri + API_ENDPOINT + "/groupings");

    post.addHeader("content-type", "application/json");

    ResourceDocument<GuidSet<PublicGuid>> body =
        JsonApi.resourceDocument(new GuidSet<>(subprimeGuids), "lists");

    post.setEntity(new StringEntity(mapper.writeValueAsString(body)));

    HttpResponse res = httpClient.execute(post);

    ResourcesDocument<GuidSet<PublicGuid>> acutal =
        mapper.readValue(res.getEntity().getContent(),
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
