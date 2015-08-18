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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import tw.guid.local.Application;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.models.Action;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class MainControllerTest {

  private String localServerUrl;
  private String centralServerUrl;

  @Before
  public void setUp() throws FileNotFoundException, IOException {
    Properties prop = new Properties();
    prop.load(new FileInputStream("serverhost.properties"));
    localServerUrl = prop.getProperty("local_server_url");
    centralServerUrl = prop.getProperty("central_server_url");
  }

  @Test
  public void testUsers() throws IOException, URISyntaxException {

    RestTemplate restTemplate = new TestRestTemplate();

    ResponseEntity<String> res = restTemplate
        .getForEntity(localServerUrl + "/" + Action.USERS + "/" + "?username="
            + "" + "&prefix=" + "" + "&role=" + "", String.class);

    Document doc = Jsoup.parse(res.getBody());

    assertTrue(
        doc.getElementsByTag("span").get(6).text().contains("ROLE_ADMIN"));
    assertTrue(doc.getElementsByTag("span").get(7).text().contains("admin"));
    assertTrue(
        doc.getElementsByTag("span").get(8).text().contains("AdminTest"));
    assertTrue(doc.getElementsByTag("span").get(9).text().contains("國立陽明大學"));
    assertTrue(doc.getElementsByTag("span").get(10).text().contains("系統管理員"));
    assertTrue(
        doc.getElementsByTag("span").get(11).text().contains("admin@ym.com"));

  }

  @Test
  public void testComparison()
      throws JsonProcessingException, URISyntaxException {

    List<String> list = Arrays.asList("BIS-UD498UYV", "BBB-SG1XQETJ",
        "BBN-TPW7F2KM", "BBO-SJ6UO7NT", "BAA-TC00G36U", "BAP-J36E0YUY",
        "BAK-RAML0ATR", "YM-75be31f6", "test-31c3fa8e", "NTU-9b6bb15c",
        "test-deb7e9e9", "YM-9aca2291", "YM-69e4ef00", "NTU-741fa23b",
        "test-2d992478", "NTU-87316faa", "YM-e4ff7bf0", "test-a082b088");

    assertEquals(
        HttpActionHelper
            .toPost(new URI(centralServerUrl), Action.COMPARISON, list, false)
            .getBody(),
        ("[" + "[\"BBB-SG1XQETJ\",\"BBN-TPW7F2KM\",\"BBO-SJ6UO7NT\"]" + ","
            + "[\"test-a082b088\",\"YM-e4ff7bf0\"]" + ","
            + "[\"YM-69e4ef00\",\"test-2d992478\",\"NTU-87316faa\"]" + ","
            + "[\"NTU-9b6bb15c\",\"YM-75be31f6\",\"test-31c3fa8e\"]" + ","
            + "[\"BAK-RAML0ATR\",\"BAP-J36E0YUY\",\"BAA-TC00G36U\"]" + ","
            + "[\"NTU-741fa23b\",\"test-deb7e9e9\",\"YM-9aca2291\"]" + "]"));

  }

}
