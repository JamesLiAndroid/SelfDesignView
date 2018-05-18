package com.practice.jamesliandroid.selfdesignview.learning;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.practice.jamesliandroid.selfdesignview.R;

/**
 * 第一步：定义一个死View
 * 没有交互，只有静态内容
 */
public class SelfView1 extends View {
    private static final int DEFAULT_COLOR = Color.RED;

    float radiusArch;
    int bgColor;

    Paint paint;

    int widthSize;
    int heightSize;
    /**
     *  1.1 创建三个构造方法
     *  让他相互之间进行调用
     *  将初始化放到最后一个构造方法中
     **/

    public SelfView1(Context context) {
        super(context, null);
        initPaint();
    }

    public SelfView1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        getAttr(context, attrs);
        initPaint();
    }

    public SelfView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttr(context, attrs);
        initPaint();
    }

    /**
     * 1.3 获取布局文件中定义的属性，关联attrs.xml
     * 注意定义这些属性的变量
     * 还有创建画笔并初始化
     * **/
    private void getAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelfView1);
        radiusArch = typedArray.getDimension(R.styleable.SelfView1_radius_arch, 0);
        bgColor = typedArray.getColor(R.styleable.SelfView1_pop_bg, DEFAULT_COLOR);
    }

    private void initPaint() {
        paint = new Paint();
        // 设置画笔抗锯齿
        paint.setAntiAlias(true);
        // 设置填充
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        // 设置防抖动
        paint.setDither(true);
    }


    /**
     * 1.5 对View开始测量和绘制
     * 对宽高进行测量，注意要定义宽高的变量
     * 测量时，先获取测量的模式，再获取测量的宽高值
     * 模式：
     * MeasureSpec.AT_MOST: 针对wrap_content
     * MeasureSpec.EXACTLY: 具体的宽高尺寸
     * MeasureSpec.UNSPECIFIED: 无限制
     *
     * **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);

        // 如果设置为wrap_content, 就对其进行指定大小
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            widthSize = 200;
            heightSize = 200;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 1.6 绘制内容
     * 需要注意的是，在动态绘制的过程中，重绘View的时候有两个回调方法：
     * postInvalidate():针对子View进行重绘
     * invalidate()：重绘当前的View
     **/

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(bgColor);

        // 先绘制矩形
        RectF rectF = new RectF(0, 0, widthSize, heightSize - 20);
        canvas.drawRoundRect(rectF, radiusArch, radiusArch, paint);

        // 绘制底部三角形
        Path path = new Path();
        path.moveTo((widthSize / 2) - 50, heightSize - 20);
        path.lineTo(widthSize / 2, heightSize);
        path.lineTo(widthSize / 2 + 50, heightSize - 20);
        path.close();
        canvas.drawPath(path, paint);

    }
}
