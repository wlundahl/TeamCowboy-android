package walter.teamcowboy.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import walter.TeamCowboy.R;
import walter.teamcowboy.types.AuthUser;

public class MainActivity extends Activity {
    private static final int SIGN_IN_REQUEST_CODE = 7;
    private static final int SIGNED_IN_REQUEST_CODE = 8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE);
        String userJSON = prefs.getString(LoggedIn.AUTH_USER, "");
        boolean requiresSignIn = true;
        if (StringUtils.isNotBlank(userJSON)) {
            AuthUser user = new Gson().fromJson(userJSON, AuthUser.class);
            requiresSignIn = !handleAuthUser(user);
        }

        if (requiresSignIn) {
            startSignIn();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        boolean requiresSignIn = true;
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (responseCode == Activity.RESULT_OK) {
                AuthUser user = (AuthUser) data.getSerializableExtra(LoggedIn.AUTH_USER);
                requiresSignIn = !handleAuthUser(user);
            } else {
                finish();
                return;
            }
        } else if (requestCode == SIGNED_IN_REQUEST_CODE && responseCode == Activity.RESULT_CANCELED) {
            // TODO: check user pref for "stay signed in"
            finish();
            return;
        }

        if (requiresSignIn) {
            startSignIn();
        }
    }

    private boolean handleAuthUser(AuthUser user) {
        boolean isHandled = false;
        if (user.isValid()) {
            Intent teamsList = new Intent(this, TeamsListActivity.class);
            teamsList.putExtra(LoggedIn.AUTH_USER, user);
            isHandled = true;
            startActivityForResult(teamsList, SIGNED_IN_REQUEST_CODE);
        }
        return isHandled;
    }

    private void startSignIn() {

        Intent signIn = new Intent(this, SignIn.class);
        startActivityForResult(signIn, SIGN_IN_REQUEST_CODE);
    }
}