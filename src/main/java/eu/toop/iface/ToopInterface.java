package eu.toop.iface;

import eu.toop.iface.mockup.MSDataRequest;
import eu.toop.iface.mockup.TOOPDataRequest;
import eu.toop.iface.mockup.TOOPMessageBundle;
import no.difi.asic.AsicWriter;
import no.difi.asic.AsicWriterFactory;
import org.apache.commons.lang3.SerializationUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ToopInterface {

    static ITOOPInterfaceDC interfaceDC;
    static ITOOPInterfaceDP interfaceDP;

    public static void sendTOOPMessageBundle(TOOPMessageBundle bundle) throws IOException {
        System.out.println("Trying to send a bundle");
    }

    public static ITOOPInterfaceDC getInterfaceDC() throws IllegalStateException {
        if (interfaceDC == null) {
            throw new IllegalStateException();
        }
        return interfaceDC;
    }

    public static void setInterfaceDC(ITOOPInterfaceDC interfaceDC) {
        ToopInterface.interfaceDC = interfaceDC;
    }

    public static ITOOPInterfaceDP getInterfaceDP() throws IllegalStateException {
        if (interfaceDP == null) {
            throw new IllegalStateException();
        }
        return interfaceDP;
    }

    public static void setInterfaceDP(ITOOPInterfaceDP interfaceDP) {
        ToopInterface.interfaceDP = interfaceDP;
    }
}
