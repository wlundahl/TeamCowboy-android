package walter.teamcowboy.android.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import walter.teamcowboy.android.activity.LoggedIn;
import walter.teamcowboy.android.activity.SignIn;
import walter.teamcowboy.types.AuthUser;

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
}
