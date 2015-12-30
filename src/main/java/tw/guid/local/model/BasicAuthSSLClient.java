/*
 * Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package tw.guid.local.model;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * 
 * {@link BasicAuthSSLClient} is designed to easily create a {@link HttpClient}
 * which allows SSL and basic authentication.
 * 
 * @author Wei-Ming Wu
 *
 */
public final class BasicAuthSSLClient {

  private static final Logger logger =
      Logger.getLogger(BasicAuthSSLClient.class.getName());

  private BasicAuthSSLClient() {}

  /**
   * Returns a {@link HttpClient} which allows SSL and basic authentication.
   * 
   * @param username
   *          of the basic authentication
   * @param password
   *          of the basic authentication
   * @return a {@link HttpClient} which allows SSL and basic authentication
   */
  public static HttpClient create(String username, String password) {
    SSLContextBuilder builder = new SSLContextBuilder();
    try {
      builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
    } catch (NoSuchAlgorithmException e) {
      logger.log(Level.SEVERE, null, e);
    } catch (KeyStoreException e) {
      logger.log(Level.SEVERE, null, e);
    }

    SSLContext ctx = null;
    try {
      ctx = builder.build();
    } catch (KeyManagementException e) {
      logger.log(Level.SEVERE, null, e);
    } catch (NoSuchAlgorithmException e) {
      logger.log(Level.SEVERE, null, e);
    }

    SSLConnectionSocketFactory sslsf =
        new SSLConnectionSocketFactory(ctx, new HostnameVerifier() {

          @Override
          public boolean verify(String hostname, SSLSession session) {
            return true;
          }

        });

    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY,
        new UsernamePasswordCredentials(username, password));

    return HttpClients.custom()
        .setDefaultCredentialsProvider(credentialsProvider)
        .setSSLSocketFactory(sslsf).build();
  }

}
