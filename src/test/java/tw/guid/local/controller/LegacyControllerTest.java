/*Copyright(c)2015 ReiMed Co.to present.*All rights reserved.**@author Ming-Jheng Li**/package tw.guid.local.controller;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import tw.edu.ym.guid.client.GuidClient;
import tw.edu.ym.guid.client.PII;
import tw.edu.ym.guid.client.field.Birthday;
import tw.edu.ym.guid.client.field.Name;
import tw.edu.ym.guid.client.field.Sex;
import tw.edu.ym.guid.client.field.TWNationalId;
import tw.guid.central.core.PrefixedHashBundle;
import tw.guid.central.core.PublicGuid;
import tw.guid.local.ApplicationTest;
import tw.guid.local.entity.SubprimeGuid;
import tw.guid.local.helper.CentralServerApiHelper;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.security.HashCodeEncryptorHolder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationTest.class)
@WebIntegrationTest
public class LegacyControllerTest {

  PrefixedHashBundle legacyEncodable;
  PrefixedHashBundle legacyEncodable1;
  PrefixedHashBundle newEncodable;
  PrefixedHashBundle invalidEncodable;

  @Autowired
  HashCodeEncryptorHolder holder;

  static {
    // for localhost testing only
    HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> {
      return hostname.equals("localhost");
    });
  }

  @Autowired
  Environment env;

  @Autowired
  SubprimeGuidRepository subprimeGuidRepo;

  @Value("${guid.legacy.central.server}")
  String centralServerUrl;

  @Value("${guid.legacy.client.key}")
  String publicKey;
  String localServerUrl = "https://localhost:8443";
  GuidClient guidClient;
  PII pii;
  PII pii1;

  @Before
  public void setUp() throws Exception {
    legacyEncodable = new PrefixedHashBundle();
    legacyEncodable.setPrefix("CCH24");
    legacyEncodable.setHash1(
        "293ae00f7a0088e628d5386da01b40fa0491a82498af7a88ca8147a9ca601e8583b4b5c8e4af045cd2554da4af5b82a24f0bef25cea9867879ef05460fa386bf");
    legacyEncodable.setHash2(
        "cadfd5da66fecd9f6475614c2029fc35c1be89076f4262ccf1184c61e25dcbdd690dbc7f65b79b826ba2a840165b073aff038ee090f3fa1fe3fdf7cee2530a64");
    legacyEncodable.setHash3(
        "01033b50d8d2a018e466009bf2cbb6334853696c7406f0ac9845d4b9e543ef9f2c8f1c903a57298ae5211d1f5a64332a7d8bec8de7f54837ab6aeadde5dfb20f");
    legacyEncodable1 = new PrefixedHashBundle();
    legacyEncodable1.setPrefix("VGH16");
    legacyEncodable1.setHash1(
        "293ae00f7a0088e628d5386da01b40fa0491a82498af7a88ca8147a9ca601e8583b4b5c8e4af045cd2554da4af5b82a24f0bef25cea9867879ef05460fa386bf");
    legacyEncodable1.setHash2(
        "cadfd5da66fecd9f6475614c2029fc35c1be89076f4262ccf1184c61e25dcbdd690dbc7f65b79b826ba2a840165b073aff038ee090f3fa1fe3fdf7cee2530a64");
    legacyEncodable1.setHash3(
        "01033b50d8d2a018e466009bf2cbb6334853696c7406f0ac9845d4b9e543ef9f2c8f1c903a57298ae5211d1f5a64332a7d8bec8de7f54837ab6aeadde5dfb20f");
    newEncodable = new PrefixedHashBundle();
    newEncodable.setPrefix("XXYYZZ");
    newEncodable.setHash1(
        "193ae00f7a0088e628d5386da01b40fa0491a82498af7a88ca8147a9ca601e8583b4b5c8e4af045cd2554da4af5b82a24f0bef25cea9867879ef05460fa386bf");
    newEncodable.setHash2(
        "1adfd5da66fecd9f6475614c2029fc35c1be89076f4262ccf1184c61e25dcbdd690dbc7f65b79b826ba2a840165b073aff038ee090f3fa1fe3fdf7cee2530a64");
    newEncodable.setHash3(
        "11033b50d8d2a018e466009bf2cbb6334853696c7406f0ac9845d4b9e543ef9f2c8f1c903a57298ae5211d1f5a64332a7d8bec8de7f54837ab6aeadde5dfb20f");
    guidClient =
        new GuidClient(new URI(localServerUrl), "admin", "password", "Test");
    pii = new PII.Builder(new Name("大頭", "王"), Sex.MALE,
        new Birthday(2012, 1, 11), new TWNationalId("A123456789")).build();
    pii1 = new PII.Builder(new Name("光棍", "陳"), Sex.MALE,
        new Birthday(2011, 11, 11), new TWNationalId("A123456789")).build();
  }

  @Test
  public void testAuthentication() throws IOException, URISyntaxException {
    GuidClient gc =
        new GuidClient(new URI(localServerUrl), "admin", "password");
    assertTrue(gc.authenticate());
    GuidClient gc1 = new GuidClient(new URI(localServerUrl), "abc", "12345");
    assertFalse(gc1.authenticate());
  }

  @Test
  public void testGuidCreateWithOldPrefix()
      throws IOException, URISyntaxException {
    GuidClient gc =
        new GuidClient(new URI(localServerUrl), "admin", "password", "VGH16");
    PII pii = new PII.Builder(new Name("明政", "李"), Sex.MALE,
        new Birthday(1979, 7, 21), new TWNationalId("E122371585")).build();
    assertEquals(gc.create(pii), "VGH16-3AC3DEC6");
  }

  @Test
  public void testCreateRepeatSubprimeGuids()
      throws IOException, URISyntaxException {
    PrefixedHashBundle prefixedHashBundle = new PrefixedHashBundle();
    prefixedHashBundle.setPrefix("Test");
    prefixedHashBundle.setHash1(pii1.getHashcodes().get(0));
    prefixedHashBundle.setHash2(pii1.getHashcodes().get(1));
    prefixedHashBundle.setHash3(pii1.getHashcodes().get(2));
    assertFalse(subprimeGuidRepo.isExist(prefixedHashBundle));
    String spguid = guidClient.create(pii1);
    assertTrue(subprimeGuidRepo.findBySpguid(spguid) != null);
  }

  @Test
  public void testGrouping() throws Exception {
    List<PublicGuid> list = newArrayList();
    list.addAll(Arrays.asList(new PublicGuid("NTUH14", "C0A7AD5C"),
        new PublicGuid("PSEUDO", "1B11B50C")));
    Collection<Set<String>> sets = CentralServerApiHelper
        .groupings(new URI(centralServerUrl), publicKey, list);
    Set<String> set = newHashSet();
    set.addAll(Arrays.asList("NTUH14-C0A7AD5C", "PSEUDO-1B11B50C"));
    assertTrue(sets.contains(set));
  }

  @Test
  public void testFindSubprimeGuidByHashesAndPrefix() {
    SubprimeGuid spguid = new SubprimeGuid();
    spguid.setHashcode1(
        "f3042960fc9351d1ad99550817f892968207c6cb2539c6fd11b3258e815dedfe4f8f3f2a95c846b32aacf6201282921e2b93812587cc19752cfc9c0cf236a57b");
    spguid.setHashcode2(
        "e92e7cf25a726bb9f7aff7c36c31fa4a96b0014a3a7ce5018c6b84bc459df512653253d01e0742878ca7ddd7bd9c5179273fa915761a9ba84948fd85007cc8f9");
    spguid.setHashcode3(
        "636ce21c211c33e6ee8e2f7590034fef8a3a5b3263c6d83af9c54b490175d649f11937e855509f57c986d1882cb5259372a37697899660afff8db6c8049de6a9");
    spguid.setPrefix("TEST");
    spguid.setSpguid("TEST-Y3XZU2NG");
    assertNotNull(subprimeGuidRepo
        .findByHashcode1AndHashcode2AndHashcode3AndPrefix(spguid.getHashcode1(),
            spguid.getHashcode2(), spguid.getHashcode3(), "TEST"));
  }

  @Test
  public void testFindSubprimeGuidBySubprimeGuid() throws IOException {
    String spguid = guidClient.create(pii);
    assertTrue(subprimeGuidRepo.findBySpguid(spguid) != null);
  }

  @Test
  public void testFindSubprimeGuidByHashesAndPrefixUsingLegacyGuidClient()
      throws IOException {
    guidClient.create(pii);
    assertNotNull(
        subprimeGuidRepo.findByHashcode1AndHashcode2AndHashcode3AndPrefix(
            pii.getHashcodes().get(0).substring(0, 128).toUpperCase(),
            pii.getHashcodes().get(1).substring(0, 128).toUpperCase(),
            pii.getHashcodes().get(2).substring(0, 128).toUpperCase(), "Test"));
  }
}
