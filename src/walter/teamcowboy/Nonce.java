package walter.teamcowboy;

import java.util.Date;
import java.util.UUID;

public interface Nonce {
    public String getTimestamp();
    public String getNonce();

    public static class NonceImpl implements Nonce{
        public static final String DIGIT_FORMAT = "%08d";
        private final long timestamp;
        private final long nonce;

        public NonceImpl() {
            UUID uuid = UUID.randomUUID();
            timestamp = new Date().getTime() / 1000;
            nonce = Math.abs(uuid.getMostSignificantBits());
        }

        public NonceImpl(long timestamp, long nonce) {
            this.timestamp = timestamp;
            this.nonce = Math.abs(nonce);
        }

        public String getTimestamp() {
            return String.format(DIGIT_FORMAT, timestamp);
        }

        public String getNonce() {
            return String.format(DIGIT_FORMAT, nonce);
        }
    }
}
