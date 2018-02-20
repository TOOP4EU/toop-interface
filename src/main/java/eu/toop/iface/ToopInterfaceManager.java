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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.helger.commons.concurrent.SimpleReadWriteLock;

@ThreadSafe
public class ToopInterfaceManager {
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock();
  private static IToopInterfaceDC _interfaceDC;
  private static IToopInterfaceDP _interfaceDP;

  private ToopInterfaceManager() {
  }

  @Nonnull
  public static IToopInterfaceDC getInterfaceDC() throws IllegalStateException {
    final IToopInterfaceDC ret = s_aRWLock.readLocked(() -> _interfaceDC);
    if (ret == null) {
      throw new IllegalStateException("No DC interface present!");
    }
    return ret;
  }

  public static void setInterfaceDC(@Nullable final IToopInterfaceDC interfaceDC) {
    s_aRWLock.writeLocked(() -> _interfaceDC = interfaceDC);
  }

  @Nonnull
  public static IToopInterfaceDP getInterfaceDP() throws IllegalStateException {
    final IToopInterfaceDP ret = s_aRWLock.readLocked(() -> _interfaceDP);
    if (ret == null) {
      throw new IllegalStateException("No DP interface present!");
    }
    return ret;
  }

  public static void setInterfaceDP(@Nullable final IToopInterfaceDP interfaceDP) {
    s_aRWLock.writeLocked(() -> _interfaceDP = interfaceDP);
  }
}
