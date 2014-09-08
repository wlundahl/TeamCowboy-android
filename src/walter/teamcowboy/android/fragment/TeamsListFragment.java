package walter.teamcowboy.android.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import walter.TeamCowboy.R;
import walter.teamcowboy.android.tasks.TCListAsyncTaskBase;
import walter.teamcowboy.types.AuthUser;
import walter.teamcowboy.types.Team;

import java.util.List;

public class TeamsListFragment extends TCListFragmentBase<Team> {

    private TeamListAsyncTask teamListAsyncTask;

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        // TODO: do something more useful -- load the team details page
        Log.i(this.getClass().getName(), ToStringBuilder.reflectionToString(listView.getAdapter().getItem(position)));
    }

    @Override
    protected int getEmptyTextStringId() {
        return R.string.no_teams;
    }

    @Override
    protected void doActivityCreated(AuthUser user) {

        teamListAsyncTask = new TeamListAsyncTask();
        teamListAsyncTask.execute(user);
    }

    private class TeamListAsyncTask extends TCListAsyncTaskBase<Team> {

        @Override
        protected List<Team> retrieveData(AuthUser user) {
            return client.getTeams(user);
        }

        @Override
        protected void handleEmptyData() {
            // TODO: figure out some UI treatment.
            Log.w(getClass().getName(), getString(getEmptyTextStringId()));
        }

        @Override
        protected void handleValidData(List<Team> teams) {
            listAdapter.addAll(teams);
        }
    }
}