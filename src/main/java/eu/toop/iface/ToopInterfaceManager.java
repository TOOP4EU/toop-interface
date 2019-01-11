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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.helger.commons.concurrent.SimpleReadWriteLock;

/**
 * This class only contains the callback interfaces for DC/DP. They must be
 * assigned once on applicaiton startup.
 *
 * @author Philip Helger
 */
@ThreadSafe
public final class ToopInterfaceManager
{
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();
  private static IToopInterfaceDC s_aInterfaceDC;
  private static IToopInterfaceDP s_aInterfaceDP;

  private ToopInterfaceManager ()
  {}

  @Nonnull
  public static IToopInterfaceDC getInterfaceDC ()
  {
    final IToopInterfaceDC ret = s_aRWLock.readLocked ( () -> s_aInterfaceDC);
    if (ret == null)
      throw new IllegalStateException ("No DC interface present!");
    return ret;
  }

  public static void setInterfaceDC (@Nullable final IToopInterfaceDC interfaceDC)
  {
    s_aRWLock.writeLocked ( () -> s_aInterfaceDC = interfaceDC);
  }

  @Nonnull
  public static IToopInterfaceDP getInterfaceDP ()
  {
    final IToopInterfaceDP ret = s_aRWLock.readLocked ( () -> s_aInterfaceDP);
    if (ret == null)
      throw new IllegalStateException ("No DP interface present!");
    return ret;
  }

  public static void setInterfaceDP (@Nullable final IToopInterfaceDP interfaceDP)
  {
    s_aRWLock.writeLocked ( () -> s_aInterfaceDP = interfaceDP);
  }
}
