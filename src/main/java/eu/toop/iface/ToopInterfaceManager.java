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

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.helger.asic.SignatureHelper;
import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.commons.io.file.FileHelper;
import com.helger.commons.io.stream.NonBlockingByteArrayOutputStream;

import eu.toop.commons.concept.ConceptValue;
import eu.toop.commons.doctype.EToopDocumentType;
import eu.toop.commons.doctype.EToopProcess;
import eu.toop.commons.exchange.message.ToopMessageBuilder;
import eu.toop.commons.exchange.mock.MSDataRequest;
import eu.toop.iface.mockup.client.HttpClientInvoker;

@ThreadSafe
public class ToopInterfaceManager {
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();
  private static IToopInterfaceDC _interfaceDC;
  private static IToopInterfaceDP _interfaceDP;

  private ToopInterfaceManager () {
  }

  @Nonnull
  public static IToopInterfaceDC getInterfaceDC () throws IllegalStateException {
    final IToopInterfaceDC ret = s_aRWLock.readLocked ( () -> _interfaceDC);
    if (ret == null) {
      throw new IllegalStateException ("No DC interface present!");
    }
    return ret;
  }

  public static void setInterfaceDC (@Nullable final IToopInterfaceDC interfaceDC) {
    s_aRWLock.writeLocked ( () -> _interfaceDC = interfaceDC);
  }

  @Nonnull
  public static IToopInterfaceDP getInterfaceDP () throws IllegalStateException {
    final IToopInterfaceDP ret = s_aRWLock.readLocked ( () -> _interfaceDP);
    if (ret == null) {
      throw new IllegalStateException ("No DP interface present!");
    }
    return ret;
  }

  public static void setInterfaceDP (@Nullable final IToopInterfaceDP interfaceDP) {
    s_aRWLock.writeLocked ( () -> _interfaceDP = interfaceDP);
  }

  public static void requestConcepts (final List<ConceptValue> conceptList) throws IOException {
    final File keystoreFile = new File ("src/main/resources/demo-keystore.jks");

    final SignatureHelper aSH = new SignatureHelper (FileHelper.getInputStream (keystoreFile), "password", null,
                                                     "password");

    try (final NonBlockingByteArrayOutputStream archiveOutput = new NonBlockingByteArrayOutputStream ()) {
      final MSDataRequest msDataRequest = new MSDataRequest ("toop::sender", "DE",
                                                             EToopDocumentType.DOCTYPE3.getURIEncoded (),
                                                             EToopProcess.PROC.getURIEncoded (), conceptList);

      ToopMessageBuilder.createRequestMessage (msDataRequest, archiveOutput, aSH);

      // Send to DC (see DCInputServlet in toop-mp-webapp)
      final String destinationUrl = "http://mp.elonia.toop:8083/dcinput";
      HttpClientInvoker.httpClientCallNoResponse (destinationUrl, archiveOutput.toByteArray ());
    }
  }
}
