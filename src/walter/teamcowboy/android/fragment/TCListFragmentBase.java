package walter.teamcowboy.android.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import com.google.common.collect.ImmutableList;
import walter.teamcowboy.TeamCowboyClient;
import walter.teamcowboy.android.activity.LoggedIn;
import walter.teamcowboy.android.activity.SignIn;
import walter.teamcowboy.types.AuthUser;

import java.util.List;

public abstract class TCListFragmentBase<T> extends ListFragment {
    protected ArrayAdapter<T> listAdapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AuthUser user = (AuthUser) getArguments().getSerializable(LoggedIn.AUTH_USER);
        if (user == null) {
            Intent signInIntent = new Intent(getActivity(), SignIn.class);
            startActivity(signInIntent);
        }

        listAdapter = createArrayAdapter();
        setListAdapter(listAdapter);
        setEmptyText(getString(getEmptyTextStringId()));

        doActivityCreated(user);
    }

    /**
     * Method that creates the array adapter.  Subclasses can override this if they need a custom adapter.
     */
    protected ArrayAdapter<T> createArrayAdapter() {
        return new ArrayAdapter<T>(getActivity(), android.R.layout.simple_list_item_1);
    }

    /**
     * Hook for subclass to provide a string id that will be used as the list fragment's empty text string.
     */
    protected abstract int getEmptyTextStringId();

    /**
     * Hook called in onActivityCreated for subclasses to do initial setup and start any required service calls. At this
     * point, the list adapter has already been created and set.
     *
     * @param user
     *         - the auth user to be used in service calls.  This will not be null.
     */
    protected abstract void doActivityCreated(AuthUser user);

    protected abstract class TCAsyncTask<T> extends AsyncTask<AuthUser, Integer, List<T>> {
        protected final TeamCowboyClient client;

        protected TCAsyncTask() {
            // TODO: figure out service provider or something.
            client = new TeamCowboyClient();
        }

        @Override
        protected List<T> doInBackground(AuthUser... params) {
            AuthUser user = params[0];

            if (user == null) {
                Log.e(getClass().getName(), "Null user passed");
                return ImmutableList.of();
            }

            return retrieveData(user);
        }

        @Override
        protected void onPostExecute(List<T> data) {
            if (data == null || data.isEmpty()) {
                handleEmptyData();
            } else {
                handleValidData(data);
            }
        }

        /**
         * Hook for subclasses to retrieve data from TC for the given {@link walter.teamcowboy.types.AuthUser}
         *
         * @param user
         *         - User to retrieve data for, will not be null
         *
         * @return List of data retrieved from the TC service.
         */
        protected abstract List<T> retrieveData(AuthUser user);

        /**
         * Hook for subclasses to do something when the data retrieval returns empty or null data. Default
         * implementation does nothing.
         */
        protected abstract void handleEmptyData();

        /**
         * Hook for subclasses to handle retrieved data that is not null and not empty.
         *
         * @param data
         *         - List of data retrieved from the TC service, will not be null and will not be empty.
         */
        protected abstract void handleValidData(List<T> data);
    }
}
