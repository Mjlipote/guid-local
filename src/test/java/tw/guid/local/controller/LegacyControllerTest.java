/*
 *
 * @author Ming Jheng Li
 *
 *
 * Copyright 2015 Ming Jheng Li
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
package tw.guid.local.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.jsonapi.JsonApi;
import com.github.wnameless.jsonapi.ResourceDocument;
import com.github.wnameless.jsonapi.ResourcesDocument;
import com.github.wnameless.math.NumberSystem;
import com.google.common.primitives.Chars;

import tw.edu.ym.guid.client.GuidClient;
import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Name;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.guid.local.Application;
import tw.guid.local.model.GuidSet;
import tw.guid.local.model.PrefixedHashBundle;
import tw.guid.local.model.PublicGuid;
import tw.guid.local.repository.SubprimeGuidRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class LegacyControllerTest {

  RestTemplate restTemplate = new TestRestTemplate();
  ObjectMapper mapper = new ObjectMapper();
  PrefixedHashBundle legacyEncodable;
  PrefixedHashBundle legacyEncodable1;
  PrefixedHashBundle newEncodable;
  PrefixedHashBundle invalidEncodable;

  static {
    // for localhost testing only
    javax.net.ssl.HttpsURLConnection
        .setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {

          @Override
          public boolean verify(String hostname,
              javax.net.ssl.SSLSession sslSession) {
            if (hostname.equals("localhost")) {
              return true;
            }
            return false;
          }
        });
  }

  @Autowired
  Environment env;

  @Autowired
  SubprimeGuidRepository subprimeGuidRepo;

  @Value("${guid.local.server.url}")
  String localServerUrl;

  @Value("${guid.central.server.url}")
  String centralServerUrl;

  @Before
  public void setUp() throws Exception {
    legacyEncodable = new PrefixedHashBundle();
    legacyEncodable.setPrefix("CCH24");
    legacyEncodable.setHash1(
        "293ae00f7a0088e628d5386da01b40fa0491a82498af7a88ca8147a9ca601e8583b4b5c8e4af045cd2554da4af5b82a24f0bef25cea9867879ef05460fa386bf");
    legacyEncodable.setHash2(
        "cadfd5da66fecd9f6475614c2029fc35c1be89076f4262ccf1184c61e25dcbdd690dbc7f65b79b826ba2a840165b073aff038ee090f3fa1fe3fdf7cee2530a64");
    legacyEncodable.setHash3(
        "01033b50d8d2a018e466009bf2cbb6334853696c7406f0ac9845d4b9e543ef9f2c8f1c903a57298ae5211d1f5a64332a7d8bec8de7f54837ab6aeadde5dfb20f");

    legacyEncodable1 = new PrefixedHashBundle();
    legacyEncodable1.setPrefix("VGH16");
    legacyEncodable1.setHash1(
        "293ae00f7a0088e628d5386da01b40fa0491a82498af7a88ca8147a9ca601e8583b4b5c8e4af045cd2554da4af5b82a24f0bef25cea9867879ef05460fa386bf");
    legacyEncodable1.setHash2(
        "cadfd5da66fecd9f6475614c2029fc35c1be89076f4262ccf1184c61e25dcbdd690dbc7f65b79b826ba2a840165b073aff038ee090f3fa1fe3fdf7cee2530a64");
    legacyEncodable1.setHash3(
        "01033b50d8d2a018e466009bf2cbb6334853696c7406f0ac9845d4b9e543ef9f2c8f1c903a57298ae5211d1f5a64332a7d8bec8de7f54837ab6aeadde5dfb20f");

    newEncodable = new PrefixedHashBundle();
    newEncodable.setPrefix("XXYYZZ");
    newEncodable.setHash1(
        "193ae00f7a0088e628d5386da01b40fa0491a82498af7a88ca8147a9ca601e8583b4b5c8e4af045cd2554da4af5b82a24f0bef25cea9867879ef05460fa386bf");
    newEncodable.setHash2(
        "1adfd5da66fecd9f6475614c2029fc35c1be89076f4262ccf1184c61e25dcbdd690dbc7f65b79b826ba2a840165b073aff038ee090f3fa1fe3fdf7cee2530a64");
    newEncodable.setHash3(
        "11033b50d8d2a018e466009bf2cbb6334853696c7406f0ac9845d4b9e543ef9f2c8f1c903a57298ae5211d1f5a64332a7d8bec8de7f54837ab6aeadde5dfb20f");
    invalidEncodable = new PrefixedHashBundle();
    invalidEncodable.setPrefix("XXYYZZ");
    invalidEncodable.setHash1(
        "93ae00f7a0088e628d5386da01b40fa0491a82498af7a88ca8147a9ca601e8583b4b5c8e4af045cd2554da4af5b82a24f0bef25cea9867879ef05460fa386bf");
    invalidEncodable.setHash2(
        "adfd5da66fecd9f6475614c2029fc35c1be89076f4262ccf1184c61e25dcbdd690dbc7f65b79b826ba2a840165b073aff038ee090f3fa1fe3fdf7cee2530a64");
    invalidEncodable.setHash3(
        "1033b50d8d2a018e466009bf2cbb6334853696c7406f0ac9845d4b9e543ef9f2c8f1c903a57298ae5211d1f5a64332a7d8bec8de7f54837ab6aeadde5dfb20f");
  }

  @Test
  public void testAuthentication() throws IOException, URISyntaxException {
    GuidClient gc =
        new GuidClient(new URI(localServerUrl), "admin", "password");
    assertTrue(gc.authenticate());

  }

  @Test
  public void testGuidCreateWithOldPrefix()
      throws IOException, URISyntaxException {
    GuidClient gc =
        new GuidClient(new URI(localServerUrl), "admin", "password", "VGH16");
    PII pii = new PII.Builder(new Name("明政", "李"), Sex.MALE,
        new Birthday(1979, 7, 21), new TWNationalId("E122371585")).build();

    assertEquals(gc.create(pii), "VGH16-3AC3DEC6");

  }

  @Test
  public void testLegacyGuidCreateWithNewPrefix()
      throws IOException, URISyntaxException {
    GuidClient gc =
        new GuidClient(new URI(localServerUrl), "admin", "password", "AABBXX");
    PII pii = new PII.Builder(new Name("明政", "李"), Sex.MALE,
        new Birthday(1979, 7, 21), new TWNationalId("E122371585")).build();

    String subprimeGuid = gc.create(pii);

    PrefixedHashBundle legacyEncodable = new PrefixedHashBundle();
    legacyEncodable.setPrefix("AABBXX");
    legacyEncodable.setHash1(
        "f3daf55c7999e106cebb8733d24a8baa25b1a684154d601de5398cabde4d2da50072215f81ab0879f59ae29551b0442cbef37dd35931757f8745ca3d455caa95");
    legacyEncodable.setHash2(
        "57785c82cbeb5d9fdbf2d05ffaab797f23b2ca145c838a4f18225e2349fd60ec4b5d02c4410c88c22bf06dea3b4eed21700f313402b6b77700e012ddeeb3dc23");
    legacyEncodable.setHash3(
        "44ce1ef09c3e215aa635d1981dc7515705c02e67d50a46247f6fa55c23af3bc4dd4a3ceb48ccebff556529e8394f618461e71e4af99b847b0427d7cb57ad3587");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.valueOf("application/vnd.api+json"));

    ResourceDocument<PrefixedHashBundle> body =
        JsonApi.resourceDocument(legacyEncodable, "encodables");

    HttpEntity<String> request =
        new HttpEntity<String>(mapper.writeValueAsString(body), headers);
    ResponseEntity<String> res = restTemplate.postForEntity(
        centralServerUrl + "/api/v1/guids", request, String.class);

    ResourceDocument<PublicGuid> acutal = mapper.readValue(res.getBody(),
        new TypeReference<ResourceDocument<PublicGuid>>() {});

    assertEquals(subprimeGuid.split("-")[0],
        acutal.getData().getAttributes().getPrefix());
    assertEquals(subprimeGuid.split("-")[1],
        acutal.getData().getAttributes().getCode());

  }

  @Test
  public void testNewRepeatSubprimeGuids()
      throws IOException, URISyntaxException {

    GuidClient gc =
        new GuidClient(new URI(localServerUrl), "admin", "password", "Test");
    PII pii = new PII.Builder(new Name("大頭", "王"), Sex.MALE,
        new Birthday(2012, 1, 11), new TWNationalId("A123456789")).build();

    PrefixedHashBundle prefixedHashBundle = new PrefixedHashBundle();
    prefixedHashBundle.setPrefix("Test");
    prefixedHashBundle.setHash1(pii.getHashcodes().get(0));
    prefixedHashBundle.setHash2(pii.getHashcodes().get(1));
    prefixedHashBundle.setHash3(pii.getHashcodes().get(2));

    assertFalse(subprimeGuidRepo.isExist(prefixedHashBundle));

    gc.create(pii);

    assertTrue(subprimeGuidRepo.isExist(prefixedHashBundle));

  }

  @Test
  public void testGroup() throws Exception {
    ResourceDocument<GuidSet<PublicGuid>> body = JsonApi.resourceDocument(
        new GuidSet<>(Arrays.asList(new PublicGuid("VGH26", "E831D239"),
            new PublicGuid("VGH16", "0306693A"))),
        "lists");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.valueOf("application/vnd.api+json"));

    HttpEntity<String> request =
        new HttpEntity<String>(mapper.writeValueAsString(body), headers);
    ResponseEntity<String> res = restTemplate.postForEntity(
        centralServerUrl + "/api/v1/groupings", request, String.class);

    ResourcesDocument<GuidSet<PublicGuid>> acutal =
        mapper.readValue(res.getBody(),
            new TypeReference<ResourcesDocument<GuidSet<PublicGuid>>>() {});
    GuidSet<PublicGuid> guidSet = new GuidSet<PublicGuid>(
        Arrays.asList(new PublicGuid("VGH26", "E831D239"),
            new PublicGuid("VGH16", "0306693A")));
    ResourcesDocument<GuidSet<PublicGuid>> expect =
        JsonApi.resourcesDocument(Arrays.asList(guidSet), "groupings",
            (set) -> String.valueOf(set.hashCode()));
    assertEquals(expect, acutal);
  }

  @Test
  public void testCreatWithNewEncodable() throws Exception {
    ResourceDocument<PrefixedHashBundle> body =
        JsonApi.resourceDocument(newEncodable, "encodables");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.valueOf("application/vnd.api+json"));

    HttpEntity<String> request =
        new HttpEntity<String>(mapper.writeValueAsString(body), headers);
    ResponseEntity<String> res = restTemplate.postForEntity(
        centralServerUrl + "/api/v1/guids", request, String.class);

    ResourceDocument<PublicGuid> acutal = mapper.readValue(res.getBody(),
        new TypeReference<ResourceDocument<PublicGuid>>() {});
    assertEquals("XXYYZZ", acutal.getData().getAttributes().getPrefix());
    assertTrue(NumberSystem.BASE_36.containsAll(Chars
        .asList(acutal.getData().getAttributes().getCode().toCharArray())));
  }

}
