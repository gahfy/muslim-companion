package net.gahfy.muslimcompanion.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import net.gahfy.muslimcompanion.R;

import java.util.Locale;

public class CompassShadowView extends View {
    private float viewWidth;
    private float viewHeight;

    public CompassShadowView(Context context) {
        super(context);
    }

    public CompassShadowView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @TargetApi(11)
    public CompassShadowView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        viewWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension((int) viewWidth, (int) viewHeight);

        Log.i(CompassShadowView.class.getSimpleName(), String.format(Locale.US, "width:%d / height:%d", (int) viewWidth, (int) viewHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        int minSize = (int) Math.min(viewHeight, viewWidth);
        if(minSize > 0) {
            RadialGradient gradient = new RadialGradient(
                    viewWidth/2,
                    viewHeight/2,
                    minSize/2,
                    new int[]{Color.BLACK, getResources().getColor(R.color.white_93)},
                    new float[]{0, 1},
                    android.graphics.Shader.TileMode.CLAMP);

            paint.setDither(true);
            paint.setShader(gradient);

            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        }
    }
}
