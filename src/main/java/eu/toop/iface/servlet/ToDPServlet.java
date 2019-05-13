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
package eu.toop.iface.servlet;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;

import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.exchange.AsicReadEntry;
import eu.toop.commons.exchange.ToopMessageBuilder140;
import eu.toop.commons.exchange.ToopRequestWithAttachments140;
import eu.toop.commons.exchange.ToopResponseWithAttachments140;
import eu.toop.iface.ToopInterfaceManager;

@WebServlet ("/to-dp")
public class ToDPServlet extends HttpServlet
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ToDPServlet.class);

  @Override
  protected void doPost (@Nonnull final HttpServletRequest aHttpServletRequest,
                         @Nonnull final HttpServletResponse aHttpServletResponse) throws ServletException, IOException
  {
    if (LOGGER.isDebugEnabled ())
      LOGGER.debug ("Received new HTTP POST on /to-dp");

    // Parse ASiC
    final ICommonsList <AsicReadEntry> aAttachments = new CommonsArrayList <> ();
    final Serializable aMsg = ToopMessageBuilder140.parseRequestOrResponse (aHttpServletRequest.getInputStream (),
                                                                            aAttachments::add);
    if (aMsg == null)
    {
      // The message content is invalid
      LOGGER.error ("The /to-dp request does not contain an ASiC archive or the ASiC archive does not contain a TOOP Request or TOOP Response Message!");
      aHttpServletResponse.setStatus (HttpServletResponse.SC_BAD_REQUEST);
    }
    else
    {
      if (aMsg instanceof TDETOOPResponseType)
      {
        // If the DP is receiving a response, it is because the TC could not
        // handle the message from step 3/4

        if (LOGGER.isDebugEnabled ())
          LOGGER.debug ("Successfully parsed to a TOOP response");

        // Call error callback
        final ToopResponseWithAttachments140 aResponse = new ToopResponseWithAttachments140 ((TDETOOPResponseType) aMsg,
                                                                                             aAttachments);
        ToopInterfaceManager.getInterfaceDP ().onToopErrorResponse (aResponse);
      }
      else
      {
        if (LOGGER.isDebugEnabled ())
          LOGGER.debug ("Successfully parsed to a TOOP request");

        // Call callback
        final ToopRequestWithAttachments140 aRequest = new ToopRequestWithAttachments140 ((TDETOOPRequestType) aMsg,
                                                                                          aAttachments);
        ToopInterfaceManager.getInterfaceDP ().onToopRequest (aRequest);
      }

      // Done - no content
      aHttpServletResponse.setStatus (HttpServletResponse.SC_NO_CONTENT);
    }
  }
}
