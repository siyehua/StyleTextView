# 本篇文章已授权微信公众号 guolin_blog （郭霖）独家发布

# StyleTextView

## 本文主要探讨TextView控件绘制文字,上下会留有一定空白原因.以及使用canvas.drawText()绘制文字时,baseLine的该如何确定,如何让文字上下没有留白

最近做项目的时候遇到个问题

![Image](/img/_001.png)

当时的布局是这样的(一言不合就上图,具体代码请点击图片)

[![Image](/img/_002.png)](/app/src/main/res/layout/_001.xml)

什么,这百分号和数字怎么对齐?这数字下面的文字怎么居中?这层级为什么这么复杂,关键外面还有好多层父布局,这是要上天呀!

![Image](/img/_100.png)

## 解决思路

薄荷就是醒脑,表情包还没做完思路就出来.

* 减少层级,采用一个TextView

```xml
<LinearLayout>
    <TextView/>
    <TextView/>
    <TextView/>
</LinearLayout>
```

* 每个TextView的大小,用SpannableString控制其大小和颜色.理论上是很简单的,下面是具体的代码

[布局](/app/src/main/res/layout/_002.xml)

代码:

```java
TextView textView = (TextView) findViewById(R.id._001);
        String text = new String("60%/n主胜");
        spannableString = new SpannableString(text);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources()
                .getDisplayMetrics());
        spannableString.setSpan(new AbsoluteSizeSpan(size), 2, 3, Spanned
                .SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体大小
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF00FF")), 2, 3,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体颜色

        int size2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30, getResources
                ().getDisplayMetrics());
        spannableString.setSpan(new AbsoluteSizeSpan(size2), text.length() - 2, text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体大小
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#000FFF")), text.length
                () - 2, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体颜色
        textView.setText(spannableString);
```

* 最终效果:

![Image](/img/_003.png)

![Image](/img/_101.png)

实践与理论一样简单,完全不是个事.然后我就兴冲冲的把代码提交了.一切都很顺利,同事还夸我脑子灵活.

### 当然故事肯定都没有这么简单的,这么简单也什么好写博客的,毕竟天天都有人夸我.

## 事故
说到事故,先说个故事.

APP都有个设计,视觉都会有个美女,自从调来了一个美女视觉之后我干活就有劲了,整天乐不思蜀....

![Image](/img/_102.png)

美女的脾气都不好,她说这个%号要靠上,不能靠下.

我好说歹说,说实现起来不容易呀,如果靠上要用复杂的布局,会影响性能...

### 不行,必须靠上!!!

整个人都斯巴达了,关键是美女没法发脾气,这才是上层把她调过来的真实目的吗?

### 百分号%下对齐的原因

百分号之所以下对齐,是因为普通图形的绘制,是从图纸的左上角开始绘制的,而文字的绘制则是从左下角开始的.

具体可以看一下canvas.draw图形方法,以及canvas.drawText方法参数.

问题是知道的,但是没有办法和设计解释,然而也想出好的方法改变baseLine绘制.
Google一圈没有答案,群里问了一圈也没有,建议自己定义,看来只好自己动手了.


## 思路
SpannableString基本上是可以满足需求的,关键是画%百分号的时候不满足.

* 看源码看TextView是如何利用SpannableString画的,覆写其关键代码达到目的

    源码老复杂了看不懂.看资料SpannableString有一个上标的功能,试了一下上标是相对于本身字体大小来上标的,而60与%相差甚远,且无法微调

* 画百分号%的时候自己画(看来只能这个思路了)

### 自定义TextView
* 自定义StyleTextView继承TextView
* 定义类TextStyle

```java
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
```

* 定义方法setText.
```java
  /**
     * 设置Text
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
            if (styleContent.up) {//上标时设置颜色为透明色,然后在onDraw自己画
                spannableString.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        setText(spannableString);
    }
```

* 覆写onDraw方法

```java
 canvas.drawText(content, startX, baseLine, mPaint);
```

### 到此自定义View完全结束.

## 外传

故事到这里就结束肯定不叫故事.

上面所有的都并非关键,关键是drawText()参数.

之前说了,drawText是从左下角开始画的,所以要计算出startX与baseLine的值.

startX:计算出百分号之前的"60"的宽度即可.

baseLine:baseLine默认是等于-top(后文证明),但TextView上下有一定的空白,导致直接写-top不满足需求

![Image](/img/_012.png)

图片来源于网络

当文字字号过大时,空白非常的明显.设计要求"60%"百分号与60对齐,所以必须量出空白的高度.

### 测试上图中的各个属性

按照上图的说法,我们打印一下这几个值

```xml
  <TextView
        android:id="@+id/_test"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello !"
        android:textSize="60sp"/>
```
```java
 final TextView textView2 = (TextView) findViewById(R.id._test);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paint mPaint = textView2.getPaint();
                Log.e("siyehua", "top: " + mPaint.getFontMetrics().top + "  ascent: " + mPaint
                        .getFontMetrics().ascent + "  descent: " + mPaint.getFontMetrics()
                        .descent + "  " + "bottom: " + mPaint.getFontMetrics().bottom + "  " +
                        "leading: " + mPaint.getFontMetrics().leading + "  /nview-top: " +
                        textView2.getTop() + "  view-bottom: " + textView2.getBottom() + " " +
                        "textSize: " + textView2.getTextSize());
            }
        });
```

结果如下:(单位px,后文无特殊说明,单位均默认为像素px)
```
top: -126.73828  ascent: -111.328125  descent: 29.296875  bottom: 32.51953  leading: 0.0
view-top: 0  view-bottom: 160 textSize: 120.0
```

![Image](/img/_013.png)

### 其中top与ascent是负数,是因为baseline是0.

### 根据上图,默认状态下,top与ascent在baseline上面,而安卓手机的Y轴正方向是向下的,故top与ascent是负数,所以前面提到的baseline实际上就等于-top.

按照网上的说法,TextView的高度实际上是 top与 bottom的距离,与结果一致

文字的高度是 ascent与descent之间的距离 111.328125 + 29.296875 ≈ 140.63px,而打印出来的文字的实际大小是120px,这个结果与预期不符.

* 假设ascent与descent是动态变化的,可能是"Hello !"这个字符串并没有达到ascent的最小值.

我们知道 a 与 b 两个字符绘制的结果,b明显要冒出一个头.所以有可能是某些字符会突破天际,达到ascent与descent值

Google一圈没有发现到底用什么字符测试测试到上限值,但是不妨换一个思考方式,既然无法得到上限,可以突破下限.把TextView的字符串改为ac,看ascent与descent变化了没有.

![Image](/img/_103.png)

* 可见ascent与descent不是动态变化的,知乎里看过有人提到,TextView上下有留白,是因为默认添加了一个上下的留白.

TextView本身包含一定的FontPadding.看TextView的setIncludeFontPadding()方法.一言不合上代码

```java
 /**
     * Set whether the TextView includes extra top and bottom padding to make
     * room for accents that go above the normal ascent and descent.
     * The default is true.
     *
     * @see #getIncludeFontPadding()
     *
     * @attr ref android.R.styleable#TextView_includeFontPadding
     */
    public void setIncludeFontPadding(boolean includepad) {
        if (mIncludePad != includepad) {
            mIncludePad = includepad;

            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }
```

源码不重要,关键是注释,注释里写默认是有一定的间距的,view的高度是top与bottom之间的间距,而设置为false之后,view的高度是ascent与descent之间的间距
重新设置并打印值

```xml
<TextView
        android:id="@+id/_test"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:includeFontPadding="false"
        android:text="Hello !"
        android:textSize="60sp"/>
```

结果view的高度等于ascent与descent之间的高度.

```
top: -126.73828  ascent: -111.328125  descent: 29.296875  bottom: 32.51953  leading: 0.0
view-top: 0  view-bottom: 140 textSize: 120.0
```

结果符合源码的解释,同时完善一下上面的关于baseline的结论.

### 假设设置了android:includeFontPadding="false",则baseline实际的值等于-ascent

但是,并没有得到:

```
ascent与descent之间的距离等于textSize的大小.
```

这个结论.

那会不会是textSize的大小实际上指的并不是文字的高度,可能只是一个需要转换的值.

![Image](/img/_104.png)

截屏后使用Mark Man一量,这文字的高度妥妥的就是120px.换个说法就是

```
ascent与descent之间的距离不等于textSize的大小/高度.
```

## 分析

![Image](/img/_012.png)

假设top与ascent之间的空白是firstSpace,ascent 与文字顶端的空白是secondSpace,

```
则文字最顶端的Y坐标topTextY = firstSpace + secondSpace;(如TextView已经设置了android:includeFontPadding="false"则不需要加上firstSpace)
firstSpace = ascent - top;
secondSpace = descent - ascent - textSize;
```

按照这个理论,画一条线.
```java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = getPaint();
        float lineY = mPaint.getFontMetrics().ascent - mPaint.getFontMetrics().top + mPaint
                .getFontMetrics().descent - mPaint.getFontMetrics().ascent - mPaint.getTextSize();
        mPaint.setColor(Color.parseColor("#FF00FF"));
        canvas.drawLine(0, lineY, getWidth(), lineY, mPaint);
    }
```

![Image](/img/_005.png)

非常标准,刚好对齐,深得我意.

所以画百分号的baseline等于

```
baseLine = topTextY + -top(默认状态下,百分号的baseline) - 百分号的topTextY;
```

代码(详情请看自定义的[StyleTextView](/app/src/main/java/com/siyehua/styletextview/StyleTextView.java))

```java
 Paint mPaint = getPaint();
            mPaint.setTextSize(first.size);
            float startX = mPaint.measureText(first.content);
            float space = topSpace(mPaint);
            
            //画顶部基准线
            float lineY = mPaint.getFontMetrics().ascent - mPaint.getFontMetrics().top + mPaint
                    .getFontMetrics().descent - mPaint.getFontMetrics().ascent - mPaint
                    .getTextSize();
            mPaint.setColor(Color.parseColor("#FF00FF"));
            canvas.drawLine(0, lineY, getWidth(), lineY, mPaint);

            //画百分号
            mPaint.setTextSize(second.size);
            float baseLine = space + -mPaint.getFontMetrics().top - topSpace(mPaint);
            mPaint.setColor(second.color);
            canvas.drawText(second.content, startX, baseLine, mPaint);
```

结果(60这个字符串本身距离直线有一点间距是因为字不够长,假设使用字母 'l' 刚刚好)

![Image](/img/_006.png)

### 总结

终于要完了吗?实际上还没有

![Image](/img/_105.png)

假设TextView的宽度是match_parent,让内容居中

```xml
<com.siyehua.styletextview.StyleTextView
        android:id="@+id/tv_content"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:text="Hello !"
        android:textSize="60sp"/>
```

![Image](/img/_007.png)

会发现百分号并没有跟着跑,实际上是因为canvas.drawText()这个方法的参数,startX与BaseLine还受内容的对齐方式影响

而我绘制百分号并没有考虑这方面的因素.

假设TextView的对齐方式是居中,则startX是TextView文字的中点.

![Image](/img/_008.png)

图中红框的右边便是文字的右边,而我们需要计算文字右边减去粉色百分号%,得到到即是上标百分号%的startY

```
上标startY = 文字右边 - "%"宽度
文字右边距 = TextView宽度/2.0f + 需要绘制的文字的宽度/2.0f
需要绘制的文字的宽度 =  "60"串的宽度 + "%"百分号的宽度
```

```java
startX = getWidth() / 2.0f + (startX + mPaint.measureText(second.content)) / 2.0f -
                    mPaint.measureText(second.content) * 1.5f;
```



结果

![Image](/img/_009.png)

可以看到百分号的已经设置到了正确的位置.

同理可得(其实我懒得算了)

#### 当TextView的高度设置match_parent,且内容设置居中时,需要用同样的方法计算baseline.

这里就不在举例了.毕竟制作表情包太累了.

就酱紫.如有任何疑问请留言讨论,或者扫下方二维码联系我

![Image](/img/_106.png)
