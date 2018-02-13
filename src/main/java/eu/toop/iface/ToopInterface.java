package eu.toop.iface;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

import com.helger.commons.concurrent.SimpleReadWriteLock;

@ThreadSafe
public class ToopInterface {
	private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock();
	private static ITOOPInterfaceDC _interfaceDC;
	private static ITOOPInterfaceDP _interfaceDP;

	private ToopInterface() {
	}

	@Nonnull
	public static ITOOPInterfaceDC getInterfaceDC() throws IllegalStateException {
		final ITOOPInterfaceDC ret = s_aRWLock.readLocked(() -> _interfaceDC);
		if (ret == null) {
			throw new IllegalStateException("No DC interface present!");
		}
		return ret;
	}

	public static void setInterfaceDC(@Nullable final ITOOPInterfaceDC interfaceDC) {
		s_aRWLock.writeLocked(() -> _interfaceDC = interfaceDC);
	}

	@Nonnull
	public static ITOOPInterfaceDP getInterfaceDP() throws IllegalStateException {
		final ITOOPInterfaceDP ret = s_aRWLock.readLocked(() -> _interfaceDP);
		if (ret == null) {
			throw new IllegalStateException("No DP interface present!");
		}
		return ret;
	}
	public static void setInterfaceDP(@Nullable final ITOOPInterfaceDP interfaceDP) {
		s_aRWLock.writeLocked(() -> _interfaceDP = interfaceDP);
	}
}
