package walter.teamcowboy.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import walter.TeamCowboy.R;
import walter.teamcowboy.TeamCowboyClient;
import walter.teamcowboy.types.AuthUser;
import walter.teamcowboy.types.Team;

import java.util.List;

public class TeamsListActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_list);

        AuthUser user = (AuthUser) getIntent().getSerializableExtra(LoggedIn.AUTH_USER);
        if (user == null) {
            Intent signInIntent = new Intent(this, SignIn.class);
            startActivity(signInIntent);
        }
        new TeamListAsyncTask().execute(user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                logout();
                return true;
            case R.id.action_settings: //TODO: add settings page.
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Helper method to delete the saved user object and return to the main activity.
     * <p/>
     * TODO: move to a base class or something.
     */
    private void logout() {
        SharedPreferences prefs = getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE);
        prefs.edit().remove(LoggedIn.AUTH_USER).commit();

        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
    }

    private void setTeamList(List<Team> teams) {
        findViewById(R.id.teams_list_loading_view).setVisibility(View.GONE);
        if (teams == null || teams.isEmpty()) {
            findViewById(R.id.teams_list_error).setVisibility(View.VISIBLE);
            return;
        }

        ListView teamsList = (ListView) findViewById(R.id.teams_list);
        teamsList.setVisibility(View.VISIBLE);
        ListAdapter teamsListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, teams);

        teamsList.setAdapter(teamsListAdapter);
    }

    private class TeamListAsyncTask extends AsyncTask<AuthUser, Integer, List<Team>> {
        @Override
        protected List<Team> doInBackground(AuthUser... params) {
            AuthUser user = params[0];

            TeamCowboyClient client = new TeamCowboyClient();
            return client.getTeams(user);
        }

        @Override
        protected void onPostExecute(List<Team> teams) {
            setTeamList(teams);
        }
    }
}