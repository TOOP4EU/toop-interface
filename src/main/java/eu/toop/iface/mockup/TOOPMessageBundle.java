package eu.toop.iface.mockup;

/*
 * A Mockup TOOPMessageBundle
 */

public class TOOPMessageBundle {

    private MSDataRequest msDataRequest;
    private TOOPDataRequest toopDataRequest;

    public MSDataRequest getMsDataRequest() {
        return msDataRequest;
    }

    public void setMsDataRequest(MSDataRequest msDataRequest) {
        this.msDataRequest = msDataRequest;
    }

    public TOOPDataRequest getToopDataRequest() {
        return toopDataRequest;
    }

    public void setToopDataRequest(TOOPDataRequest toopDataRequest) {
        this.toopDataRequest = toopDataRequest;
    }
}
