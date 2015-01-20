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
    float height;
    float width;

    public CompassArrowView(Context context) {
        super(context);
        this.context = context;
    }

    public CompassArrowView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.context = context;
    }

    public CompassArrowView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @TargetApi(21)
    public CompassArrowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) width, (int) height);
    }

    public void onDraw(Canvas canvas){
        Paint paint = new Paint();
        Path path = new Path();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(context.getResources().getColor(R.color.primary));

        path.moveTo(0.34f * width, 0.66f*height);
        path.lineTo(0.5f * width, 0.17f*height);
        path.lineTo(0.66f * width, 0.66f*height);
        path.lineTo(0.5f * width, 0.55f*height);
        path.close();
        canvas.drawPath(path, paint);
    }
}
