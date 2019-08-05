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

import javax.annotation.CheckForSigned;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.commons.debug.GlobalDebug;
import com.helger.commons.state.ESuccess;
import com.helger.security.keystore.EKeyStoreType;
import com.helger.security.keystore.IKeyStoreType;
import com.helger.settings.exchange.configfile.ConfigFile;
import com.helger.settings.exchange.configfile.ConfigFileBuilder;

/**
 * This class contains global configuration elements for the toop-interface.
 *
 * @author Philip Helger, BRZ, AT
 */
@Immutable
public final class ToopInterfaceConfig
{
  private static final Logger LOGGER = LoggerFactory.getLogger (ToopInterfaceConfig.class);
  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static ConfigFile s_aConfigFile;

  static
  {
    reloadConfiguration ();
  }

  /**
   * The name of the primary system property which points to the
   * <code>toop-interface.properties</code> files
   */
  public static final String SYSTEM_PROPERTY_TOOP_INTERFACE_PROPERTIES_PATH = "toop.interface.properties.path";

  /** The default primary properties file to load */
  public static final String PATH_PRIVATE_TOOP_INTERFACE_PROPERTIES = "private-toop-interface.properties";

  /** The default secondary properties file to load */
  public static final String PATH_TOOP_INTERFACE_PROPERTIES = "toop-interface.properties";

  /**
   * Reload the configuration file. It checks if the system property
   * {@link #SYSTEM_PROPERTY_TOOP_INTERFACE_PROPERTIES_PATH} is present and if
   * so, tries it first, than {@link #PATH_PRIVATE_TOOP_INTERFACE_PROPERTIES} is
   * checked and finally the {@link #PATH_TOOP_INTERFACE_PROPERTIES} path is
   * checked.
   *
   * @return {@link ESuccess}
   */
  @Nonnull
  public static ESuccess reloadConfiguration ()
  {
    final ConfigFileBuilder aCFB = new ConfigFileBuilder ().addPathFromEnvVar ("TOOP_INTERFACE_CONFIG")
                                                           .addPathFromSystemProperty (SYSTEM_PROPERTY_TOOP_INTERFACE_PROPERTIES_PATH)
                                                           .addPath (PATH_PRIVATE_TOOP_INTERFACE_PROPERTIES)
                                                           .addPath (PATH_TOOP_INTERFACE_PROPERTIES);

    return s_aRWLock.writeLocked ( () -> {
      s_aConfigFile = aCFB.build ();
      if (s_aConfigFile.isRead ())
      {
        LOGGER.info ("Read TOOP interface properties from " + s_aConfigFile.getReadResource ().getPath ());
        return ESuccess.SUCCESS;
      }

      LOGGER.warn ("Failed to read TOOP interface properties from " + aCFB.getAllPaths ());
      return ESuccess.FAILURE;
    });
  }

  private ToopInterfaceConfig ()
  {}

  /**
   * @return The configuration file. Never <code>null</code>.
   */
  @Nonnull
  public static ConfigFile getConfigFile ()
  {
    return s_aRWLock.readLocked ( () -> s_aConfigFile);
  }

  public static boolean isGlobalDebug ()
  {
    return getConfigFile ().getAsBoolean ("global.debug", GlobalDebug.isDebugMode ());
  }

  public static boolean isGlobalProduction ()
  {
    return getConfigFile ().getAsBoolean ("global.production", GlobalDebug.isProductionMode ());
  }

  @Nullable
  public static String getToopConnectorUrl ()
  {
    return getConfigFile ().getAsString ("toop.connector.url");
  }

  @Nullable
  public static String getToopConnectorDCUrl ()
  {
    return getConfigFile ().getAsString ("toop.connector.dc.url");
  }

  @Nullable
  public static String getToopConnectorDPUrl ()
  {
    return getConfigFile ().getAsString ("toop.connector.dp.url");
  }

  @Nullable
  public static IKeyStoreType getKeystoreType ()
  {
    final String sType = getConfigFile ().getAsString ("toop.keystore.type");
    return EKeyStoreType.getFromIDCaseInsensitiveOrDefault (sType, EKeyStoreType.JKS);
  }

  @Nullable
  public static String getKeystorePath ()
  {
    return getConfigFile ().getAsString ("toop.keystore.path");
  }

  @Nullable
  public static String getKeystorePassword ()
  {
    return getConfigFile ().getAsString ("toop.keystore.password");
  }

  @Nullable
  public static String getKeystoreKeyAlias ()
  {
    return getConfigFile ().getAsString ("toop.keystore.key.alias");
  }

  @Nullable
  public static String getKeystoreKeyPassword ()
  {
    return getConfigFile ().getAsString ("toop.keystore.key.password");
  }

  public static boolean isUseHttpSystemProperties ()
  {
    return getConfigFile ().getAsBoolean ("toop.http.usesysprops", false);
  }

  public static boolean isProxyServerEnabled ()
  {
    return getConfigFile ().getAsBoolean ("toop.proxy.enabled", false);
  }

  @Nullable
  public static String getProxyServerAddress ()
  {
    // Scheme plus hostname or IP address
    return getConfigFile ().getAsString ("toop.proxy.address");
  }

  @CheckForSigned
  public static int getProxyServerPort ()
  {
    return getConfigFile ().getAsInt ("toop.proxy.port", -1);
  }

  @Nullable
  public static String getProxyServerNonProxyHosts ()
  {
    // Separated by pipe
    return getConfigFile ().getAsString ("toop.proxy.non-proxy");
  }

  public static boolean isTLSTrustAll ()
  {
    return getConfigFile ().getAsBoolean ("toop.tls.trustall", false);
  }
}
