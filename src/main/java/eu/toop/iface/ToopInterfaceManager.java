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
