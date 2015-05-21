package net.gahfy.muslimcompanion.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.google.android.gms.analytics.HitBuilders;

import net.gahfy.muslimcompanion.MainActivity;
import net.gahfy.muslimcompanion.models.MuslimLocation;

/**
 * Abstract class that all fragments of the application should extend
 * @author Gahfy
 */
public abstract class AbstractFragment extends Fragment {
    /** List of all possible geolocation modes */
    public enum GEOLOCATION_TYPE{
        /** No geolocation needed */
        NONE,
        /** Geolocation needed only once, when entering the fragment */
        ONCE,
        /** Geolocation needed continuously */
        CONTINUOUS
    }

    /** The parent MainActivity of the Fragment */
    private MainActivity mainActivity;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        // If the parent activity is instance of MainActivity, then we set it as the parent
        // MainActivity
        if(activity instanceof MainActivity)
            this.mainActivity = (MainActivity) activity;
    }

    @Override
    public void onStart(){
        super.onStart();

        // Set screen name.
        getMainActivity().getAnalyticsTracker().setScreenName(getClass().getSimpleName());

        // Send a screen view.
        getMainActivity().getAnalyticsTracker().send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    /**
     * Returns the parent MainActivity of the Fragment.
     * @return the parent MainActivity of the Fragment
     */
    public MainActivity getMainActivity(){
        return this.mainActivity;
    }

    /**
     * Returns the geolocation type needed by the fragment.
     * @return the geolocation type needed by the fragment
     */
    public GEOLOCATION_TYPE getGeolocationTypeNeeded(){
        return GEOLOCATION_TYPE.NONE;
    }

    /**
     * Called when a position is found.
     * @param location the position that has been found
     */
    public void onLocationChanged(MuslimLocation location){

    }
}
