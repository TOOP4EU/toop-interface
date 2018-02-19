package eu.toop.iface.mockup.client;

import java.io.IOException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.w3c.dom.Document;

import com.helger.httpclient.HttpClientFactory;
import com.helger.httpclient.HttpClientManager;
import com.helger.httpclient.response.ResponseHandlerXml;

public final class SendToMPClient {
  private SendToMPClient() {
  }

  public static void httpClientCall(final byte[] aDataTosend) throws IOException {
    final HttpClientFactory aHCFactory = new HttpClientFactory();
    // For proxy etc
    aHCFactory.setUseSystemProperties(true);

    try (final HttpClientManager aMgr = new HttpClientManager(aHCFactory)) {
      final HttpPost aPost = new HttpPost("http://localhost:8080/interface/dc");
      aPost.setEntity(new ByteArrayEntity(aDataTosend));

      // Assume response is XML
      final ResponseHandlerXml aRH = new ResponseHandlerXml();
      final Document aResponse = aMgr.execute(aPost, aRH);
      if (aResponse != null) {
        // TODO
      }
    }
  }
}
