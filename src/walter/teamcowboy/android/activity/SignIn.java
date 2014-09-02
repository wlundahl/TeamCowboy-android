package walter.teamcowboy.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import walter.TeamCowboy.R;
import walter.teamcowboy.types.AuthUser;

public class SignIn extends Activity {
    static final String USERNAME = "walter.teamcowboy.UserName";
    static final String PASSWORD = "walter.teamcowboy.Password";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE);
        String userJSON = prefs.getString(LoggedIn.AUTH_USER, "");
        if (StringUtils.isNotBlank(userJSON)) {
            AuthUser user = new Gson().fromJson(userJSON, AuthUser.class);
            if (user.isValid()) {
                Intent teamsList = new Intent(this, TeamList.class);
                teamsList.putExtra(LoggedIn.AUTH_USER, user);
                startActivity(teamsList);
            }
        }
    }

    public void attemptLogin(View view) {

        EditText usernameView = (EditText) findViewById(R.id.username);
        EditText passwordView = (EditText) findViewById(R.id.password);

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        TextView messageView = (TextView) findViewById(R.id.login_error_message);
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            messageView.setText(getString(R.string.invalid_login_input));
            return;
        }

        messageView.setText(StringUtils.EMPTY);

        Intent loginIntent = new Intent(this, LoggedIn.class);
        loginIntent.putExtra(USERNAME, username);
        loginIntent.putExtra(PASSWORD, password);
        startActivity(loginIntent);
    }
}
