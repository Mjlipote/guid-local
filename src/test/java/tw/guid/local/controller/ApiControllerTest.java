/* Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 *
 * @author Ming-Jheng Li
 *
 */
package tw.guid.local.controller;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.github.wnameless.workbookaccessor.WorkbookReader;

import tw.guid.local.Application;
import tw.guid.local.repository.SubprimeGuidRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class ApiControllerTest {

  @Autowired
  Environment env;

  @Autowired
  SubprimeGuidRepository spGuidRepo;

  String localServerUrl = "https://localhost:8443";

  CloseableHttpClient httpClient;

  Set<String> set = newHashSet();

  @Before
  public void setUp() throws Exception {

    SSLContextBuilder builder = new SSLContextBuilder();
    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
    SSLConnectionSocketFactory sslsf =
        new SSLConnectionSocketFactory(builder.build());

    httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

    WorkbookReader reader0 =
        WorkbookReader.open("./src/main/resources/LegacyPrefixes.xlsx");

    for (List<String> row : reader0.withoutHeader().toLists()) {
      set.add(row.get(0));
    }

    set.add("BIOBANK");
    set.add("YMU");
    set.add("TEST");

  }

  @Test
  public void testValidationSubprimeGuidLength()
      throws URISyntaxException, ClientProtocolException, IOException {

    HttpGet httpGet = new HttpGet(localServerUrl + "/guids/"
        + Action.API_VALIDATION + "?spguid=" + "YM-75be3");

    httpGet.addHeader("Content-type", "application/json");

    CloseableHttpResponse postResponse = httpClient.execute(httpGet);
    HttpEntity entity = postResponse.getEntity();

    assertTrue(IOUtils.toString(entity.getContent()).equals("false"));

    HttpGet httpGet1 = new HttpGet(localServerUrl + "/guids/"
        + Action.API_VALIDATION + "?spguid=" + "YM-75be31f6kkp");

    httpGet1.addHeader("Content-type", "application/json");

    CloseableHttpResponse postResponse1 = httpClient.execute(httpGet1);
    HttpEntity entity1 = postResponse1.getEntity();

    assertTrue(IOUtils.toString(entity1.getContent()).equals("false"));

  }

  @Test
  public void testExistence()
      throws URISyntaxException, ClientProtocolException, IOException {

    HttpGet httpGet = new HttpGet(localServerUrl + "/guids/"
        + Action.API_EXISTENCE + "?subprimeGuid=" + "TEST-Y3XZU2NG");

    httpGet.addHeader("Content-type", "application/json");

    CloseableHttpResponse postResponse = httpClient.execute(httpGet);
    HttpEntity entity = postResponse.getEntity();

    assertTrue(IOUtils.toString(entity.getContent()).equals("true"));

  }

  @Test
  public void testGetAllPrefixListInLocalServerDatabase()
      throws URISyntaxException, IOException {

    HttpGet httpGet =
        new HttpGet(localServerUrl + "/guids/" + Action.API_PREFIX);

    httpGet.addHeader("Content-type", "application/json");

    CloseableHttpResponse postResponse = httpClient.execute(httpGet);
    HttpEntity entity = postResponse.getEntity();

    Map<String, Object> flattenJson =
        JsonFlattener.flattenAsMap(IOUtils.toString(entity.getContent()));

    Set<String> s = newHashSet();

    for (int i = 0; i < flattenJson.size(); i++) {
      s.add(flattenJson.get("[" + i + "]").toString());
    }

    assertTrue(set.containsAll(s));

  }
}
