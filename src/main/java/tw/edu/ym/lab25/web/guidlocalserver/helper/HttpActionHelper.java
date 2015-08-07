/**
 * 
 */
package tw.edu.ym.lab25.web.guidlocalserver.helper;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public class HttpActionHelper {

  private static RestTemplate restTemplate = new RestTemplate();

  private static HttpHeaders headers = new HttpHeaders();

  private static ObjectMapper mapper = new ObjectMapper();

  private HttpActionHelper() {

  }

  /**
   * 
   * @param url
   * @param object
   * @param action
   * @return
   * @throws JsonProcessingException
   */
  public static String toPost(URI url, String action, Object object) throws JsonProcessingException {

    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("custom", "true");

    HttpEntity<String> jsonRequest = new HttpEntity<String>(mapper.writeValueAsString(object), headers);

    ResponseEntity<String> res = restTemplate
        .postForEntity("http://" + url.getHost() + ":" + url.getPort() + "/" + action, jsonRequest, String.class);

    return res.getBody();
  }

  /**
   * 
   * @param url
   * @param action
   * @param object
   * @return
   */
  public static String toGet(URI url, String action, Object object) {

    ResponseEntity<String> res = restTemplate
        .getForEntity("http://" + url.getHost() + ":" + url.getPort() + "/" + action + object, String.class);

    return res.getBody();
  }
}
