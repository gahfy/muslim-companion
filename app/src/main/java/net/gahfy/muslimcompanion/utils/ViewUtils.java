package net.gahfy.muslimcompanion.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import net.gahfy.muslimcompanion.R;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class is a library of useful methods for views
 * @author Gahfy
 */
public class ViewUtils {
    /** The list of possible font type */
    public static enum FONT_WEIGHT {
        THIN,
        THIN_ITALIC,
        LIGHT,
        LIGHT_ITALIC,
        REGULAR,
        REGULAR_ITALIC,
        MEDIUM,
        MEDIUM_ITALIC,
        BOLD,
        BOLD_ITALIC,
        BLACK,
        BLACK_ITALIC
    }

    /**
     * Sets a typeface (given by its path from assets) to a TextView.
     * @param context Context in which the application is running
     * @param textView The TextView to which the typeface should be applied
     * @param typefacePath The path (relative to assets) to the font to apply to the TextView
     */
    private static void setTypefaceToTextView(Context context, TextView textView, String typefacePath){
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), typefacePath));
    }

    /**
     * Sets a type of typeface (regarding the character set) to a TextView.
     * @param context Context in which the application is running
     * @param textView The TextView to which the typeface should be applied
     * @param typefaceType The type of the typeface to apply to the TextView
     */
    public static void setTypefaceToTextView(Context context, TextView textView, FONT_WEIGHT typefaceType){
        switch(typefaceType){
            case THIN:
                setTypefaceToTextView(context, textView, context.getString(R.string.thin_path));
                break;
            case THIN_ITALIC:
                setTypefaceToTextView(context, textView, context.getString(R.string.thin_italic_path));
                break;
            case LIGHT:
                setTypefaceToTextView(context, textView, context.getString(R.string.light_path));
                break;
            case LIGHT_ITALIC:
                setTypefaceToTextView(context, textView, context.getString(R.string.light_italic_path));
                break;
            case REGULAR:
                setTypefaceToTextView(context, textView, context.getString(R.string.regular_path));
                break;
            case REGULAR_ITALIC:
                setTypefaceToTextView(context, textView, context.getString(R.string.regular_italic_path));
                break;
            case MEDIUM:
                setTypefaceToTextView(context, textView, context.getString(R.string.medium_path));
                break;
            case MEDIUM_ITALIC:
                setTypefaceToTextView(context, textView, context.getString(R.string.medium_italic_path));
                break;
            case BOLD:
                setTypefaceToTextView(context, textView, context.getString(R.string.bold_path));
                break;
            case BOLD_ITALIC:
                setTypefaceToTextView(context, textView, context.getString(R.string.bold_italic_path));
                break;
            case BLACK:
                setTypefaceToTextView(context, textView, context.getString(R.string.black_path));
                break;
            case BLACK_ITALIC:
                setTypefaceToTextView(context, textView, context.getString(R.string.black_italic_path));
                break;
        }
    }
}
