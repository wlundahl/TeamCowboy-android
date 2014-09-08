package walter.teamcowboy.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import walter.TeamCowboy.R;
import walter.teamcowboy.types.AuthUser;

/**
 * Base activity class that attempts to retrieve a stored {@link walter.teamcowboy.types.AuthUser} and then calls hooks
 * based on the status of the user.
 */
public abstract class UserActivityBase extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE);
        String userJSON = prefs.getString(LoggedIn.AUTH_USER, "");

        if (StringUtils.isBlank(userJSON)) {
            handleInvalidUser();
            return;
        }

        AuthUser user = new Gson().fromJson(userJSON, AuthUser.class);
        handleUserObject(user);
    }

    /**
     * Method that determines how to handle the given user object. Can be called by subclasses but should not be
     * overridden.
     *
     * @param user
     *         AuthUser that can be null or invalid.
     */
    protected void handleUserObject(AuthUser user) {
        if (user != null && user.isValid()) {
            handleValidUser(user);
        } else {
            handleInvalidUser();
        }
    }

    /**
     * Hook for subclasses to handle the case where there isn't a valid stored user.  Some cases where this method could
     * be called include: no saved user, error in deserialization, or an invalid user.
     * <p/>
     * TODO: send an error code?
     */
    protected abstract void handleInvalidUser();

    /**
     * Hook for subclasses to handle a valid saved user object.
     *
     * @param user
     *         AuthUser that is both non null and valid.
     */
    protected abstract void handleValidUser(AuthUser user);
}
