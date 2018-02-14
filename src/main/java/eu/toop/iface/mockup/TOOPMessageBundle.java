package eu.toop.iface.mockup;

/*
 * A Mockup TOOPMessageBundle
 */

public class TOOPMessageBundle {

    private MSDataRequest msDataRequest;
    private MSDataResponse msDataResponse;
    private TOOPDataRequest toopDataRequest;
    private TOOPDataResponse toopDataResponse;

    public MSDataRequest getMsDataRequest() {
        return msDataRequest;
    }

    public void setMsDataRequest(MSDataRequest msDataRequest) {
        this.msDataRequest = msDataRequest;
    }

    public MSDataResponse getMsDataResponse() {
        return msDataResponse;
    }

    public void setMsDataResponse(MSDataResponse msDataResponse) {
        this.msDataResponse = msDataResponse;
    }

    public TOOPDataRequest getToopDataRequest() {
        return toopDataRequest;
    }

    public void setToopDataRequest(TOOPDataRequest toopDataRequest) {
        this.toopDataRequest = toopDataRequest;
    }

    public TOOPDataResponse getToopDataResponse() {
        return toopDataResponse;
    }

    public void setToopDataResponse(TOOPDataResponse toopDataResponse) {
        this.toopDataResponse = toopDataResponse;
    }
}
