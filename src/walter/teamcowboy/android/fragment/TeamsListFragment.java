package walter.teamcowboy.android.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import walter.TeamCowboy.R;
import walter.teamcowboy.TeamCowboyClient;
import walter.teamcowboy.android.activity.LoggedIn;
import walter.teamcowboy.android.activity.SignIn;
import walter.teamcowboy.types.AuthUser;
import walter.teamcowboy.types.Team;

import java.util.List;

public class TeamsListFragment extends ListFragment {
    private ArrayAdapter<Team> teamListAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AuthUser user = (AuthUser) getArguments().getSerializable(LoggedIn.AUTH_USER);
        if (user == null) {
            Intent signInIntent = new Intent(getActivity(), SignIn.class);
            startActivity(signInIntent);
        }

        setEmptyText(getActivity().getString(R.string.no_teams));

        teamListAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        setListAdapter(teamListAdapter);

        new TeamListAsyncTask().execute(user);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        // TODO: do something more useful -- load the team details page
        Log.i(this.getClass().getName(), ToStringBuilder.reflectionToString(listView.getAdapter().getItem(position)));
    }

    private void setTeamList(List<Team> teams) {
        if (teams == null || teams.isEmpty()) {
            // TODO: figure out some UI treatment.
            Log.w(getClass().getName(), getString(R.string.no_teams));
            return;
        }

        teamListAdapter.addAll(teams);
        setListShown(true);
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