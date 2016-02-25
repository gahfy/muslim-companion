package net.gahfy.muslimcompanion.fragment;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.Display;

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
    public void onAttach(Context context){
        super.onAttach(context);

        // If the parent activity is instance of MainActivity, then we set it as the parent
        // MainActivity
        if(context instanceof MainActivity)
            this.mainActivity = (MainActivity) context;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public int getLocationDetailsTextResId(){
        return 0;
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

    public int getOrientation(Display display){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO)
            return getOrientationBefore8(display);
        else
            return display.getRotation();
    }

    @SuppressWarnings("deprecation")
    public int getOrientationBefore8(Display display){
        return display.getOrientation();
    }
}
