package net.gahfy.muslimcompanion.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import net.gahfy.muslimcompanion.R;

/**
 * This class is a library of useful methods for views
 * @author Gahfy
 */
public class ViewUtils {
    /** The list of possible font type */
    public enum FONT_WEIGHT {
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
        BLACK_ITALIC,
        TOOLBAR_TITLE,
        QURAN_ARABIC
    }

    /**
     * Sets a type of typeface (regarding the character set) to a TextView.
     * @param context Context in which the application is running
     * @param textView The TextView to which the typeface should be applied
     * @param typefaceType The type of the typeface to apply to the TextView
     */
    public static void setTypefaceToTextView(Context context, TextView textView, FONT_WEIGHT typefaceType){
        textView.setTypeface(getTypefaceToUse(context, typefaceType));
    }

    /**
     * Returns the typeface to use with the given typeface type.
     * @param context Context in which the application is running
     * @param typefaceType The type of the typeface to return
     * @return the typeface to use with the given typeface type
     */
    public static Typeface getTypefaceToUse(Context context, FONT_WEIGHT typefaceType){
        switch(typefaceType){
            case THIN:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.thin_path));
            case THIN_ITALIC:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.thin_italic_path));
            case LIGHT:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.light_path));
            case LIGHT_ITALIC:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.light_italic_path));
            case REGULAR:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.regular_path));
            case REGULAR_ITALIC:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.regular_italic_path));
            case MEDIUM:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.medium_path));
            case MEDIUM_ITALIC:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.medium_italic_path));
            case BOLD:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.bold_path));
            case BOLD_ITALIC:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.bold_italic_path));
            case BLACK:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.black_path));
            case BLACK_ITALIC:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.black_italic_path));
            case TOOLBAR_TITLE:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.toolbat_title_path));
            case QURAN_ARABIC:
                return Typeface.createFromAsset(context.getAssets(), "font/KFC_naskh.otf");
            default:
                return Typeface.createFromAsset(context.getAssets(), context.getString(R.string.regular_path));
        }
    }

    public static void setDrawableToImageView(ImageView imageView, int drawableId){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            imageView.setImageDrawable(imageView.getContext().getDrawable(drawableId));
        else
            setDrawableToImageViewBeforeLollipop(imageView, drawableId);
    }

    @SuppressWarnings("deprecation")
    private static void setDrawableToImageViewBeforeLollipop(ImageView imageView, int drawableId){
        imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(drawableId));
    }
}
