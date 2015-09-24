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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

import com.github.wnameless.json.flattener.JsonFlattener;

import tw.guid.local.Application;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.web.Action;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class ApiControllerTest {

  @Autowired
  Environment env;

  @Value("${guid.local.server.url}")
  String localServerUrl;

  @Test
  public void testValidationSubprimeGuidLength() throws URISyntaxException {

    assertTrue(
        HttpActionHelper.toGet(new URI(localServerUrl), Action.API_VALIDATION,
            "?spguid=" + "YM-75be3", true).getBody().equals("false"));

    assertTrue(
        HttpActionHelper.toGet(new URI(localServerUrl), Action.API_VALIDATION,
            "?spguid=" + "YM-75be31f6kkp", true).getBody().equals("false"));
  }

  @Test
  public void testExistence() throws URISyntaxException {

    assertTrue(
        HttpActionHelper.toGet(new URI(localServerUrl), Action.API_EXISTENCE,
            "?subprimeGuid=TEST-Y3XZU2NG", true).getBody().equals("true"));

  }

  @Test
  public void testGetAllPrefixListInLocalServerDatabase()
      throws URISyntaxException, IOException {

    Set<String> set = newHashSet();
    set.add("YMU");
    set.add("BIOBANK");
    set.add("TEST");

    Map<String, Object> flattenJson =
        JsonFlattener.flattenAsMap(HttpActionHelper
            .toGet(new URI(localServerUrl), Action.API_PREFIX, "", true)
            .getBody());

    Set<String> s = newHashSet();

    for (int i = 0; i < flattenJson.size(); i++) {
      s.add(flattenJson.get("[" + i + "]").toString());
    }

    assertTrue(set.containsAll(s));

  }
}
