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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.helger.commons.http.CHttp;

/**
 * Abstract servlet class that correctly returns "method not allowed"
 *
 * @author Philip Helger
 */
public abstract class AbstractToopInterfaceServlet extends HttpServlet
{
  protected static void methodNotSupported (final HttpServletResponse aResp) throws IOException
  {
    aResp.sendError (CHttp.HTTP_METHOD_NOT_ALLOWED);
  }

  @Override
  protected void doDelete (final HttpServletRequest aReq, final HttpServletResponse aResp) throws ServletException,
                                                                                           IOException
  {
    methodNotSupported (aResp);
  }

  @Override
  protected void doGet (final HttpServletRequest aReq, final HttpServletResponse aResp) throws ServletException,
                                                                                        IOException
  {
    methodNotSupported (aResp);
  }

  @Override
  protected void doHead (final HttpServletRequest aReq, final HttpServletResponse aResp) throws ServletException,
                                                                                         IOException
  {
    methodNotSupported (aResp);
  }

  @Override
  protected void doOptions (final HttpServletRequest aReq, final HttpServletResponse aResp) throws ServletException,
                                                                                            IOException
  {
    methodNotSupported (aResp);
  }

  @Override
  protected void doPost (final HttpServletRequest aReq, final HttpServletResponse aResp) throws ServletException,
                                                                                         IOException
  {
    methodNotSupported (aResp);
  }

  @Override
  protected void doPut (final HttpServletRequest aReq, final HttpServletResponse aResp) throws ServletException,
                                                                                        IOException
  {
    methodNotSupported (aResp);
  }

  @Override
  protected void doTrace (final HttpServletRequest aReq, final HttpServletResponse aResp) throws ServletException,
                                                                                          IOException
  {
    methodNotSupported (aResp);
  }
}
