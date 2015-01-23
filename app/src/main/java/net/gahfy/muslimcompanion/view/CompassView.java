package net.gahfy.muslimcompanion.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import net.gahfy.muslimcompanion.R;
import net.gahfy.muslimcompanion.utils.ViewUtils;

public class CompassView extends View{
    private Paint northTextPaint;
    private Paint defaultTextPaint;

    private String northAbbreviation;
    private String southAbbreviation;
    private String eastAbbreviation;
    private String westAbbreviation;

    int northTextWidth;
    int southTextWidth;
    int eastTextWidth;
    int westTextWidth;

    private float viewWidth;
    private float viewHeight;

    public CompassView(Context context) {
        super(context);
        initValues();
    }


    public CompassView(Context context, AttributeSet attrs){
        super(context, attrs);
        initValues();
    }

    public CompassView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        initValues();
    }

    public void initValues(){
        northAbbreviation = getResources().getString(R.string.north_abbreviation);
        southAbbreviation = getResources().getString(R.string.south_abbreviation);
        eastAbbreviation = getResources().getString(R.string.east_abbreviation);
        westAbbreviation = getResources().getString(R.string.west_abbreviation);

        northTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        northTextPaint.setColor(getResources().getColor(R.color.accent));
        northTextPaint.setStyle(Paint.Style.FILL);
        northTextPaint.setTextSize(50);
        northTextPaint.setTypeface(ViewUtils.getTypefaceToUse(this.getContext(), ViewUtils.FONT_WEIGHT.MEDIUM));

        defaultTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defaultTextPaint.setColor(getResources().getColor(R.color.black_87));
        defaultTextPaint.setStyle(Paint.Style.FILL);
        defaultTextPaint.setTextSize(50);
        defaultTextPaint.setTypeface(ViewUtils.getTypefaceToUse(this.getContext(), ViewUtils.FONT_WEIGHT.LIGHT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) viewWidth, (int) viewHeight);

        northTextPaint.setTextSize(viewWidth * 0.14f);
        defaultTextPaint.setTextSize(viewWidth * 0.14f);

        Rect northBounds = new Rect();
        Rect southBounds = new Rect();
        Rect eastBounds = new Rect();
        Rect westBounds = new Rect();

        northTextPaint.getTextBounds(northAbbreviation, 0, northAbbreviation.length(), northBounds);
        defaultTextPaint.getTextBounds(southAbbreviation, 0, southAbbreviation.length(), southBounds);
        defaultTextPaint.getTextBounds(eastAbbreviation, 0, eastAbbreviation.length(), eastBounds);
        defaultTextPaint.getTextBounds(westAbbreviation, 0, westAbbreviation.length(), westBounds);

        northTextWidth = northBounds.width();
        southTextWidth = southBounds.width();
        eastTextWidth = eastBounds.width();
        westTextWidth = westBounds.width();
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawRect(0.49f * viewWidth, 0.029f * viewHeight, 0.51f * viewWidth, 0.095f * viewHeight, defaultTextPaint);
        canvas.drawText(northAbbreviation, (viewWidth - northTextWidth) * 0.5f, viewHeight * 0.25f, northTextPaint);
        canvas.rotate(90, viewWidth / 2, viewHeight / 2);
        canvas.drawRect(0.49f * viewWidth, 0.029f * viewHeight, 0.51f * viewWidth, 0.095f * viewHeight, defaultTextPaint);
        canvas.drawText(eastAbbreviation, (viewWidth - eastTextWidth) * 0.5f, viewHeight * 0.25f, defaultTextPaint);
        canvas.rotate(90, viewWidth / 2, viewHeight / 2);
        canvas.drawRect(0.49f * viewWidth, 0.029f * viewHeight, 0.51f * viewWidth, 0.095f * viewHeight, defaultTextPaint);
        canvas.drawText(southAbbreviation, (viewWidth - southTextWidth) * 0.5f, viewHeight * 0.25f, defaultTextPaint);
        canvas.rotate(90, viewWidth / 2, viewHeight / 2);
        canvas.drawRect(0.49f * viewWidth, 0.029f * viewHeight, 0.51f * viewWidth, 0.095f * viewHeight, defaultTextPaint);
        canvas.drawText(westAbbreviation, (viewWidth - westTextWidth) *0.5f, viewHeight * 0.25f, defaultTextPaint);
    }
}
