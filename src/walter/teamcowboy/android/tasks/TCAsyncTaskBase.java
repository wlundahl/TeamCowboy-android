package walter.teamcowboy.android.tasks;

import android.os.AsyncTask;
import android.util.Log;
import walter.teamcowboy.TeamCowboyClient;
import walter.teamcowboy.types.AuthUser;

public abstract class TCAsyncTaskBase<T> extends AsyncTask<AuthUser, Integer, T> {
    protected final TeamCowboyClient client;

    protected TCAsyncTaskBase() {
        // TODO: figure out service provider or something.
        client = new TeamCowboyClient();
    }

    @Override
    protected T doInBackground(AuthUser... params) {
        AuthUser user = params[0];

        if (user == null) {
            Log.e(getClass().getName(), "Null user passed");
            return getDefaultValue();
        }

        return retrieveData(user);
    }

    @Override
    protected void onPostExecute(T data) {
        if (isValidData(data)) {
            handleValidData(data);
        } else {
            handleEmptyData();
        }
    }

    /**
     * Method that determines if the data object provided is valid.
     *
     * @param data
     *         object of type <code>T</code>; may be null.
     */
    protected boolean isValidData(T data) {
        return data != null;
    }

    /**
     * Method that determines the default value to be returned in error conditions.
     */
    protected T getDefaultValue() {
        return null;
    }

    /**
     * Hook for subclasses to retrieve data from TC for the given {@link walter.teamcowboy.types.AuthUser}
     *
     * @param user
     *         - User to retrieve data for, will not be null
     *
     * @return List of data retrieved from the TC service.
     */
    protected abstract T retrieveData(AuthUser user);

    /**
     * Hook for subclasses to do something when the data retrieval returns empty or null data. Default
     * implementation does nothing.
     */
    protected abstract void handleEmptyData();

    /**
     * Hook for subclasses to handle retrieved data that is not null and not empty.
     *
     * @param data
     *         - Data retrieved from the TC service, will not be null and will not be empty.
     */
    protected abstract void handleValidData(T data);
}
