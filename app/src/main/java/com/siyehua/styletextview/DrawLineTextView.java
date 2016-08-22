package com.siyehua.styletextview;/**
 * Created by huangxk on 2016/8/22.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @method
 * @pram
 * @return
 */
public class DrawLineTextView extends TextView {
    public DrawLineTextView(Context context) {
        super(context);
    }

    public DrawLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawLineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawLineTextView(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (flag) {
        Paint mPaint = getPaint();
        float lineY = mPaint.getFontMetrics().ascent - mPaint.getFontMetrics().top + mPaint
                .getFontMetrics().descent - mPaint.getFontMetrics().ascent - mPaint.getTextSize();
        mPaint.setColor(Color.parseColor("#FF00FF"));
        canvas.drawLine(0, lineY, getWidth(), lineY, mPaint);
//        }
    }

    float top, ascent, descent, bottom;
    private boolean flag = false;

    public void setLinePositionAndDraw(float top, float ascent, float descent, float bottom) {
        flag = true;
        this.top = top;
        this.ascent = ascent;
        this.descent = descent;
        this.bottom = bottom;
        postInvalidate();
    }
}
