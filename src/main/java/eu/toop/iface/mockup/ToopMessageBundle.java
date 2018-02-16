package eu.toop.iface.mockup;

/*
 * A Mockup TOOPMessageBundle
 */

public class ToopMessageBundle {

    private MSDataRequest msDataRequest;
    private MSDataResponse msDataResponse;
    private ToopDataRequest toopDataRequest;
    private ToopDataResponse toopDataResponse;

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

    public ToopDataRequest getToopDataRequest() {
        return toopDataRequest;
    }

    public void setToopDataRequest(ToopDataRequest toopDataRequest) {
        this.toopDataRequest = toopDataRequest;
    }

    public ToopDataResponse getToopDataResponse() {
        return toopDataResponse;
    }

    public void setToopDataResponse(ToopDataResponse toopDataResponse) {
        this.toopDataResponse = toopDataResponse;
    }
}
