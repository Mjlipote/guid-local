/**
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
package tw.edu.ym.lab525.web.guidlocalserver.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tw.edu.ym.guid.client.GuidClient;
import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Name;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.guid.local.Application;
import tw.guid.local.config.RestfulConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class LegacyControllerTest {
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

  // String hc1 =
  // "f3042960fc9351d1ad99550817f892968207c6cb2539c6fd11b3258e815dedfe4f8f3f2a95c846b32aacf6201282921e2b93812587cc19752cfc9c0cf236a57b00";
  // String hc2 =
  // "e92e7cf25a726bb9f7aff7c36c31fa4a96b0014a3a7ce5018c6b84bc459df512653253d01e0742878ca7ddd7bd9c5179273fa915761a9ba84948fd85007cc8f900";
  // String hc3 =
  // "636ce21c211c33e6ee8e2f7590034fef8a3a5b3263c6d83af9c54b490175d649f11937e855509f57c986d1882cb5259372a37697899660afff8db6c8049de6a900";

  @Test
  public void testAuthenticate() throws IOException, URISyntaxException {
    GuidClient gc = new GuidClient(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL),
        "admin", "password");
    assertTrue(gc.authenticate());

    // assertTrue(
    // HttpActionHelper.toGet(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL),
    // Action.AUTHENTICATE, "", true).getBody().equals("true"));

  }

  @Test
  public void testCreate() throws IOException, URISyntaxException {
    GuidClient gc = new GuidClient(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL),
        "admin", "password", "AdminTest");
    PII pii = new PII.Builder(new Name("明政", "李"), Sex.MALE,
        new Birthday(1979, 7, 21), new TWNationalId("E122371585")).build();

    assertEquals(gc.create(pii), "AdminTest-T8A37BPL");

    // List<String> jsonHashes = newArrayList();
    // jsonHashes.add(hc1);
    // jsonHashes.add(hc2);
    // jsonHashes.add(hc3);
    //
    // assertEquals(
    // HttpActionHelper
    // .toPost(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL), Action.CREATE,
    // "?prefix=" + "TEST" + "&" + "jsonHashes=" + "[" + hc1 + ","
    // + hc2 + "," + hc3 + "]")
    // .getBody(),
    // "[" + "{\"spguid\":\"TEST-Y3XZU2NG\",\"prefix\":\"TEST\"}" + "]");

  }

}
