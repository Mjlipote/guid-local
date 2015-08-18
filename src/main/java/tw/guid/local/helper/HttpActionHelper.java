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
package tw.guid.local.helper;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.guid.local.models.Action;

public final class HttpActionHelper {

  private static final String API_ROOT = "guid";

  private static RestTemplate restTemplate = new RestTemplate();

  private static HttpHeaders headers = new HttpHeaders();

  private static ObjectMapper mapper = new ObjectMapper();

  private HttpActionHelper() {}

  /**
   * 
   * @param url
   * @param object
   * @param action
   * @return
   * @throws JsonProcessingException
   */
  public static ResponseEntity<String> toPostWithoutApiRoot(URI url,
      Action action, Object object, boolean authority)
          throws JsonProcessingException {

    return PostCreater(url, action, object, authority, false);
  }

  /**
   * 
   * @param url
   * @param action
   * @param object
   * @return
   */
  public static ResponseEntity<String> toGetWithoutApiRoot(URI url,
      Action action, String param, boolean authority) {

    return getCreater(url, action, param, authority, false);
  }

  /**
   * 
   * @param url
   * @param object
   * @param action
   * @return
   * @throws JsonProcessingException
   */
  public static ResponseEntity<String> toPost(URI url, Action action,
      Object object, boolean authority) throws JsonProcessingException {

    return PostCreater(url, action, object, authority, true);
  }

  /**
   * 
   * @param url
   * @param action
   * @param object
   * @return
   */
  public static ResponseEntity<String> toGet(URI url, Action action,
      String param, boolean authority) {

    return getCreater(url, action, param, authority, true);
  }

  private static ResponseEntity<String> PostCreater(URI url, Action action,
      Object object, boolean authority, boolean apiRoot)
          throws JsonProcessingException {

    if (authority == true) {
      headers = getHeaders("admin", Crc32HashcodeCreator.getCrc32("password"));
    } else {
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      headers.add("custom", "true");
    }

    String http[] = url.toString().split("://");

    HttpEntity<String> jsonRequest =
        new HttpEntity<String>(mapper.writeValueAsString(object), headers);
    String urlStr = apiRoot == true
        ? http[0] + "://" + url.getHost() + ":" + url.getPort() + "/" + API_ROOT
            + "/" + action
        : http[0] + "://" + url.getHost() + ":" + url.getPort() + "/" + action;
    ResponseEntity<String> response = restTemplate.exchange(urlStr,
        HttpMethod.POST, jsonRequest, String.class);

    return response;
  }

  private static ResponseEntity<String> getCreater(URI url, Action action,
      String param, boolean authority, boolean apiRoot) {

    if (authority == true) {
      headers = getHeaders("admin", Crc32HashcodeCreator.getCrc32("password"));
    } else {
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      headers.add("custom", "true");
    }

    String http[] = url.toString().split("://");

    HttpEntity<String> jsonRequest = new HttpEntity<String>(headers);
    ResponseEntity<String> response =
        restTemplate.exchange(
            apiRoot == true
                ? http[0] + "://" + url.getHost() + ":" + url.getPort() + "/"
                    + API_ROOT + "/" + action + param
                : http[0] + "://" + url.getHost() + ":" + url.getPort() + "/"
                    + action + param,
            HttpMethod.GET, jsonRequest, String.class);

    return response;
  }

  /**
   * 
   * @param username
   * @param password
   * @return
   */
  private static HttpHeaders getHeaders(String username, String password) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    String plainCreds = username + ":" + password;
    byte[] base64CredsBytes =
        Base64.encodeBase64(plainCreds.getBytes(Charset.forName("US-ASCII")));
    String base64Creds = new String(base64CredsBytes);
    httpHeaders.add("Authorization", "Basic " + base64Creds);

    return httpHeaders;
  }

}
