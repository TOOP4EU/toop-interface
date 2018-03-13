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
package eu.toop.iface;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.helger.asic.SignatureHelper;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.io.resourceprovider.DefaultResourceProvider;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;

import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.TDETOOPDataRequestType;
import eu.toop.commons.dataexchange.TDETOOPDataResponseType;
import eu.toop.commons.doctype.EToopDocumentType;
import eu.toop.commons.doctype.EToopProcess;
import eu.toop.commons.exchange.ToopMessageBuilder;
import eu.toop.iface.util.HttpClientInvoker;

@ThreadSafe
public final class ToopInterfaceClient {
  private ToopInterfaceClient () {
  }

  /**
   * Execute step 1/4
   *
   * @param sSenderParticipantID
   *          Participant ID of the sender as used by R2D2. May not be
   *          <code>null</code>.
   * @param sCountryCode
   *          Destination country code ID (as in "SE"). May not be
   *          <code>null</code>.
   * @param eDocumentTypeID
   *          Document type ID to request. May not be <code>null</code>.
   * @param eProcessID
   *          Process ID to request. May not be <code>null</code>.
   * @param conceptList
   *          list of concepts to be queried
   * @throws IOException
   *           in case of HTTP error
   */
  public static void createRequestAndSendToToopConnector (@Nonnull @Nonempty final String sSenderParticipantID,
                                                          @Nonnull @Nonempty final String sCountryCode,
                                                          @Nonnull final EToopDocumentType eDocumentTypeID,
                                                          @Nonnull final EToopProcess eProcessID,
                                                          @Nullable final List<? extends ConceptValue> conceptList) throws IOException {
    final SignatureHelper aSH = new SignatureHelper (new DefaultResourceProvider ().getInputStream (ToopInterfaceConfig.getKeystorePath ()),
                                                     ToopInterfaceConfig.getKeystorePassword (),
                                                     ToopInterfaceConfig.getKeystoreKeyAlias (),
                                                     ToopInterfaceConfig.getKeystoreKeyPassword ());

    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ()) {
      final TDETOOPDataRequestType aRequest = ToopMessageBuilder.createMockRequest (sSenderParticipantID, sCountryCode,
                                                                                    eDocumentTypeID, eProcessID,
                                                                                    conceptList);

      ToopMessageBuilder.createRequestMessage (aRequest, aBAOS, aSH);

      // Send to DC (see FromDCServlet in toop-connector-webapp)
      final String aFromDCUrl = ToopInterfaceConfig.getToopConnectorDCUrl ();
      HttpClientInvoker.httpClientCallNoResponse (aFromDCUrl, aBAOS.toByteArray ());
    }
  }

  /**
   * Create a response, wrap it in an ASiC and send it to DP TOOP Connector
   * 
   * @param aResponse
   *          Response object
   * @throws IOException
   *           In case sending or the like fails
   */
  public static void sendResponseToToopConnector (@Nonnull final TDETOOPDataResponseType aResponse) throws IOException {
    final SignatureHelper aSH = new SignatureHelper (new DefaultResourceProvider ().getInputStream (ToopInterfaceConfig.getKeystorePath ()),
                                                     ToopInterfaceConfig.getKeystorePassword (),
                                                     ToopInterfaceConfig.getKeystoreKeyAlias (),
                                                     ToopInterfaceConfig.getKeystoreKeyPassword ());

    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ()) {
      ToopMessageBuilder.createResponseMessage (aResponse, aBAOS, aSH);

      // Send to DP (see FromDPServlet in toop-connector-webapp)
      final String sFromDPUrl = ToopInterfaceConfig.getToopConnectorDPUrl ();
      HttpClientInvoker.httpClientCallNoResponse (sFromDPUrl, aBAOS.toByteArray ());
    }
  }
}
