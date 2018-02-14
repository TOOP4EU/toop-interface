package eu.toop.iface.mockup;

/*
 * A Mockup TOOPDataResponse
 */

import java.io.Serializable;

public class TOOPDataResponse implements Serializable {
    public String identifier;

    public TOOPDataResponse(String identifier) {
        this.identifier = identifier;
    }
}
