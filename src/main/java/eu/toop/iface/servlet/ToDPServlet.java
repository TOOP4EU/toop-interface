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

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.toop.commons.dataexchange.v140.TDETOOPRequestType;
import eu.toop.commons.exchange.ToopMessageBuilder;
import eu.toop.iface.ToopInterfaceManager;

@WebServlet ("/to-dp")
public class ToDPServlet extends HttpServlet
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ToDPServlet.class);

  @Override
  protected void doPost (@Nonnull final HttpServletRequest aHttpServletRequest,
                         @Nonnull final HttpServletResponse aHttpServletResponse) throws ServletException, IOException
  {
    // Parse ASiC
    final TDETOOPRequestType aRequestMsg = ToopMessageBuilder.parseRequestMessage (aHttpServletRequest.getInputStream ());
    if (aRequestMsg == null)
    {
      // The message content is invalid
      LOGGER.error ("The request does not contain an ASiC archive or the ASiC archive does not contain a TOOP Request Message!");
      aHttpServletResponse.setStatus (HttpServletResponse.SC_BAD_REQUEST);
    }
    else
    {
      // Call callback
      ToopInterfaceManager.getInterfaceDP ().onToopRequest (aRequestMsg);

      // Done - no content
      aHttpServletResponse.setStatus (HttpServletResponse.SC_NO_CONTENT);
    }
  }
}
