package walter.rest.client;

import android.util.Log;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public class RestClientImpl implements RestClient {
    // For some reason we need to use the Android logger.
    // TODO: figure out how to not do this.
    private static final String TAG = RestClientImpl.class.getName();

    // TODO: handle timeouts
    @Override
    public RestResponse makeRequest(RestRequest restRequest) {

        HttpProtocol scheme = restRequest.getProtocol();

        // TODO: move to the rest client wrapper
        Iterable<NameValuePair> nameValuePairs = Iterables.transform(restRequest.getParams().entrySet(),
                new Function<Map.Entry<String, String>, NameValuePair>() {
                    public NameValuePair apply(Map.Entry<String, String> param) {
                        return new BasicNameValuePair(param.getKey(), param.getValue());
                    }
                });
        URIBuilder uriBuilder = new URIBuilder().setScheme(scheme.toString()).setHost(restRequest.getHost()).setPath(
                restRequest.getPath());
        HttpRequestBase request;
        URI uri;
        switch (restRequest.getMethod()) {
            case POST:
                try {
                    uri = uriBuilder.build();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e); // TODO: fix this.
                }
                request = new HttpPost(uri);
                try {
                    List<NameValuePair> escapedParamsList = ImmutableList.<NameValuePair>builder().addAll(
                            nameValuePairs).build();
                    ((HttpPost) request).setEntity(new UrlEncodedFormEntity(escapedParamsList));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e); // TODO: fix this
                }
                break;
            case GET:
            default:
                try {
                    uri = uriBuilder.setParameters(Iterables.toArray(nameValuePairs, NameValuePair.class)).build();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e); // TODO: fix this.
                }
                request = new HttpGet(uri);
        }
        Log.i(TAG, uri.toString());

        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<RestResponse> testHandler = new ResponseHandler<RestResponse>() {
            public RestResponse handleResponse(HttpResponse httpResponse) throws IOException {
                StatusLine status = httpResponse.getStatusLine();
                HttpEntity entity = httpResponse.getEntity();
                return new RestResponse(status.getStatusCode(), status.getReasonPhrase(), EntityUtils.toString(entity));
            }
        };

        try {
            return httpClient.execute(request, testHandler);
        } catch (IOException e) {
            throw new RuntimeException(e); // TODO: fix this.
        }
    }
}

