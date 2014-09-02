package walter.teamcowboy.request;

import org.apache.commons.lang3.Validate;
import walter.rest.client.HttpMethod;
import walter.rest.client.HttpProtocol;

import static walter.rest.client.HttpMethod.GET;
import static walter.rest.client.HttpMethod.POST;
import static walter.rest.client.HttpProtocol.HTTP;
import static walter.rest.client.HttpProtocol.HTTPS;

/**
 * Api methods for teamcowboy.
 */
public enum APIMethod {
    GET_TEST("Test_GetRequest", GET),
    POST_TEST("Test_PostRequest", POST),
    SIGN_IN("Auth_GetUserToken", POST, HTTPS),
    USER("User_Get", GET),
    GET_TEAMS("User_GetTeams", GET),
    GET_TEAM("Team_Get", GET);

    private final String apiName;
    private final HttpMethod method;
    private final HttpProtocol httpProtocol;

    private APIMethod(String apiName, HttpMethod method) {
        this(apiName, method, HTTP);
    }

    private APIMethod(String apiName, HttpMethod method, HttpProtocol httpProtocol) {
        Validate.notBlank(apiName);
        this.apiName = apiName;

        Validate.notNull(method);
        this.method = method;

        Validate.notNull(httpProtocol);
        this.httpProtocol = httpProtocol;
    }

    /**
     * Get the name of the teamcowboy api of this api method.
     * @return the internal teamcowboy name of the method.
     */
    public String getApiName() {
        return apiName;
    }

    /**
     * Gets the {@link walter.rest.client.HttpMethod} used in this method's request
     * @return {@link walter.rest.client.HttpMethod} for this request.
     */
    public HttpMethod getHttpMethod() {
        return method;
    }

    /**
     * The {@link walter.rest.client.HttpProtocol} needed for this request.
     * @return {@link walter.rest.client.HttpProtocol} needed for this request.
     */
    public HttpProtocol getHTTPProtocol() {
        return httpProtocol;
    }

    /**
     * Determines whether or not this method should use HTTPS
     * @return <code>true</code> if this request should use HTTPS
     */
    public boolean isSecure() {
        return httpProtocol.isSecure();
    }


}
