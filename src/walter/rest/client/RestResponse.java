package walter.rest.client;

public class RestResponse {
    private final int statusCode;
    private final String reason;
    private final String payload;

    public RestResponse(int statusCode, String reason, String payload) {
        this.statusCode = statusCode;
        this.reason = reason;
        this.payload = payload;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReason() {
        return reason;
    }

    public String getPayload() {
        return payload;
    }
}


