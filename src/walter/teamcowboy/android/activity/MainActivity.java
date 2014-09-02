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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE);
        String userJSON = prefs.getString(LoggedIn.AUTH_USER, "");
        if (StringUtils.isNotBlank(userJSON)) {
            AuthUser user = new Gson().fromJson(userJSON, AuthUser.class);
            if (user.isValid()) {
                Intent teamsList = new Intent(this, TeamList.class);
                teamsList.putExtra(LoggedIn.AUTH_USER, user);
                startActivity(teamsList);
                return;
            }
        }

        Intent signIn = new Intent(this, SignIn.class);
        startActivity(signIn);
    }
}