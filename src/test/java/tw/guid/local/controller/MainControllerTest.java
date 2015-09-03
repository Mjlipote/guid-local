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
package tw.guid.local.controller;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

import tw.guid.local.Application;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.web.Action;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class MainControllerTest {

  @Autowired
  Environment env;

  @Value("${guid.local.server.url}")
  String localServerUrl;

  @Value("${guid.central.server.url}")
  String centralServerUrl;

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
