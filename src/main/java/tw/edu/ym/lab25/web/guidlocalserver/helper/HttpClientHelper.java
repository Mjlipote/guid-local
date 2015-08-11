package tw.edu.ym.lab25.web.guidlocalserver.helper;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import tw.edu.ym.lab525.web.guidlocalserver.models.Action;
import wmw.util.InputStreamUtil;

public final class HttpClientHelper {

  private static final String API_ROOT = "guid";

  private HttpClient httpClient;

  private final URI uri;
  private final String username;
  private final String password;

  private final Action action;

  public HttpClientHelper(URI url, String username, String password, Action action) {
    this.uri = url;
    this.username = username;
    this.password = password;
    this.action = action;
  }

  public String toGet() throws IOException {
    return toGet("");
  }

  public String toGet(String param) throws IOException {
    if (httpClient == null)
      httpClient = new DefaultHttpClient(getSSLClientConnectionManager(uri.getPort() == -1 ? 443 : uri.getPort()));

    HttpGet httpGet = new HttpGet("https://" + uri.getHost() + (uri.getPort() == -1 ? "" : ":" + uri.getPort()) + "/"
        + API_ROOT + "/" + action + param);
    httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "US-ASCII", false));

    HttpResponse response = checkStatusCode(httpClient.execute(httpGet));
    HttpEntity entity = response.getEntity();

    String json = InputStreamUtil.toString(entity.getContent());
    String result = new Gson().fromJson(json, String.class);
    return result;
  }

  public List<String> toPsot(List<Map<String, String>> listmap) throws IOException {
    if (httpClient == null)
      httpClient = new DefaultHttpClient(getSSLClientConnectionManager(uri.getPort() == -1 ? 443 : uri.getPort()));

    HttpPost httpPost = new HttpPost(
        "https://" + uri.getHost() + (uri.getPort() == -1 ? "" : ":" + uri.getPort()) + "/" + API_ROOT + "/" + action);
    httpPost
        .addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password), "US-ASCII", false));

    List<NameValuePair> nvps = newArrayList();

    for (Map<String, String> map : listmap) {
      Set<String> kset = map.keySet();
      for (String key : kset) {
        nvps.add(new BasicNameValuePair(key, map.get(key)));
      }
    }

    try {
      httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
      new GuidClientException(e);
    }
    HttpResponse response = checkStatusCode(httpClient.execute(httpPost));
    HttpEntity entity = response.getEntity();

    String json = InputStreamUtil.toString(entity.getContent());
    @SuppressWarnings("unchecked")
    List<String> result = new Gson().fromJson(json, List.class);
    return result;

  }

  private HttpResponse checkStatusCode(HttpResponse response) {
    if (response.getStatusLine().getStatusCode() != 200) {
      System.out.println("ERROR01");
      throw new GuidClientException(response.getStatusLine().toString());
    }
    return response;
  }

  private ClientConnectionManager getSSLClientConnectionManager(int port) {
    SSLContext sslContext = null;
    try {
      sslContext = SSLContext.getInstance("SSL");
    } catch (NoSuchAlgorithmException e) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
      new GuidClientException(e);
    }

    try {
      sslContext.init(null, new TrustManager[] { new X509TrustManager() {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {}

      } }, new SecureRandom());
    } catch (KeyManagementException e) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, e);
      new GuidClientException(e);
    }

    SSLSocketFactory sf = new SSLSocketFactory(sslContext, ALLOW_ALL_HOSTNAME_VERIFIER);
    Scheme httpsScheme = new Scheme("https", port, sf);
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(httpsScheme);

    return new BasicClientConnectionManager(schemeRegistry);
  }

  private final class GuidClientException extends RuntimeException {

    private static final long serialVersionUID = -3404267628838765596L;

    public GuidClientException(String message) {
      super(message);
    }

    public GuidClientException(Throwable cause) {
      super(cause);
    }

  }

}
