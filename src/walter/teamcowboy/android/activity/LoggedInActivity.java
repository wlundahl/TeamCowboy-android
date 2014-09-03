package walter.teamcowboy.android.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.os.Bundle;

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

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


    }

    // TODO: make a base TC fragment class?
    // copied from http://developer.android.com/guide/topics/ui/actionbar.html#Tabs
    private static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity activity;
        private final String tag;
        private final Class<T> clazz;
        private Fragment fragment;

        private TabListener(Activity activity, String tag, Class<T> clazz) {
            this.activity = activity;
            this.tag = tag;
            this.clazz = clazz;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (fragment == null) {
                fragment = Fragment.instantiate(activity, clazz.getName());
                ft.add(fragment, tag);
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