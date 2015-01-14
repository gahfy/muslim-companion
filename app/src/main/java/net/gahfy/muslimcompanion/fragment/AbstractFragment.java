package net.gahfy.muslimcompanion.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;

import net.gahfy.muslimcompanion.MainActivity;

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

    /**
     * Returns the parent MainActivity of the Fragment.
     * @return the parent MainActivity of the Fragment
     */
    public MainActivity getMainActivity(){
        return this.mainActivity;
    }
}
