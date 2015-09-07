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

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.wnameless.json.flattener.JsonFlattener;

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

    Set<String> set0 = newHashSet();
    set0.add("BBB-SG1XQETJ");
    set0.add("BBN-TPW7F2KM");
    set0.add("BBO-SJ6UO7NT");
    Set<String> set1 = newHashSet();
    set1.add("test-a082b088");
    set1.add("YM-e4ff7bf0");
    Set<String> set2 = newHashSet();
    set2.add("YM-69e4ef00");
    set2.add("test-2d992478");
    set2.add("NTU-87316faa");
    Set<String> set3 = newHashSet();
    set3.add("NTU-9b6bb15c");
    set3.add("YM-75be31f6");
    set3.add("test-31c3fa8e");
    Set<String> set4 = newHashSet();
    set4.add("BAK-RAML0ATR");
    set4.add("BAP-J36E0YUY");
    set4.add("BAA-TC00G36U");
    Set<String> set5 = newHashSet();
    set5.add("NTU-741fa23b");
    set5.add("test-deb7e9e9");
    set5.add("YM-9aca2291");

    Set<Set<String>> ssts = newHashSet();
    ssts.add(set0);
    ssts.add(set1);
    ssts.add(set2);
    ssts.add(set3);
    ssts.add(set4);
    ssts.add(set5);

    Map<String, Object> flattenJson =
        JsonFlattener.flattenAsMap(HttpActionHelper
            .toPost(new URI(centralServerUrl), Action.COMPARISON, list, false)
            .getBody());

    Set<Set<String>> sets = newHashSet();

    for (int i = 0; i < flattenJson.size(); i++) {
      Set<String> set = newHashSet();
      for (int j = 0; j < flattenJson.size(); j++) {
        if (flattenJson.get("[" + i + "]" + "[" + j + "]") != null) {
          set.add(flattenJson.get("[" + i + "]" + "[" + j + "]").toString());
        }
      }
      if (set.size() > 0) sets.add(set);
    }

    assertTrue(sets.containsAll(ssts));
  }

}
