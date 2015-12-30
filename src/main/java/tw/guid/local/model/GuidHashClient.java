/*
 * Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package tw.guid.local.model;

import static com.github.wnameless.jsonapi.JsonApi.resourceDocument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.BaseEncoding.base64Url;

import java.io.IOException;
import java.net.URI;
import java.security.PublicKey;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.jsonapi.ResourceDocument;

import tw.guid.central.core.PrefixedHashBundle;
import tw.guid.central.core.PublicGuid;

/**
 * 
 * GuidClient can create and query GUIDs from a GUID server.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class GuidHashClient {

  private static final String API_ENDPOINT = "/api/v1";

  private final String apiUrl;
  private final HttpClient httpClient;
  private final ObjectMapper mapper = new ObjectMapper();

  /**
   * Creates a GUID client.
   * 
   * @param centralServer
   *          a central server URI
   * @param publicKey
   *          for authentication
   */
  public GuidHashClient(URI centralServer, PublicKey publicKey) {
    this(centralServer, base64Url().encode(publicKey.getEncoded()));
  }

  /**
   * Creates a GUID client.
   * 
   * @param centralServer
   *          a central server URI
   * @param base64PublicKey
   *          for authentication
   */
  public GuidHashClient(URI centralServer, String base64PublicKey) {
    apiUrl = centralServer.toString().replaceFirst("/$", "") + API_ENDPOINT;
    httpClient =
        BasicAuthSSLClient.create("token", checkNotNull(base64PublicKey));
  }

  /**
   * Computes a GUID by given PII.
   * 
   * @param prefix
   *          of the GUID
   * @param phb
   *          a {@link PrefixedHashBundle}
   * @return a public GUID
   * @throws ClientProtocolException
   * @throws IOException
   */
  public PublicGuid compute(PrefixedHashBundle phb)
      throws ClientProtocolException, IOException {
    ResourceDocument<PrefixedHashBundle> req = resourceDocument(phb, "guids");

    HttpPost post = new HttpPost(apiUrl + "/guids");
    post.addHeader("content-type", "application/json");
    post.setEntity(new StringEntity(mapper.writeValueAsString(req)));

    HttpResponse res = httpClient.execute(post);
    String json = IOUtils.toString(res.getEntity().getContent());
    ResourceDocument<PublicGuid> guid = mapper.readValue(json,
        new TypeReference<ResourceDocument<PublicGuid>>() {});

    return guid.getData().getAttributes();
  }

}
