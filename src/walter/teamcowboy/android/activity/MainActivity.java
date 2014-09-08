package walter.teamcowboy.android.activity;

import android.app.Activity;
import android.content.Intent;
import walter.teamcowboy.types.AuthUser;

public class MainActivity extends UserActivityBase {
    private static final int SIGN_IN_REQUEST_CODE = 7;
    private static final int SIGNED_IN_REQUEST_CODE = 8;

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (responseCode == Activity.RESULT_OK) {
                AuthUser user = (AuthUser) data.getSerializableExtra(LoggedIn.AUTH_USER);
                handleUserObject(user);
            } else {
                finish();
                return;
            }
        } else if (requestCode == SIGNED_IN_REQUEST_CODE && responseCode == Activity.RESULT_CANCELED) {
            // TODO: check user pref for "stay signed in"
            finish();
            return;
        } else {
            handleInvalidUser();
        }
    }

    /**
     * This method starts up the activity to handle sign in.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    protected void handleInvalidUser() {
        Intent signIn = new Intent(this, SignIn.class);
        startActivityForResult(signIn, SIGN_IN_REQUEST_CODE);
    }

    /**
     * This method passes the valid user to the activity that handles logged in users.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    protected void handleValidUser(AuthUser user) {
        Intent teamsList = new Intent(this, LoggedInActivity.class);
        teamsList.putExtra(LoggedIn.AUTH_USER, user);
        startActivityForResult(teamsList, SIGNED_IN_REQUEST_CODE);
    }
}