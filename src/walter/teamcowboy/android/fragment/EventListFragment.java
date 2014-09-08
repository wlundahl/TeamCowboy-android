package walter.teamcowboy.android.fragment;

import android.util.Log;
import walter.TeamCowboy.R;
import walter.teamcowboy.android.tasks.TCListAsyncTaskBase;
import walter.teamcowboy.types.AuthUser;
import walter.teamcowboy.types.Event;

import java.util.List;

public class EventListFragment extends TCListFragmentBase<Event> {

    private EventListAsyncTask eventListAsyncTask;

    @Override
    protected int getEmptyTextStringId() {
        return R.string.no_events;
    }

    @Override
    protected void doActivityCreated(AuthUser user) {
        eventListAsyncTask = new EventListAsyncTask();
        eventListAsyncTask.execute(user);
    }

    private class EventListAsyncTask extends TCListAsyncTaskBase<Event> {
        @Override
        protected List<Event> retrieveData(AuthUser user) {
            return client.getEvents(user);
        }

        @Override
        protected void handleEmptyData() {
            Log.w(getClass().getName(), getString(getEmptyTextStringId()));
        }

        @Override
        protected void handleValidData(List<Event> data) {
            listAdapter.addAll(data);
        }
    }
}
