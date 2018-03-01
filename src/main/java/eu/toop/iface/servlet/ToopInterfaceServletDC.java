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
package eu.toop.iface.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eu.toop.commons.dataexchange.TDETOOPDataResponseType;
import eu.toop.iface.IToopInterfaceDC;
import eu.toop.iface.ToopInterfaceManager;

/**
 * This servlet can be included in Java DC implementations to receive messages
 * from the MP. It should read the received ASiC container and extract the
 * {@link TDETOOPDataResponseType} object. This is than forwarded to the
 * {@link IToopInterfaceDC} implementation registered in
 * {@link ToopInterfaceManager}.
 *
 * @author Philip Helger
 */
public class ToopInterfaceServletDC extends HttpServlet {
  @Override
  protected void doPost (final HttpServletRequest req,
                         final HttpServletResponse resp) throws ServletException, IOException {
    ToopInterfaceManager.getInterfaceDC ().doPost (req, resp);
  }
}
