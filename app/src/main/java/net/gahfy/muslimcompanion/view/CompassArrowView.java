package net.gahfy.muslimcompanion.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import net.gahfy.muslimcompanion.R;

/**
 * Arrow view in the compass that is drawn to be resizable
 * @author Gahfy
 */
public class CompassArrowView extends View {
    /** The Paint that is used to draw the arrow */
    Paint arrowPaint;

    /** The Path of the arrow */
    Path arrowPath;

    public CompassArrowView(Context context) {
        super(context);
        initPaintAndPath();
    }

    public CompassArrowView(Context context, AttributeSet attrs){
        super(context, attrs);
        initPaintAndPath();
    }

    public CompassArrowView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        initPaintAndPath();
    }

    /**
     * Initialization of the Paint used to draw the arrow
     */
    private void initPaintAndPath(){
        arrowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPaint.setColor(getResources().getColor(R.color.primary));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        float viewHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) viewWidth, (int) viewHeight);

        // Initialization of the path of the arrow
        arrowPath = new Path();
        arrowPath.moveTo(0.34f * viewWidth, 0.66f*viewHeight);
        arrowPath.lineTo(0.5f * viewWidth, 0.17f*viewHeight);
        arrowPath.lineTo(0.66f * viewWidth, 0.66f*viewHeight);
        arrowPath.lineTo(0.5f * viewWidth, 0.55f*viewHeight);
        arrowPath.close();
    }

    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        canvas.drawPath(arrowPath, arrowPaint);
    }
}
