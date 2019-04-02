package eu.toop.iface.util;

import com.helger.commons.io.stream.NonBlockingStringReader;
import eu.toop.iface.dpsearch.ResultListType;

import javax.xml.bind.JAXBElement;

public class JaxbMarshaller {

    /**
     * Unmarshal a ResultListType instance from the given string
     * @param str
     * @return
     * @throws javax.xml.bind.JAXBException
     */
    public static ResultListType jaxbUnmarshalFromString (final String str)
            throws javax.xml.bind.JAXBException {
        final javax.xml.bind.JAXBContext jaxbCtx =
                javax.xml.bind.JAXBContext.newInstance (ResultListType.class.getPackage ().getName ());
        final javax.xml.bind.Unmarshaller unmarshaller = jaxbCtx.createUnmarshaller ();
        final NonBlockingStringReader stringReader = new NonBlockingStringReader(str);
        final JAXBElement ret = (JAXBElement) unmarshaller.unmarshal (stringReader);
        return (ResultListType) ret.getValue ();
    }
}
