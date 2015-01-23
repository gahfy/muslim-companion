package net.gahfy.muslimcompanion.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.util.AttributeSet;
import android.view.View;

import net.gahfy.muslimcompanion.R;

public class CompassShadowView extends View {
    private float viewWidth;
    private float viewHeight;
    private Paint shadowPaint;

    public CompassShadowView(Context context) {
        super(context);
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public CompassShadowView(Context context, AttributeSet attrs){
        super(context, attrs);
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @TargetApi(11)
    public CompassShadowView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) viewWidth, (int) viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int minSize = (int) Math.min(viewHeight, viewWidth);
        if(minSize > 0) {
            RadialGradient gradient = new RadialGradient(
                    viewWidth/2,
                    viewHeight/2,
                    minSize/2,
                    new int[]{Color.BLACK, getResources().getColor(R.color.white_93)},
                    new float[]{0, 1},
                    android.graphics.Shader.TileMode.CLAMP);

            shadowPaint.setDither(true);
            shadowPaint.setShader(gradient);

            canvas.drawRect(0, 0, getWidth(), getHeight(), shadowPaint);
        }
    }
}
