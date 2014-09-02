package walter.teamcowboy.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import org.apache.commons.lang3.StringUtils;
import walter.TeamCowboy.R;

public class SignIn extends Activity {
    static final String USERNAME = "walter.teamcowboy.UserName";
    static final String PASSWORD = "walter.teamcowboy.Password";
    private static final String CREATE_ACCOUNT_URL = "https://www.teamcowboy.com/register";
    private static final String FORGOT_PASSWORD_URL = "https://www.teamcowboy.com/resetPassword";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        final EditText passwordView = getPasswordEditor();
        passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    attemptLogin(passwordView.getRootView());
                    return true;
                }
                return false;
            }
        });
    }

    public void attemptLogin(View view) {

        EditText usernameView = (EditText) findViewById(R.id.username);
        EditText passwordView = getPasswordEditor();

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

    public void createAccount(View view) {
        openURL(CREATE_ACCOUNT_URL);
    }

    public void forgotPassword(View view) {
        openURL(FORGOT_PASSWORD_URL);
    }

    private EditText getPasswordEditor() {
        return (EditText) findViewById(R.id.password);
    }

    /**
     * Helper method that starts an activity to open the given url.
     */
    private void openURL(String url) {
        Intent createAccount = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(createAccount);
    }
}
