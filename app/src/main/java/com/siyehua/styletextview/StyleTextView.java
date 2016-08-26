package com.siyehua.styletextview;/**
 * Created by huangxk on 2016/8/18.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * @method
 * @pram
 * @return
 */
public class StyleTextView extends TextView {
    public StyleTextView(Context context) {
        super(context);
    }

    public StyleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StyleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public StyleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (first != null) {
            Paint mPaint = getPaint();
            mPaint.setTextSize(first.size);
            float startX = mPaint.measureText(first.content);
            float space = topSpace(mPaint);

            float lineY = mPaint.getFontMetrics().ascent - mPaint.getFontMetrics().top + mPaint
                    .getFontMetrics().descent - mPaint.getFontMetrics().ascent - mPaint
                    .getTextSize();
            mPaint.setColor(Color.parseColor("#FF00FF"));
            canvas.drawLine(0, lineY, getWidth(), lineY, mPaint);

            mPaint.setTextSize(second.size);
            startX = getWidth() / 2.0f + (startX + mPaint.measureText(second.content)) / 2.0f -
                    mPaint.measureText(second.content);

            float baseLine = space + -mPaint.getFontMetrics().top - topSpace(mPaint);
            mPaint.setColor(second.color);
            canvas.drawText(second.content, startX, baseLine, mPaint);

            Log.e("siyehua", "width: " + getWidth() + "  height: " + getHeight() + " startY: " +
                    startX + "  baseLine: " + baseLine);
        }

    }



    private float topSpace(Paint mPaint) {
        return (mPaint.getFontMetrics().ascent - mPaint.getFontMetrics().top) //最上层空白
                + secondSpace(mPaint);
    }

    private float secondSpace(Paint mPaint) {
        return mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent - mPaint
                .getTextSize();
    }


    SpannableString spannableString;
    String content;
    TextStyle first, second;


    /**
     * 设置Text
     *
     * @param content String
     * @param styles
     */
    public void setText(String content, TextStyle... styles) {
        this.content = content;
        spannableString = new SpannableString(content);
        int i = 0;
        for (TextStyle styleContent : styles) {
            if (i == 0) first = styleContent;
            else if (i == 1) second = styleContent;
            i++;
            int start = content.indexOf(styleContent.content);
            int end = start + styleContent.content.length();
            if (styleContent.size != -1)
                spannableString.setSpan(new AbsoluteSizeSpan(styleContent.size), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (styleContent.color != -1)
                spannableString.setSpan(new ForegroundColorSpan(styleContent.color), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (styleContent.up) {//上标时设置颜色为透明色,然后再onDraw自己画
                spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        setText(spannableString);
    }


    public static class TextStyle {
        String content;
        int size;
        int color;
        boolean up = false;//是否上标

        public TextStyle() {
        }

        public TextStyle(String content, int size, int color, boolean up) {
            this.content = content;
            this.size = size;
            this.color = color;
            this.up = up;
        }
    }

}
