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
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tw.guid.local.Application;
import tw.guid.local.helper.HttpActionHelper;
import tw.guid.local.models.Action;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class ApiControllerTest {

  private static final Logger log =
      LoggerFactory.getLogger(ApiControllerTest.class);

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
  public void testValidateNovelSubprimeGuid() throws URISyntaxException {

    assertTrue(
        HttpActionHelper.toGet(new URI(localServerUrl), Action.API_VALIDATE,
            "?spguid=" + "BBB-SG1XQETJ", true).getBody().equals("true"));

    assertTrue(
        HttpActionHelper.toGet(new URI(localServerUrl), Action.API_VALIDATE,
            "?spguid=" + "BBB-SG1XQETH", true).getBody().equals("false"));
  }

  @Test
  public void testValidateOldSubprimeGuid() throws URISyntaxException {

    assertTrue(
        HttpActionHelper.toGet(new URI(localServerUrl), Action.API_VALIDATE,
            "?spguid=" + "YM-75be31f6", true).getBody().equals("true"));

    assertTrue(
        HttpActionHelper.toGet(new URI(localServerUrl), Action.API_VALIDATE,
            "?spguid=" + "YM-75be31f0", true).getBody().equals("false"));
  }

  @Test
  public void testValidateSubprimeGuidLength() throws URISyntaxException {

    assertTrue(
        HttpActionHelper.toGet(new URI(localServerUrl), Action.API_VALIDATE,
            "?spguid=" + "YM-75be3", true).getBody().equals("false"));

    assertTrue(
        HttpActionHelper.toGet(new URI(localServerUrl), Action.API_VALIDATE,
            "?spguid=" + "YM-75be31f6kkp", true).getBody().equals("false"));
  }

  @Test
  public void testExist() throws URISyntaxException, IOException {

    assertTrue(HttpActionHelper.toGet(new URI(localServerUrl), Action.API_EXIST,
        "?hashcode1="
            + "f3042960fc9351d1ad99550817f892968207c6cb2539c6fd11b3258e815dedfe4f8f3f2a95c846b32aacf6201282921e2b93812587cc19752cfc9c0cf236a57b00"
            + "&hashcode2="
            + "e92e7cf25a726bb9f7aff7c36c31fa4a96b0014a3a7ce5018c6b84bc459df512653253d01e0742878ca7ddd7bd9c5179273fa915761a9ba84948fd85007cc8f900"
            + "&hashcode3="
            + "636ce21c211c33e6ee8e2f7590034fef8a3a5b3263c6d83af9c54b490175d649f11937e855509f57c986d1882cb5259372a37697899660afff8db6c8049de6a900",
        true).getBody().equals("true"));

    assertTrue(HttpActionHelper
        .toGet(new URI(localServerUrl), Action.API_EXIST, "?hashcode1=" + "xxx"
            + "&hashcode2=" + "yyy" + "&hashcode3=" + "zzz", true)
        .getBody().equals("false"));

  }

  @Test
  public void testGetAllPrefixListInLocalServer()
      throws URISyntaxException, IOException {
    assertEquals(HttpActionHelper
        .toGet(new URI(localServerUrl), Action.API_PREFIX, "", true).getBody(),
        "[" + "\"UserTest\"" + "," + "\"AdminTest\"" + "]");
  }
}
