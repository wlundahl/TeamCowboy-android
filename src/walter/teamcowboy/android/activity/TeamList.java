package walter.teamcowboy.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import walter.TeamCowboy.R;
import walter.teamcowboy.TeamCowboyClient;
import walter.teamcowboy.types.AuthUser;
import walter.teamcowboy.types.Team;

import java.util.List;

public class TeamList extends Activity {
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

    private void setTeamList(List<Team> teams) {
        findViewById(R.id.teams_list_loading_view).setVisibility(View.GONE);
        if (teams == null || teams.isEmpty()) {
            findViewById(R.id.teams_list_error).setVisibility(View.VISIBLE);
            return;
        }

        ListView teamsList = (ListView) findViewById(R.id.teams_list);
        teamsList.setVisibility(View.VISIBLE);
        ListAdapter teamsListAdapter = new ArrayAdapter<Team>(this, android.R.layout.simple_list_item_1, teams);


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