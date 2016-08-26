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
//        if (flag) {
        Paint mPaint = getPaint();
        float lineY = mPaint.getFontMetrics().ascent - mPaint.getFontMetrics().top + mPaint
                .getFontMetrics().descent - mPaint.getFontMetrics().ascent - mPaint.getTextSize();

        if (type == 0) {
            super.onDraw(canvas);
        } else if (type == 1) {
            mPaint.setColor(Color.parseColor("#0000F0"));
            canvas.drawText(getText().toString(), 0, getTextSize(), mPaint);
        } else if (type == 2) {
            mPaint.setColor(Color.parseColor("#F00000"));
            canvas.drawText(getText().toString(), 0, -mPaint.getFontMetrics().top, mPaint);
        }
        mPaint.setColor(Color.parseColor("#FF00FF"));
        canvas.drawLine(0, lineY, getWidth(), lineY, mPaint);
//        }
    }

    private int type = 0;

    public void setType(int type) {
        this.type = type;
        postInvalidate();
    }
}
