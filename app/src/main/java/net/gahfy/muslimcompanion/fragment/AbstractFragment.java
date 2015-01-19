package net.gahfy.muslimcompanion.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.google.android.gms.analytics.HitBuilders;

import net.gahfy.muslimcompanion.MainActivity;

import java.util.Date;

/**
 * Abstract class that all fragments of the application should extend
 * @author Gahfy
 */
public class AbstractFragment extends Fragment {
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
        getMainActivity().activityTracker.setScreenName(getClass().getSimpleName());

        // Send a screen view.
        getMainActivity().activityTracker.send(new HitBuilders.AppViewBuilder().build());
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
}
