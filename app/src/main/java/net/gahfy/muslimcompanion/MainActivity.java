package net.gahfy.muslimcompanion;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.analytics.Tracker;

import net.gahfy.muslimcompanion.fragment.AbstractFragment;
import net.gahfy.muslimcompanion.fragment.CompassFragment;

/**
 * As its name says, this is the main Activity of the application
 * @author Gahfy
 */
public class MainActivity extends ActionBarActivity {
    /** The fragment that is currently attached to the Activity */
    AbstractFragment currentFragment;

    public Tracker activityTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.app_name);

        // Get tracker.
        activityTracker = ((MuslimCompanionApplication) getApplication()).getTracker(
                MuslimCompanionApplication.TrackerName.GLOBAL_TRACKER);

        if (savedInstanceState == null) {
            currentFragment = new CompassFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.lyt_fragment_container, currentFragment)
                    .commit();
        } else {
            currentFragment = (AbstractFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.lyt_fragment_container);
        }
    }
}
