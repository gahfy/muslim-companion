package net.gahfy.muslimcompanion.fragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.gahfy.muslimcompanion.DbManager;
import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.models.MuslimLocation;
import net.gahfy.muslimcompanion.utils.LocationUtils;
import net.gahfy.muslimcompanion.utils.MathUtils;
import net.gahfy.muslimcompanion.utils.ViewUtils;
import net.gahfy.muslimcompanion.view.CompassArrowView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CitySearchFragment extends AbstractFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getMainActivity().hideToolbar();
        return inflater.inflate(R.layout.fragment_city_search, container, false);
    }
}
