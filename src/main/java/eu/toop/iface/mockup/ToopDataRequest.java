package eu.toop.iface.mockup;

/*
 * A Mockup TOOPDataRequest
 */

import java.io.Serializable;

public class ToopDataRequest implements Serializable {
    public String identifier;

    public ToopDataRequest(String identifier) {
        this.identifier = identifier;
    }
}
