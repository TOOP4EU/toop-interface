/**
 * Copyright (C) 2018 toop.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.toop.iface.util;

import java.io.IOException;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;

import com.helger.commons.ValueEnforcer;
import com.helger.httpclient.HttpClientFactory;
import com.helger.httpclient.HttpClientManager;
import com.helger.httpclient.response.ResponseHandlerByteArray;

/**
 * This class can be used to send something from DC or DP to the
 * MessageProcessor.
 *
 * @author Philip Helger
 *
 */
public final class HttpClientInvoker {
  private HttpClientInvoker() {
  }

  public static <T> void httpClientCall(@Nonnull final String sDestinationURL, @Nonnull final byte[] aDataToSend,
      @Nonnull final ResponseHandler<T> aResponseHandler, final Consumer<T> aResultHandler) throws IOException {
    ValueEnforcer.notEmpty(sDestinationURL, "DestinationURL");
    ValueEnforcer.notNull(aDataToSend, "DataToSend");
    ValueEnforcer.notNull(aResponseHandler, "ResponseHandler");
    ValueEnforcer.notNull(aResultHandler, "ResultHandler");

    final HttpClientFactory aHCFactory = new HttpClientFactory();
    // For proxy etc
    aHCFactory.setUseSystemProperties(true);

    try (final HttpClientManager aMgr = new HttpClientManager(aHCFactory)) {
      final HttpPost aPost = new HttpPost(sDestinationURL);
      aPost.setEntity(new ByteArrayEntity(aDataToSend));

      final T aResponse = aMgr.execute(aPost, aResponseHandler);
      aResultHandler.accept(aResponse);
    }
  }

  public static void httpClientCallNoResponse(@Nonnull final String sDestinationURL, @Nonnull final byte[] aDataToSend)
      throws IOException {
    ValueEnforcer.notEmpty(sDestinationURL, "DestinationURL");
    ValueEnforcer.notNull(aDataToSend, "DataToSend");

    httpClientCall(sDestinationURL, aDataToSend, new ResponseHandlerByteArray(), x -> {
      // do nothing
    });
  }
}
