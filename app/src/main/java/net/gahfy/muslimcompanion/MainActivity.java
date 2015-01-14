package net.gahfy.muslimcompanion;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import net.gahfy.muslimcompanion.fragment.AbstractFragment;
import net.gahfy.muslimcompanion.fragment.CompassFragment;

/**
 * As its name says, this is the main Activity of the application
 * @author Gahfy
 */
public class MainActivity extends FragmentActivity {
    /** The fragment that is currently attached to the Activity */
    AbstractFragment currentFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            currentFragment = new CompassFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, currentFragment)
                    .commit();
        } else {
            currentFragment = (AbstractFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }
    }
}
