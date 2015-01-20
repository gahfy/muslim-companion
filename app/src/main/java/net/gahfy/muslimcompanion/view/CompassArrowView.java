package net.gahfy.muslimcompanion.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import net.gahfy.muslimcompanion.R;

public class CompassArrowView extends View {
    Context context;
    float viewHeight;
    float viewWidth;
    Paint arrowPaint;
    Path arrowPath;

    public CompassArrowView(Context context) {
        super(context);
        this.context = context;
        initPaint();
    }

    public CompassArrowView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
        initPaint();
    }

    public CompassArrowView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();
    }

    private void initPaint(){
        arrowPaint = new Paint();
        arrowPaint.setStyle(Paint.Style.FILL);
        arrowPaint.setColor(context.getResources().getColor(R.color.primary));
    }

    @TargetApi(21)
    public CompassArrowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) viewWidth, (int) viewHeight);

        arrowPath = new Path();
        arrowPath.moveTo(0.34f * viewWidth, 0.66f*viewHeight);
        arrowPath.lineTo(0.5f * viewWidth, 0.17f*viewHeight);
        arrowPath.lineTo(0.66f * viewWidth, 0.66f*viewHeight);
        arrowPath.lineTo(0.5f * viewWidth, 0.55f*viewHeight);
        arrowPath.close();
    }

    public void onDraw(Canvas canvas){
        canvas.drawPath(arrowPath, arrowPaint);
    }
}
