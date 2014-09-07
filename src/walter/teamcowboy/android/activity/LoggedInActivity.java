package walter.teamcowboy.android.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import walter.TeamCowboy.R;
import walter.teamcowboy.android.fragment.EventListFragment;
import walter.teamcowboy.android.fragment.TeamsListFragment;
import walter.teamcowboy.types.AuthUser;

/**
 * Activity that will be called from the {@link MainActivity} and adds a tabbed action bar for the different signed in
 * views.
 * <p/>
 * <b>Note:</b> don't confuse this with the {@link LoggedIn} activity which handles logging a user in.
 * <p/>
 * TODO: Refactor the {@link LoggedIn} activity and consolidate it with {@link SignIn}.
 */
public class LoggedInActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_logged_in);

        AuthUser user = (AuthUser) getIntent().getSerializableExtra(LoggedIn.AUTH_USER);
        if (user == null) {
            Log.w(getClass().getName(), "Attempted to start Logged In activity without a logged in user.");
            logout();
            return;
        }

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab teamsList = actionBar.newTab().setText(getString(R.string.teams_tab_text)).setTabListener(
                new TabListener<>(this, "teams", TeamsListFragment.class, user));
        actionBar.addTab(teamsList);

        ActionBar.Tab eventsList = actionBar.newTab().setText(getString(R.string.events_tab_text)).setTabListener(new TabListener<>(this,
                "events", EventListFragment.class, user));
        actionBar.addTab(eventsList);
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

        setResult(Activity.RESULT_OK);
        finish();
    }

    // TODO: make a base TC fragment class?
    // copied from http://developer.android.com/guide/topics/ui/actionbar.html#Tabs
    private static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity activity;
        private final String tag;
        private final Class<T> clazz;
        private final AuthUser user;
        private Fragment fragment;

        private TabListener(Activity activity, String tag, Class<T> clazz, AuthUser user) {
            this.activity = activity;
            this.tag = tag;
            this.clazz = clazz;
            this.user = user;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (fragment == null) {
                fragment = Fragment.instantiate(activity, clazz.getName());

                Bundle args = new Bundle();
                args.putSerializable(LoggedIn.AUTH_USER, user);
                fragment.setArguments(args);

                ft.add(R.id.logged_in_data_fragment_container, fragment);
            } else {
                ft.attach(fragment);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (fragment != null) {
                ft.detach(fragment);
            }
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // maybe check caching or something here?
        }
    }
}