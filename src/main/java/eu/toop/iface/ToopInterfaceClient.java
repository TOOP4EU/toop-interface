/**
 * Copyright (C) 2018-2019 toop.eu
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
import com.helger.commons.ValueEnforcer;
import com.helger.commons.annotation.Nonempty;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;
import com.helger.commons.string.StringHelper;
import com.helger.commons.wrapper.Wrapper;
import com.helger.httpclient.response.ResponseHandlerByteArray;
import com.helger.pd.searchapi.PDSearchAPIReader;
import com.helger.pd.searchapi.v1.ResultListType;

import eu.toop.commons.codelist.EPredefinedDocumentTypeIdentifier;
import eu.toop.commons.codelist.EPredefinedProcessIdentifier;
import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.dataexchange.v140.TDEDataRequestSubjectType;
import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.error.ToopErrorException;
import eu.toop.commons.exchange.ToopMessageBuilder140;
import eu.toop.iface.util.HttpClientInvoker;
import oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_21.IdentifierType;

@ThreadSafe
public final class ToopInterfaceClient
{
  private ToopInterfaceClient ()
  {}

  /**
   * Execute step 1/4
   *
   * @param aRequestSubject
   *        Data request subject. May not be <code>null</code>.
   * @param sDCCountryCode
   *        DC country code as e.g. "SE"
   * @param sDPCountryCode
   *        DP country code as e.g. "SE"
   * @param aSenderParticipantID
   *        Participant ID of the sender as used by R2D2. May not be
   *        <code>null</code>.
   * @param eDocumentTypeID
   *        Document type ID to request. May not be <code>null</code>.
   * @param eProcessID
   *        Process ID to request. May not be <code>null</code>.
   * @param aConceptList
   *        list of concepts to be queried
   * @throws IOException
   *         in case of HTTP error
   * @throws ToopErrorException
   *         For known TOOP errors
   */
  @Deprecated
  public static void createRequestAndSendToToopConnector (@Nonnull final TDEDataRequestSubjectType aRequestSubject,
                                                          @Nonnull final String sDCCountryCode,
                                                          @Nonnull final String sDPCountryCode,
                                                          @Nonnull @Nonempty final IdentifierType aSenderParticipantID,
                                                          @Nonnull final EPredefinedDocumentTypeIdentifier eDocumentTypeID,
                                                          @Nonnull final EPredefinedProcessIdentifier eProcessID,
                                                          @Nullable final List <? extends ConceptValue> aConceptList) throws IOException,
                                                                                                                      ToopErrorException
  {
    // TODO this is still mock!
    final TDETOOPRequestType aRequest = ToopMessageBuilder140.createMockRequest (aRequestSubject,
                                                                                 sDCCountryCode,
                                                                                 sDPCountryCode,
                                                                                 aSenderParticipantID,
                                                                                 eDocumentTypeID,
                                                                                 eProcessID,
                                                                                 aConceptList);
    sendRequestToToopConnector (aRequest);
  }

  /**
   * Create a request, wrap it in an ASiC and send it to DP TOOP Connector,
   * using the configured connector URL.
   *
   * @param aRequest
   *        Request object. May not be <code>null</code>.
   * @throws IOException
   *         In case sending or the like fails
   * @throws ToopErrorException
   *         For known TOOP errors
   * @since 0.9.2
   */
  public static void sendRequestToToopConnector (@Nonnull final TDETOOPRequestType aRequest) throws IOException,
                                                                                             ToopErrorException
  {
    sendRequestToToopConnector (aRequest, ToopInterfaceConfig.getToopConnectorDCUrl ());
  }

  /**
   * Create a request, wrap it in an ASiC and send it to DP TOOP Connector,
   * using the provided URL.
   *
   * @param aRequest
   *        Request object. May not be <code>null</code>.
   * @param sTargetURL
   *        Target URL. May not be <code>null</code>.
   * @throws IOException
   *         In case sending or the like fails
   * @throws ToopErrorException
   *         For known TOOP errors
   * @since 0.10.0
   */
  public static void sendRequestToToopConnector (@Nonnull final TDETOOPRequestType aRequest,
                                                 @Nonnull final String sTargetURL) throws IOException,
                                                                                   ToopErrorException
  {
    ValueEnforcer.notNull (aRequest, "Request");
    ValueEnforcer.notNull (sTargetURL, "TargetURL");

    final SignatureHelper aSH = new SignatureHelper (ToopInterfaceConfig.getKeystoreType (),
                                                     ToopInterfaceConfig.getKeystorePath (),
                                                     ToopInterfaceConfig.getKeystorePassword (),
                                                     ToopInterfaceConfig.getKeystoreKeyAlias (),
                                                     ToopInterfaceConfig.getKeystoreKeyPassword ());

    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
    {
      ToopMessageBuilder140.createRequestMessageAsic (aRequest, aBAOS, aSH);

      // Send to DC (see FromDCServlet in toop-connector-webapp)
      HttpClientInvoker.httpClientCallNoResponse (sTargetURL, aBAOS.toByteArray ());
    }
  }

  /**
   * Create a response, wrap it in an ASiC and send it to DP TOOP Connector,
   * using the configured connector URL.
   *
   * @param aResponse
   *        Response object. May not be <code>null</code>.
   * @throws IOException
   *         In case sending or the like fails
   * @throws ToopErrorException
   *         For known TOOP errors
   */
  public static void sendResponseToToopConnector (@Nonnull final TDETOOPResponseType aResponse) throws IOException,
                                                                                                ToopErrorException
  {
    sendResponseToToopConnector (aResponse, ToopInterfaceConfig.getToopConnectorDPUrl ());
  }

  /**
   * Create a response, wrap it in an ASiC and send it to DP TOOP Connector,
   * using the provided URL.
   *
   * @param aResponse
   *        Response object. May not be <code>null</code>.
   * @param sTargetURL
   *        Target URL. May not be <code>null</code>.
   * @throws IOException
   *         In case sending or the like fails
   * @throws ToopErrorException
   *         For known TOOP errors
   * @since 0.10.0
   */
  public static void sendResponseToToopConnector (@Nonnull final TDETOOPResponseType aResponse,
                                                  @Nonnull final String sTargetURL) throws IOException,
                                                                                    ToopErrorException
  {
    ValueEnforcer.notNull (aResponse, "Response");
    ValueEnforcer.notNull (sTargetURL, "TargetURL");

    final SignatureHelper aSH = new SignatureHelper (ToopInterfaceConfig.getKeystoreType (),
                                                     ToopInterfaceConfig.getKeystorePath (),
                                                     ToopInterfaceConfig.getKeystorePassword (),
                                                     ToopInterfaceConfig.getKeystoreKeyAlias (),
                                                     ToopInterfaceConfig.getKeystoreKeyPassword ());

    try (final NonBlockingByteArrayOutputStream aBAOS = new NonBlockingByteArrayOutputStream ())
    {
      ToopMessageBuilder140.createResponseMessageAsic (aResponse, aBAOS, aSH);

      // Send to DP (see FromDPServlet in toop-connector-webapp)
      HttpClientInvoker.httpClientCallNoResponse (sTargetURL, aBAOS.toByteArray ());
    }
  }

  @Nullable
  public static ResultListType searchDataProvider (@Nonnull @Nonempty final String sCountryCode,
                                                   @Nullable final String sDocTypeStr)
  {

    ValueEnforcer.notEmpty (sCountryCode, "CountryCode");

    String sQueryURI = ToopInterfaceConfig.getToopConnectorUrl () + "/search-dp/" + sCountryCode;
    if (StringHelper.hasText (sDocTypeStr))
      sQueryURI += '/' + sDocTypeStr;

    try
    {
      final Wrapper <ResultListType> aResponse = new Wrapper <> ();
      HttpClientInvoker.httpClientCallGet (sQueryURI,
                                           new ResponseHandlerByteArray (),
                                           aBytes -> aResponse.set (PDSearchAPIReader.resultListV1 ().read (aBytes)));
      return aResponse.get ();
    }
    catch (final Exception e)
    {
      throw new IllegalStateException (e);
    }
  }
}
