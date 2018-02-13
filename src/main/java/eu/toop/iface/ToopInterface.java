package eu.toop.iface;

public class ToopInterface {

    static ITOOPInterfaceDC interfaceDC;
    static ITOOPInterfaceDP interfaceDP;

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
