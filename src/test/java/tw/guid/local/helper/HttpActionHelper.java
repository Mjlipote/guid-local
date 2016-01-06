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

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.guid.local.web.Action;

public final class HttpActionHelper {

  private static final String API_ROOT = "guids";

  private static final RestTemplate restTemplate = new RestTemplate();

  private static final ObjectMapper mapper = new ObjectMapper();

  private HttpActionHelper() {}

  public static ResponseEntity<String> toPostWithoutApiRoot(URI url,
      Action action, Object object, boolean authority)
          throws JsonProcessingException {
    return PostCreater(url, action, object, authority, false);
  }

  public static ResponseEntity<String> toGetWithoutApiRoot(URI url,
      Action action, String param, boolean authority) {
    return getCreater(url, action, param, authority, false);
  }

  public static ResponseEntity<String> toPost(URI url, Action action,
      Object object, boolean authority) throws JsonProcessingException {
    return PostCreater(url, action, object, authority, true);
  }

  public static ResponseEntity<String> toGet(URI url, Action action,
      String param, boolean authority) {
    return getCreater(url, action, param, authority, true);
  }

  private static ResponseEntity<String> PostCreater(URI url, Action action,
      Object object, boolean authority, boolean apiRoot)
          throws JsonProcessingException {
    HttpHeaders headers = new HttpHeaders();
    if (authority == true) {
      headers = getHeaders("admin", HashcodeCreator.getSha512("password"));
    } else {
      headers.setContentType(APPLICATION_JSON);
      headers.setAccept(Arrays.asList(APPLICATION_JSON));
      headers.add("custom", "true");
    }

    HttpEntity<String> jsonRequest =
        new HttpEntity<>(mapper.writeValueAsString(object), headers);
    String urlStr = apiRoot == true
        ? url.getScheme() + "://" + url.getAuthority() + "/" + API_ROOT + "/"
            + action
        : url.getScheme() + "://" + url.getAuthority() + "/" + action;
    ResponseEntity<String> response = restTemplate.exchange(urlStr,
        HttpMethod.POST, jsonRequest, String.class);

    return response;
  }

  private static ResponseEntity<String> getCreater(URI url, Action action,
      String param, boolean authority, boolean apiRoot) {
    HttpHeaders headers = new HttpHeaders();
    if (authority == true) {
      headers = getHeaders("admin", HashcodeCreator.getSha512("password"));
    } else {
      headers.setContentType(APPLICATION_JSON);
      headers.setAccept(Arrays.asList(APPLICATION_JSON));
      headers.add("custom", "true");
    }

    HttpEntity<String> jsonRequest = new HttpEntity<>(headers);
    ResponseEntity<String> response = restTemplate.exchange(
        apiRoot == true ? url.getScheme() + "://" + url.getAuthority() + "/"
            + API_ROOT + "/" + action + param
        : url.getScheme() + "://" + url.getAuthority() + "/" + action + param,
        HttpMethod.GET, jsonRequest, String.class);

    return response;
  }

  private static HttpHeaders getHeaders(String username, String password) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(APPLICATION_JSON);
    httpHeaders.setAccept(Arrays.asList(APPLICATION_JSON));
    String plainCreds = username + ":" + password;
    byte[] base64CredsBytes =
        Base64.encodeBase64(plainCreds.getBytes(Charset.forName("US-ASCII")));
    String base64Creds = new String(base64CredsBytes);
    httpHeaders.add("Authorization", "Basic " + base64Creds);

    return httpHeaders;
  }

}
