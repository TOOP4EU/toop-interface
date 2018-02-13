package eu.toop.iface.mockup;

/*
 * A Mockup TOOPDataRequest
 */

import java.io.Serializable;

public class TOOPDataRequest implements Serializable {
    public String identifier;

    public TOOPDataRequest(String identifier) {
        this.identifier = identifier;
    }
}
