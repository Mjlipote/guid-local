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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;

import tw.edu.ym.lab25.web.guidlocalserver.helper.HttpActionHelper;
import tw.edu.ym.lab525.web.guidlocalserver.Application;
import tw.edu.ym.lab525.web.guidlocalserver.config.RestfulActionConfig;
import tw.edu.ym.lab525.web.guidlocalserver.models.SubprimeGuidRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest

public class MainControllerTest {

  @Test
  public void testCreateSPGuid() throws JsonProcessingException, URISyntaxException {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("custom", "true");

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

    assertEquals(HttpActionHelper
        .toPost(new URI("http://localhost:8080"), RestfulActionConfig.CREATE, spGuidCreateRequestList).getBody(),
        "[" + "{\"spguid\":\"TEST-Y3XZU2NG\",\"prefix\":\"TEST\"}" + "]");

  }

}
