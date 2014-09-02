package walter.rest.client;

import org.apache.commons.lang3.StringUtils;

/**
 * Enum with the types of http protocols.
 */
public enum HttpProtocol {
    HTTP(80), HTTPS(443, true);

    private final boolean isSecure;
    private final int standardPort;

    private HttpProtocol(int standardPort) {
        this(standardPort, false);
    }

    private HttpProtocol(int standardPort, boolean isSecure) {
        this.standardPort = standardPort;
        this.isSecure = isSecure;
    }

    /**
     * Is this a secure protocol.
     * @return <code>true</code> if this is secure.
     */
    public boolean isSecure() {
        return isSecure;
    }

    @Override
    public String toString() {
        return StringUtils.lowerCase(name());
    }

    public int getStandardPort() {
        return standardPort;
    }
}
