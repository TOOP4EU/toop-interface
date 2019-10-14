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

import javax.annotation.Nonnull;

import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.exchange.ToopRequestWithAttachments140;
import eu.toop.commons.exchange.ToopResponseWithAttachments140;

/**
 * This interface must be implemented by DP receiving components to retrieve
 * incoming requests (steps 2/4 and 3/4). The content of the request is an ASiC
 * archive containing a {@link TDETOOPRequestType}.
 *
 * @author Anton Wiklund
 * @author Philip Helger
 */
public interface IToopInterfaceDP
{
  /**
   * Invoked every time a TOOP Request Message is received (in step 2/4)
   *
   * @param aRequest
   *        Message object. Never <code>null</code>.
   * @throws IOException
   *         in case of processing errors
   */
  void onToopRequest (@Nonnull ToopRequestWithAttachments140 aRequest) throws IOException;

  /**
   * If the TOOP connector cannot handle the TOOP Response in step 3/4 it sends
   * it back to the DP for correction.
   *
   * @param aResponse
   *        Message object. Never <code>null</code>.
   * @throws IOException
   *         in case of processing errors
   */
  void onToopErrorResponse (@Nonnull ToopResponseWithAttachments140 aResponse) throws IOException;
}
