package walter.rest.client;

import java.util.Map;

public class RestRequest {
    private final HttpProtocol protocol;
    private final HttpMethod method;
    private final String host;
    private final String path;
    private final Map<String, String> params;

    // TODO: add a builder at some point.
    public RestRequest(HttpProtocol protocol, HttpMethod method, String host, String path, Map<String, String> params) {
        this.protocol = protocol;
        this.method = method;
        this.host = host;
        this.path = path;
        this.params = params;
    }

    public HttpProtocol getProtocol() {
        return protocol;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }

}
