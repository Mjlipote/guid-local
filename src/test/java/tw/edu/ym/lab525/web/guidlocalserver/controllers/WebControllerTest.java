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

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

import tw.edu.ym.lab525.web.guidlocalserver.Application;
import tw.edu.ym.lab525.web.guidlocalserver.config.RestfulConfig;
import tw.edu.ym.lab525.web.guidlocalserver.helper.HttpActionHelper;
import tw.edu.ym.lab525.web.guidlocalserver.helper.HttpClientHelper;
import tw.edu.ym.lab525.web.guidlocalserver.models.Action;
import tw.edu.ym.lab525.web.guidlocalserver.models.SubprimeGuidRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class WebControllerTest {

  @Test
  public void testCreateSubprimeGuid() throws URISyntaxException, IOException {

    List<String> guidHashList = newArrayList();
    guidHashList.add(
        "f3042960fc9351d1ad99550817f892968207c6cb2539c6fd11b3258e815dedfe4f8f3f2a95c846b32aacf6201282921e2b93812587cc19752cfc9c0cf236a57b00");
    guidHashList.add(
        "e92e7cf25a726bb9f7aff7c36c31fa4a96b0014a3a7ce5018c6b84bc459df512653253d01e0742878ca7ddd7bd9c5179273fa915761a9ba84948fd85007cc8f900");
    guidHashList.add(
        "636ce21c211c33e6ee8e2f7590034fef8a3a5b3263c6d83af9c54b490175d649f11937e855509f57c986d1882cb5259372a37697899660afff8db6c8049de6a900");

    SubprimeGuidRequest scr = new SubprimeGuidRequest();
    scr.setPrefix("TEST");
    scr.setGuidHash(guidHashList);

    List<SubprimeGuidRequest> spGuidCreateRequestList = newArrayList();
    spGuidCreateRequestList.add(scr);

    assertEquals(
        HttpActionHelper.toPost(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL),
            Action.CREATE, spGuidCreateRequestList, false).getBody(),
        "TEST-Y3XZU2NG");

    // HttpClientHelper httpClientHelper = new HttpClientHelper(new
    // URI("http://localhost:8080"), "admin", "password");
    //
    // assertEquals(httpClientHelper.toPost(new
    // Gson().toJson(spGuidCreateRequestList), "create"), "TEST-Y3XZU2NG");

  }

  @Test
  public void testBatchSubprimeGuid()
      throws JsonProcessingException, URISyntaxException {

    List<String> guidHashList = newArrayList();
    List<String> guidHashList1 = newArrayList();
    guidHashList.add(
        "f3042960fc9351d1ad99550817f892968207c6cb2539c6fd11b3258e815dedfe4f8f3f2a95c846b32aacf6201282921e2b93812587cc19752cfc9c0cf236a57b00");
    guidHashList.add(
        "e92e7cf25a726bb9f7aff7c36c31fa4a96b0014a3a7ce5018c6b84bc459df512653253d01e0742878ca7ddd7bd9c5179273fa915761a9ba84948fd85007cc8f900");
    guidHashList.add(
        "636ce21c211c33e6ee8e2f7590034fef8a3a5b3263c6d83af9c54b490175d649f11937e855509f57c986d1882cb5259372a37697899660afff8db6c8049de6a900");

    guidHashList1.add(
        "4cfba4fd5fb65d523c05d7f3d6ae841cb65267e7881621e84a54abe9b81f1951ce3e57438b3d5672052b274c7a34bc55e1c67639242783957b9c6d3d0f9dbfbd00");
    guidHashList1.add(
        "f02cdfc2147cc0f56c176624e73ce6ad7766f2717f56ce6c5c5827ce145f32c8a2cf8f43135b1d9517e45f31fcba814cb36c03544e253fc340ce4a477b5ec96600");
    guidHashList1.add(
        "5d8fdc76d49b1e8c3668ce98f4ace74a53838535df0f82290158b487ce81de9c3f9f4d7cb8ebf94b1fe608c49b75700109396d4fd077148bd77aee11d52b013d00");

    SubprimeGuidRequest scr = new SubprimeGuidRequest();
    scr.setPrefix("TEST");
    scr.setGuidHash(guidHashList);

    SubprimeGuidRequest scr1 = new SubprimeGuidRequest();
    scr1.setPrefix("TEST");
    scr1.setGuidHash(guidHashList1);

    List<SubprimeGuidRequest> spGuidCreateRequestList = newArrayList();
    spGuidCreateRequestList.add(scr);
    spGuidCreateRequestList.add(scr1);

    assertEquals(
        HttpActionHelper.toPost(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL),
            Action.BATCH, spGuidCreateRequestList, true).getBody(),
        "[" + "TEST-Y3XZU2NG" + "," + "TEST-WDVWU2EQ" + "]");

  }

  @Test
  public void testUser() throws URISyntaxException, IOException {

    // assertEquals(HttpActionHelper.toGet(new
    // URI(RestfulConfig.GUID_LOCAL_SERVER_URL), Action.USER, "",
    // true).getBody(),
    // "[" +
    // "{\"username\":\"admin\",\"prefix\":\"AdminTest\",\"email\":\"admin@ym.com\",\"institute\":\"國立陽明大學\",\"jobTitle\":\"系統管理員\",\"telephone\":\"0910777666\",\"address\":\"國立陽明大學\",\"role\":\"ROLE_ADMIN\"}"
    // + "]");

    HttpClientHelper hch =
        new HttpClientHelper(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL),
            "admin", "password", Action.USER);
    assertEquals(hch.toGet(),
        "[" + "{\"username\":\"admin\",\"prefix\":\"AdminTest\",\"email\":\"admin@ym.com\",\"institute\":\"國立陽明大學\",\"jobTitle\":\"系統管理員\",\"telephone\":\"0910777666\",\"address\":\"國立陽明大學\",\"role\":\"ROLE_ADMIN\"}"
            + "]");

  }

  @Test
  public void testExist() throws URISyntaxException, IOException {

    assertTrue(HttpActionHelper
        .toGet(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL), Action.EXIST,
            "?hashcode1=f3042960fc9351d1ad99550817f892968207c6cb2539c6fd11b3258e815dedfe4f8f3f2a95c846b32aacf6201282921e2b93812587cc19752cfc9c0cf236a57b00&hashcode2=e92e7cf25a726bb9f7aff7c36c31fa4a96b0014a3a7ce5018c6b84bc459df512653253d01e0742878ca7ddd7bd9c5179273fa915761a9ba84948fd85007cc8f900&hashcode3=636ce21c211c33e6ee8e2f7590034fef8a3a5b3263c6d83af9c54b490175d649f11937e855509f57c986d1882cb5259372a37697899660afff8db6c8049de6a900",
            true)
        .getBody().equals("true"));

    assertTrue(HttpActionHelper
        .toGet(new URI(RestfulConfig.GUID_LOCAL_SERVER_URL), Action.EXIST,
            "?hashcode1=xxxxx&hashcode2=yyyyy&hashcode3=zzzzz", true)
        .getBody().equals("false"));

    HttpClientHelper hch =
        new HttpClientHelper(new URI(RestfulConfig.GUID_CENTRAL_SERVER_URL),
            "admin", "password", Action.EXIST);
    assertTrue(hch.toGet().equals("false"));
  }

}
