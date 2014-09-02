package walter.teamcowboy.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import walter.TeamCowboy.R;
import walter.teamcowboy.TeamCowboyClient;
import walter.teamcowboy.types.AuthUser;

public class LoggedIn extends Activity {
    static final String AUTH_USER = "walter.teamcowboy.AUTH_USER";
    private static final String TAG = LoggedIn.class.getName();
    private String username;
    private boolean shouldStaySignedIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.logged_in);

        Intent intent = getIntent();
        username = intent.getStringExtra(SignIn.USERNAME);
        String password = intent.getStringExtra(SignIn.PASSWORD);
        shouldStaySignedIn = intent.getBooleanExtra(SignIn.STAY_SIGNED_IN, true);

        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            Log.w(TAG, "Received an invalid username[" + username + "] or password [" + password + "]");
            return;
        }

        AsyncTask<Pair<String, String>, Integer, AuthUser> userTask = new LoginTask().execute(
                Pair.of(username, password));
    }

    private void setLoginStatus(AuthUser user) {
        Intent result = new Intent();
        int resultCode;
        if (user == null) {
            resultCode = Activity.RESULT_CANCELED;
        } else {
            resultCode = Activity.RESULT_OK;
            if (shouldStaySignedIn) {
                SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_file_name), Context
                        .MODE_PRIVATE);
                SharedPreferences.Editor prefEditor = prefs.edit();
                prefEditor.putString(AUTH_USER, new Gson().toJson(user));
                prefEditor.commit();
            }

            result.putExtra(AUTH_USER, user);
        }
        setResult(resultCode, result);
        finish();
    }

    private class LoginTask extends AsyncTask<Pair<String, String>, Integer, AuthUser> {
        @Override
        protected AuthUser doInBackground(Pair<String, String>... userNamePassword) {
            TeamCowboyClient tcClient = new TeamCowboyClient();
            // TODO: make these ctor args.
            return tcClient.login(userNamePassword[0].getLeft(), userNamePassword[0].getRight());
        }

        @Override
        protected void onPostExecute(AuthUser user) {
            setLoginStatus(user);
        }
    }
}