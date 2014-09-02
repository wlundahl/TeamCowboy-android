package walter.teamcowboy.types;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Class that represents an authenticated TeamCowboy user.
 */
public class AuthUser implements Serializable {
    private int userId;
    private String token;

    public AuthUser(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public boolean isValid() {
        return StringUtils.isNotBlank(token);
    }
}
