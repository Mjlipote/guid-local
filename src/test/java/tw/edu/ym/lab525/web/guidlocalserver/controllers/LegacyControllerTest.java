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

  @Test
  public void testAuthenticate() throws IOException, URISyntaxException {
    GuidClient gc = new GuidClient(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL),
        "admin", "password");
    assertTrue(gc.authenticate());

  }

  @Test
  public void testCreate() throws IOException, URISyntaxException {
    GuidClient gc = new GuidClient(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL),
        "admin", "password", "AdminTest");
    PII pii = new PII.Builder(new Name("明政", "李"), Sex.MALE,
        new Birthday(1979, 7, 21), new TWNationalId("E122371585")).build();

    assertEquals(gc.create(pii), "AdminTest-T8A37BPL");

  }

}
