package tw.guid.local.helper;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import tw.guid.local.web.Action;
import wmw.util.InputStreamUtil;

public final class HttpClientHelper {

  private static final String API_ROOT = "guid";

  private final HttpClient httpClient = createSelfSignedHttpsClient();

  private final URI uri;
  private final String username;
  private final String password;

  private final Action action;

  public HttpClientHelper(URI url, String username, String password,
      Action action) {
    this.uri = checkNotNull(url);
    this.username = checkNotNull(username);
    this.password = checkNotNull(password);
    this.action = checkNotNull(action);
  }

  public String toGet() throws IOException {
    return toGet("");
  }

  public String toGet(String param) throws IOException {
    HttpGet httpGet = new HttpGet("https://" + uri.getHost()
        + (uri.getPort() == -1 ? "" : ":" + uri.getPort()) + "/" + API_ROOT
        + "/" + action + param);

    HttpResponse response = checkStatusCode(httpClient.execute(httpGet));
    HttpEntity entity = response.getEntity();

    String json = InputStreamUtil.toString(entity.getContent());
    String result = new Gson().fromJson(json, String.class);

    return result;
  }

  public List<String> toPsot(List<Map<String, String>> listmap)
      throws IOException {
    HttpPost httpPost = new HttpPost("https://" + uri.getHost()
        + (uri.getPort() == -1 ? "" : ":" + uri.getPort()) + "/" + API_ROOT
        + "/" + action);

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
    List<String> result =
        new Gson().getAdapter(new TypeToken<List<String>>() {}).fromJson(json);

    return result;
  }

  private HttpResponse checkStatusCode(HttpResponse response) {
    if (response.getStatusLine().getStatusCode() != 200)
      throw new GuidClientException(response.getStatusLine().toString());

    return response;
  }

  private CloseableHttpClient createSelfSignedHttpsClient() {
    SSLConnectionSocketFactory sslsf = null;
    SSLContextBuilder builder = new SSLContextBuilder();
    try {
      builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
      sslsf = new SSLConnectionSocketFactory(builder.build());
    } catch (NoSuchAlgorithmException | KeyStoreException
        | KeyManagementException e) {
      e.printStackTrace();
    }

    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY,
        new UsernamePasswordCredentials(username, password));

    return HttpClients.custom()
        .setDefaultCredentialsProvider(credentialsProvider)
        .setSSLSocketFactory(sslsf).build();
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
