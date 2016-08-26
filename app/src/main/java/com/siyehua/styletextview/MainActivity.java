package com.siyehua.styletextview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.TypedValue;

public class MainActivity extends AppCompatActivity {
    SpannableString spannableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final DrawLineTextView textView2 = (DrawLineTextView) findViewById(R.id._test);
//        assert textView2 != null;
//        textView2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Paint mPaint = textView2.getPaint();
//                textView2.setLinePositionAndDraw(mPaint.getFontMetrics().top, mPaint
//                        .getFontMetrics().ascent, mPaint.getFontMetrics().descent, mPaint
//                        .getFontMetrics().bottom);
//                Log.e("siyehua", "top: " + mPaint.getFontMetrics().top + "  ascent: " + mPaint
//                        .getFontMetrics().ascent + "  descent: " + mPaint.getFontMetrics()
//                        .descent + "  " + "bottom: " + mPaint.getFontMetrics().bottom + "  " +
//                        "leading: " + mPaint.getFontMetrics().leading + " mPaintSize: " + mPaint
//                        .getTextSize() + "  \nview-top: " +
//                        textView2.getTop() + "  view-bottom: " + textView2.getBottom() + " " +
//                        "textSize: " + textView2.getTextSize());
//            }
//        });
//        final DrawLineTextView textView3 = (DrawLineTextView) findViewById(R.id._test2);
//        assert textView3 != null;
//        textView3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Paint mPaint = textView2.getPaint();
//                textView3.setLinePositionAndDraw(mPaint.getFontMetrics().top, mPaint
//                        .getFontMetrics().ascent, mPaint.getFontMetrics().descent, mPaint
//                        .getFontMetrics().bottom);
//
//                Log.e("siyehua", "top: " + mPaint.getFontMetrics().top + "  ascent: " + mPaint
//                        .getFontMetrics().ascent + "  descent: " + mPaint.getFontMetrics()
//                        .descent + "  " + "bottom: " + mPaint.getFontMetrics().bottom + "  " +
//                        "leading: " + mPaint.getFontMetrics().leading + " mPaintSize: " + mPaint
//                        .getTextSize() + "  \nview-top: " +
//                        textView2.getTop() + "  view-bottom: " + textView2.getBottom() + " " +
//                        "textSize: " + textView2.getTextSize());
//            }
//        });


//        TextView textView = (TextView) findViewById(R.id._001);
//        String text = new String("60%\n主胜");
//        spannableString = new SpannableString(text);
//        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources()
//                .getDisplayMetrics());
//        spannableString.setSpan(new AbsoluteSizeSpan(size), 2, 3, Spanned
//                .SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体大小
//        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF00FF")), 2, 3,
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体颜色
//
//        int size2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30, getResources
//                ().getDisplayMetrics());
//        spannableString.setSpan(new AbsoluteSizeSpan(size2), text.length() - 2, text.length(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体大小
//        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#000FFF")), text.length
//                        () - 2, text.length(), Spanned.SPA
//
//                N_EXCLUSIVE_EXCLUSIVE);//设置字体颜色
//        textView.setText(spannableString);



        StyleTextView textView = (StyleTextView) findViewById(R.id.tv_content);
        assert textView != null;
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources()
                .getDisplayMetrics());
        int color = Color.parseColor("#FF00FF");
        int size2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 24, getResources
                ().getDisplayMetrics());
        int color2 = Color.parseColor("#0000FF");
        int size3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 60, getResources
                ().getDisplayMetrics());
        int color3 = Color.parseColor("#464646");
        textView.setText("6 0%\n主胜", new StyleTextView.TextStyle("6 0", size3, color3, false), new
                StyleTextView.TextStyle("%", size, color, true), new StyleTextView.TextStyle
                ("主胜", size2, color2, false));


//        DrawLineTextView textView1 = (DrawLineTextView) findViewById(R.id.tv_001);
//        DrawLineTextView textView2 = (DrawLineTextView) findViewById(R.id.tv_002);
//        textView2.setType(1);
//        DrawLineTextView textView3 = (DrawLineTextView) findViewById(R.id.tv_003);
//        textView3.setType(2);

    }

}
