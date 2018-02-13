package eu.toop.iface.mockup;

/*
 * A Mockup MSDataRequest
 */

import java.io.Serializable;

public class MSDataRequest implements Serializable {
    public String identifier;

    public MSDataRequest(String identifier) {
        this.identifier = identifier;
    }
}
