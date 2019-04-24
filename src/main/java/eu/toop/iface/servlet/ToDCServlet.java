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

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;
import eu.toop.commons.exchange.AsicReadEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.collection.impl.CommonsArrayList;
import com.helger.commons.collection.impl.ICommonsList;

import eu.toop.commons.dataexchange.v140.TDETOOPResponseType;
import eu.toop.commons.exchange.AsicReadEntry;
import eu.toop.commons.exchange.ToopMessageBuilder140;
import eu.toop.iface.IToopInterfaceDC;
import eu.toop.iface.ToopInterfaceManager;

/**
 * This servlet can be included in Java DC implementations to receive messages
 * from the MP (step 4/4). It should read the received ASiC container and
 * extract the {@link TDETOOPResponseType} object. This is than forwarded to the
 * {@link IToopInterfaceDC} implementation registered in
 * {@link ToopInterfaceManager}.
 *
 * @author Philip Helger
 */
@WebServlet ("/to-dc")
public class ToDCServlet extends HttpServlet
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ToDCServlet.class);

  @Override
  protected void doPost (@Nonnull final HttpServletRequest aHttpServletRequest,
                         @Nonnull final HttpServletResponse aHttpServletResponse) throws ServletException, IOException
  {
    // Parse ASiC and also keep attachments
    final ICommonsList <AsicReadEntry> aAttachments = new CommonsArrayList <> ();
    final Serializable aParsedMsg = ToopMessageBuilder140.parseRequestOrResponse (aHttpServletRequest.getInputStream (),
                                                                                  aAttachments::add);
    if (aParsedMsg == null)
    {
      // The message content is invalid
      LOGGER.error ("The /to-dc request does not contain an ASiC archive or the ASiC archive does not contain a TOOP Response Message or a TOOP Error Message!");
      aHttpServletResponse.setStatus (HttpServletResponse.SC_BAD_REQUEST);
    }
    else
    {
      if (aParsedMsg instanceof TDETOOPResponseType)
      {
        // Call callback
        ToopInterfaceManager.getInterfaceDC ().onToopResponse ((TDETOOPResponseType) aParsedMsg, aAttachments);
        aHttpServletResponse.setStatus (HttpServletResponse.SC_ACCEPTED);
      }
      else
      {
        LOGGER.error ("The /to-dc request contains an ASiC archive but with unsupported payload of type " +
                      aParsedMsg.getClass ().getName ());
        aHttpServletResponse.setStatus (HttpServletResponse.SC_BAD_REQUEST);
      }
    }
  }
}
