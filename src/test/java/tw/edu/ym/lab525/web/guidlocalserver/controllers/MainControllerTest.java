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

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import tw.guid.local.Application;
import tw.guid.local.models.Action;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class MainControllerTest {

  private static final Logger log =
      LoggerFactory.getLogger(MainControllerTest.class);

  private static String localServerUrl;

  @Before
  public void setUp() {
    Properties prop = new Properties();
    try {
      prop.load(new FileInputStream("serverhost.properties"));
      localServerUrl = prop.getProperty("local_server_url");
    } catch (FileNotFoundException e) {
      log.error(e.getMessage(), e);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
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

}
