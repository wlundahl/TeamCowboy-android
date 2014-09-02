package walter.teamcowboy.types;

/**
 * Class that represents the team cowboy error response object.
 */
public class ResponseError {
    private String errorCode;
    private int httpResponse;
    private String message;

    public ResponseError(String errorCode, int httpResponse, String message) {
        this.errorCode = errorCode;
        this.httpResponse = httpResponse;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public int getHttpResponse() {
        return httpResponse;
    }

    public String getMessage() {
        return message;
    }
}
