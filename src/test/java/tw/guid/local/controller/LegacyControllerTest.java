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
import tw.guid.local.Application;
import tw.guid.local.entity.SubprimeGuid;
import tw.guid.local.helper.CentralServerApiHelper;
import tw.guid.local.repository.SubprimeGuidRepository;
import tw.guid.local.security.HashCodeEncryptorHolder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class LegacyControllerTest {

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
  GuidClient guidClientWithNewPrefix;
  GuidClient guidClientWithNewPrefix1;
  GuidClient guidClientWithLegacyPrefix;
  GuidClient guidClientWithLegacyPrefix1;
  PII legacyPii;
  PII newPii;
  PII newPii1;
  PII newPii2;

  @Before
  public void setUp() throws Exception {
    guidClientWithNewPrefix =
        new GuidClient(new URI(localServerUrl), "admin", "password", "Test");
    guidClientWithNewPrefix1 =
        new GuidClient(new URI(localServerUrl), "admin", "password", "Guid");
    guidClientWithLegacyPrefix =
        new GuidClient(new URI(localServerUrl), "admin", "password", "NTUH14");
    guidClientWithLegacyPrefix1 =
        new GuidClient(new URI(localServerUrl), "admin", "password", "CCH24");
    legacyPii = new PII.Builder(new Name("明政", "李"), Sex.MALE,
        new Birthday(1979, 7, 21), new TWNationalId("E122371585")).build();
    newPii = new PII.Builder(new Name("大頭", "巫"), Sex.MALE,
        new Birthday(2000, 7, 7), new TWNationalId("A123456789")).build();
    newPii1 = new PII.Builder(new Name("光棍", "施"), Sex.MALE,
        new Birthday(2010, 11, 11), new TWNationalId("A123456789")).build();
    newPii2 = new PII.Builder(new Name("小賓", "鄭"), Sex.MALE,
        new Birthday(1977, 7, 7), new TWNationalId("A123456789")).build();
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
    assertEquals(gc.create(legacyPii), "VGH16-3AC3DEC6");
  }

  @Test
  public void testCreateRepeatSubprimeGuids()
      throws IOException, URISyntaxException {
    PrefixedHashBundle prefixedHashBundle = new PrefixedHashBundle();
    prefixedHashBundle.setPrefix("Test");
    prefixedHashBundle.setHash1(newPii.getHashcodes().get(0));
    prefixedHashBundle.setHash2(newPii.getHashcodes().get(1));
    prefixedHashBundle.setHash3(newPii.getHashcodes().get(2));
    assertFalse(subprimeGuidRepo.isExist(prefixedHashBundle));
    String spguid = guidClientWithNewPrefix.create(newPii);
    assertTrue(subprimeGuidRepo.findBySpguid(spguid) != null);
  }

  @Test
  public void testGroupingWithLegacyPrefixAndLegacyPii() throws Exception {
    List<PublicGuid> list = newArrayList();
    list.addAll(Arrays.asList(new PublicGuid("TpeVGH", "79C60E65"),
        new PublicGuid("VGH26", "AABE1DBF")));
    Collection<Set<String>> sets = CentralServerApiHelper
        .groupings(new URI(centralServerUrl), publicKey, list);
    Set<String> set = newHashSet();
    set.addAll(Arrays.asList("TpeVGH-79C60E65", "VGH26-AABE1DBF"));
    assertTrue(sets.contains(set));
  }

  @Test
  public void testGroupingWithLegacyPrefixAndNewPii() throws Exception {
    String[] guid = guidClientWithLegacyPrefix.create(newPii1).split("-");
    String[] guid1 = guidClientWithLegacyPrefix1.create(newPii1).split("-");
    List<PublicGuid> list = newArrayList();
    list.addAll(Arrays.asList(new PublicGuid(guid[0], guid[1]),
        new PublicGuid(guid1[0], guid1[1])));
    Collection<Set<String>> sets = CentralServerApiHelper
        .groupings(new URI(centralServerUrl), publicKey, list);
    Set<String> set = newHashSet();
    set.addAll(
        Arrays.asList(guid[0] + "-" + guid[1], guid1[0] + "-" + guid1[1]));
    assertTrue(sets.iterator().next().containsAll(set));
  }

  @Test
  public void testGroupingWithNewPrefixAndLegacyPii() throws Exception {
    String[] guid = guidClientWithNewPrefix.create(legacyPii).split("-");
    String[] guid1 = guidClientWithNewPrefix1.create(legacyPii).split("-");
    List<PublicGuid> list = newArrayList();
    list.addAll(Arrays.asList(new PublicGuid(guid[0], guid[1]),
        new PublicGuid(guid1[0], guid1[1])));
    Collection<Set<String>> sets = CentralServerApiHelper
        .groupings(new URI(centralServerUrl), publicKey, list);
    Set<String> set = newHashSet();
    set.addAll(
        Arrays.asList(guid[0] + "-" + guid[1], guid1[0] + "-" + guid1[1]));
    assertTrue(sets.iterator().next().containsAll(set));
  }

  @Test
  public void testGroupingWithNewPrefixAndNewPii() throws Exception {
    String[] guid = guidClientWithNewPrefix.create(newPii2).split("-");
    String[] guid1 = guidClientWithNewPrefix1.create(newPii2).split("-");
    List<PublicGuid> list = newArrayList();
    list.addAll(Arrays.asList(new PublicGuid(guid[0], guid[1]),
        new PublicGuid(guid1[0], guid1[1])));
    Collection<Set<String>> sets = CentralServerApiHelper
        .groupings(new URI(centralServerUrl), publicKey, list);
    Set<String> set = newHashSet();
    set.addAll(
        Arrays.asList(guid[0] + "-" + guid[1], guid1[0] + "-" + guid1[1]));
    assertTrue(sets.iterator().next().containsAll(set));
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
    String spguid = guidClientWithLegacyPrefix.create(legacyPii);
    assertTrue(subprimeGuidRepo.findBySpguid(spguid) != null);
  }

  @Test
  public void testFindSubprimeGuidByHashesAndPrefixUsingLegacyGuidClient()
      throws IOException {
    guidClientWithNewPrefix.create(legacyPii);
    assertNotNull(
        subprimeGuidRepo.findByHashcode1AndHashcode2AndHashcode3AndPrefix(
            legacyPii.getHashcodes().get(0).substring(0, 128).toUpperCase(),
            legacyPii.getHashcodes().get(1).substring(0, 128).toUpperCase(),
            legacyPii.getHashcodes().get(2).substring(0, 128).toUpperCase(),
            "Test"));
  }
}
