package walter.teamcowboy;

import static walter.teamcowboy.Nonce.NonceImpl;

import android.util.Log;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Iterables;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import walter.rest.client.RestClient;
import walter.rest.client.RestClientImpl;
import walter.rest.client.RestRequest;
import walter.rest.client.RestResponse;
import walter.teamcowboy.request.APIMethod;
import walter.teamcowboy.types.AuthUser;
import walter.teamcowboy.types.ResponseError;
import walter.teamcowboy.types.Team;
import walter.teamcowboy.types.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

public class TeamCowboyClient {
    private static final String TAG = TeamCowboyClient.class.getName();

    private static final String SIGNATURE_TEMPLATE = "%s|%s|%s|%s|%s|%s";

    private static final String ENDPOINT = "api.teamcowboy.com";
    private static final String PATH = "/v1/";

    // Required params names
    private static final String TIMESTAMP = "timestamp";
    private static final String NONCE = "nonce";

    private final Gson gson;

    public TeamCowboyClient() {
        gson = new GsonBuilder().create();
    }

    public Map<String, Object> makeTestRequest() {
        return makeRequest(APIMethod.GET_TEST, ImmutableMap.of("testParam", "test123"), Map.class);
    }

    public Map<String, Object> makeTestPostRequest() {
        return makeRequest(APIMethod.POST_TEST, ImmutableMap.of("testParam", "test123"), Map.class);
    }

    /**
     * Log the user into the TeamCowboy service for the given credentials.
     */
    public AuthUser login(String username, String password) {
        return makeRequest(APIMethod.SIGN_IN, ImmutableMap.of("username", username, "password", password),
                AuthUser.class);
    }

    public User getUserDetails(AuthUser authUser) {
        return makeRequest(APIMethod.USER, ImmutableMap.of("userToken", authUser.getToken()), User.class);
    }

    public List<Team> getTeams(AuthUser authUser) {
        return makeRequestForList(APIMethod.GET_TEAMS, ImmutableMap.of("userToken", authUser.getToken()), Team.class);
    }

    public Map<String, String> createParams(APIMethod method, Map<String, String> methodParams) {
        return createParams(method, methodParams, false);
    }

    public Map<String, String> createParams(APIMethod method, Map<String, String> methodParams,
            boolean shouldIncludePlaintextSignature) {
        ImmutableSortedMap.Builder<String, String> params = ImmutableSortedMap.naturalOrder();
        params.putAll(methodParams);

        params.put("api_key", getPublicKey());
        params.put("method", method.getApiName());

        Nonce nonce = getNonce();
        params.put(TIMESTAMP, nonce.getTimestamp());
        params.put(NONCE, nonce.getNonce());
        if (shouldAddResponseType()) {
            params.put("response_type", "json");
        }

        // Generate signature.
        Pair<String, String> signature = createSignature(method, params.build());
        params.put("sig", signature.getLeft());
        if (shouldIncludePlaintextSignature) {
            params.put("plaintextSignature", signature.getRight());
        }

        return params.build();
    }

    @VisibleForTesting
    protected boolean shouldAddResponseType() {
        return true;
    }

    @VisibleForTesting
    protected Nonce getNonce() {
        return new NonceImpl();
    }

    @VisibleForTesting
    protected String getPublicKey() {
        return TeamCowboyClientConfig.PUBLIC_KEY;
    }

    @VisibleForTesting
    protected String getPrivateKey() {
        return TeamCowboyClientConfig.PRIVATE_KEY;
    }

    // TODO: reevaluate Optional.
    private <T> T makeRequest(APIMethod method, Map<String, String> params, Class<T> resultClass) {
        Optional<JsonElement> bodyMaybe = makeRequest(method, params);
        if (!bodyMaybe.isPresent()) {
            return null;
        }

        JsonElement body = bodyMaybe.get();
        return gson.fromJson(body, resultClass);
    }

    private <T> List<T> makeRequestForList(APIMethod method, Map<String, String> params, final Class<T> resultClass) {
        final Optional<JsonElement> bodyMaybe = makeRequest(method, params);
        if (!bodyMaybe.isPresent()) {
            return null;
        }

        final Function<JsonElement, T> deserializer = new Function<JsonElement, T>() {
            @Override
            public T apply(JsonElement element) {
                return gson.fromJson(element, resultClass);
            }
        };
        JsonElement body = bodyMaybe.get();
        if (body.isJsonArray()) {
            JsonArray bodyArray = body.getAsJsonArray();
            Iterable<T> deserializedObjects = Iterables.transform(bodyArray, deserializer);
            return ImmutableList.<T>builder().addAll(deserializedObjects).build();
        }

        return ImmutableList.of(deserializer.apply(body));
    }

    private Optional<JsonElement> makeRequest(APIMethod method, Map<String, String> params) {
        RestClient client = new RestClientImpl();
        RestRequest request = new RestRequest(method.getHTTPProtocol(), method.getHttpMethod(), ENDPOINT, PATH,
                createParams(method, params));
        RestResponse response = client.makeRequest(request);

        if (response == null) {
            Log.e(TAG, "Null response returned from the RESTClient for request: " +
                    ToStringBuilder.reflectionToString(request));
            return Optional.absent();
        }

        int statusCode = response.getStatusCode();
        Log.i(TAG, "Recieved " + statusCode + " saying " + response.getReason() + " with payload:\n" +
                response.getPayload());

        JsonObject json = gson.fromJson(response.getPayload(), JsonObject.class);

        boolean isSuccess = json.get("success").getAsBoolean();
        Log.i(TAG, "Request success=" + isSuccess);

        JsonElement body = json.get("body");
        Log.i(TAG, "Response body:\n" + body);

        if (!isSuccess) {
            ResponseError error = gson.fromJson(body.getAsJsonObject().get("error"), ResponseError.class);
            Log.e(TAG, "Observed error: " + ToStringBuilder.reflectionToString(error));
            return Optional.absent();
        }
        return Optional.of(body);
    }

    private Pair<String, String> createSignature(APIMethod method, ImmutableSortedMap<String, String> params) {
        Iterable<NameValuePair> nameValuePairIterable = Iterables.transform(params.entrySet(),
                new Function<Map.Entry<String, String>, NameValuePair>() {
                    public NameValuePair apply(Map.Entry<String, String> param) {
                        return new BasicNameValuePair(StringUtils.lowerCase(param.getKey()),
                                StringUtils.lowerCase(param.getValue()));
                    }
                });

        List<NameValuePair> nameValuePairs = ImmutableList.<NameValuePair>builder().addAll(
                nameValuePairIterable).build();
        String query = URLEncodedUtils.format(nameValuePairs, "UTF-8");

        String plaintext = String.format(SIGNATURE_TEMPLATE, getPrivateKey(), method.getHttpMethod().name(),
                method.getApiName(), params.get(TIMESTAMP), params.get(NONCE), query);

        Log.i(TAG, "Plaintext signature: " + plaintext);

        MessageDigest sha1;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            // TODO: fix this.
            throw new RuntimeException(e);
        }

        sha1.reset();
        sha1.update(plaintext.getBytes());
        byte[] digested = sha1.digest();

        Formatter hexFormatter = new Formatter();
        for (byte digestByte : digested) {
            hexFormatter.format("%02x", digestByte);
        }
        String signature = hexFormatter.toString();
        hexFormatter.close();
        return Pair.of(signature, plaintext);
    }
}
