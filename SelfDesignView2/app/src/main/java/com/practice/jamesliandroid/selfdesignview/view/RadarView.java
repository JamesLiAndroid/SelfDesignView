package com.practice.jamesliandroid.selfdesignview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.practice.jamesliandroid.selfdesignview.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RadarView extends View {

    private static final int DEFAULT_COLOR = Color.parseColor("#9107F4");

    // 定义的变量，通过attr文件读取
    private int mCircleColor = DEFAULT_COLOR;

    private int mCircleNum = 3;

    private int mSweepColor = DEFAULT_COLOR;

    private int mRaindropColor = DEFAULT_COLOR;

    private int mRaindropNum = 4;

    private boolean isShowCross = true;

    private boolean isShowRaindrop = true;

    private float mSpeed = 10;

    private float mFlicker = 10;

    private float mDegrees;
    private boolean isScanning = true;
    private List<RainDrop> mRainDrops = new ArrayList<>();

    // 定义三支画笔
    private Paint mCirclePaint;
    private Paint mRaindropPaint;
    private Paint mSweepPaint;

    // private Paint mWordsPaint;

    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init();
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        init();
    }

//    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStrokeWidth(1);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setAntiAlias(true);

        mRaindropPaint = new Paint();
        mRaindropPaint.setStyle(Paint.Style.FILL);
        mRaindropPaint.setAntiAlias(true);

        mSweepPaint = new Paint();
        mSweepPaint.setAntiAlias(true);
    }

    /**
     * 获取自定义属性
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RadarView);
            mCircleColor = typedArray.getColor(R.styleable.RadarView_circleColor, DEFAULT_COLOR);
            mCircleNum = typedArray.getInt(R.styleable.RadarView_circleNum, 3);
            if (mCircleNum < 3) {
                mCircleNum = 3;
            }
            mSweepColor = typedArray.getColor(R.styleable.RadarView_sweepColor, DEFAULT_COLOR);
            mRaindropColor = typedArray.getColor(R.styleable.RadarView_raindropColor, DEFAULT_COLOR);
            mRaindropNum = typedArray.getInt(R.styleable.RadarView_raindropNum, 4);
            isShowCross = typedArray.getBoolean(R.styleable.RadarView_showCross, true);
            isShowRaindrop = typedArray.getBoolean(R.styleable.RadarView_showRaindrop, true);
            mSpeed = typedArray.getFloat(R.styleable.RadarView_speed, 3.0f);
            if (mSpeed <= 0) {
                mSpeed = 3.0f;
            }
            mFlicker = typedArray.getFloat(R.styleable.RadarView_flicker, 3.0f);
            if (mFlicker <= 0) {
                mFlicker = 3.0f;
            }

            typedArray.recycle();
        }
    }

    // 主要分为两步，测量和绘制

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int defaultSize = dp2px(getContext(), 200);
        setMeasuredDimension(measureWidth(widthMeasureSpec, defaultSize), measureHeight(heightMeasureSpec, defaultSize));
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @param defaultSize
     * @return
     */
    private int measureWidth(int widthMeasureSpec, int defaultSize) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize + getPaddingLeft() + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        result = Math.max(result, getSuggestedMinimumWidth());
        return result;
    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     * @param defaultSize
     * @return
     */
    private int measureHeight(int heightMeasureSpec, int defaultSize) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultSize + getPaddingTop() + getPaddingBottom();
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        result = Math.max(result, getSuggestedMinimumHeight());
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // 对圆的数据计算
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int radius = Math.min(width, height) / 2;

        // 计算圆心
        int cx = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        int cy = getPaddingTop() + (getHeight() - getPaddingTop() - getPaddingBottom()) / 2;

        drawCircle(canvas, cx, cy, radius);

        if (isShowCross) {
            drawCross(canvas, cx, cy, radius);
        }

        if (isScanning) {
            if (isShowRaindrop) {
                drawRaindrop(canvas, cx, cy, radius);
            }
            drawSweep(canvas, cx, cy, radius);
            mDegrees = (mDegrees + (360 / mSpeed / 60)) % 360;
            invalidate();
        }
    }

    /**
     * 绘制交叉线
     *
     * @param canvas
     * @param cx
     * @param cy
     * @param radius
     */
    private void drawCross(Canvas canvas, int cx, int cy, int radius) {
        canvas.drawLine(cx - radius, cy, cx + radius, cy, mCirclePaint);
        canvas.drawLine(cx, cy - radius, cx, cy + radius, mCirclePaint);
    }

    /**
     * 绘制扫过的角度
     *
     * @param canvas
     * @param cx
     * @param cy
     * @param radius
     */
    private void drawSweep(Canvas canvas, int cx, int cy, int radius) {
        // 扇形的透明渐变效果
        SweepGradient sweepGradient = new SweepGradient(cx, cy,
                new int[]{Color.TRANSPARENT, changeAlpha(mSweepColor, 0), changeAlpha(mSweepColor, 168),
                        changeAlpha(mSweepColor, 255), changeAlpha(mSweepColor, 255)},
                new float[]{0.0f, 0.6f, 0.99f, 0.998f, 1f});
        mSweepPaint.setShader(sweepGradient);
        // 先旋转画布，再绘制扫描的颜色渲染
        canvas.rotate(-90 + mDegrees, cx, cy);
        canvas.drawCircle(cx, cy, radius, mSweepPaint);
    }

    /**
     * 绘制雨点图
     *
     * @param canvas
     * @param cx
     * @param cy
     * @param radius
     */
    private void drawRaindrop(Canvas canvas, int cx, int cy, int radius) {
        generateRainDrops(cx, cy, radius);
        for (RainDrop rainDrop : mRainDrops) {
            mRaindropPaint.setColor(rainDrop.changeAlpha());
            canvas.drawCircle(rainDrop.x, rainDrop.y, rainDrop.radius, mRaindropPaint);
            // 水滴扩散和透明的效果
            rainDrop.radius += 1.0f * 20 / 60 / mFlicker;
            rainDrop.alpha -= 1.0f * 255 / 60 / mFlicker;
        }
        removeRainDrops();
    }

    /**
     * 生成雨滴列表
     *
     * @param cx
     * @param cy
     * @param radius
     */
    private void generateRainDrops(int cx, int cy, int radius) {
        // 最多同时存在多少个水滴
        if (mRainDrops.size() < mRaindropNum) {
            // 随机生成一个20以内的数字，如果数字是0，则生成一个水滴
            boolean probability = (int) (Math.random() * 20) == 0;
            if (probability) {
                int x = 0;
                int y = 0;
                int xOffset = (int) (Math.random() * (radius - 20));
                int yOffset = (int) (Math.random() * (int) Math.sqrt(1.0 * (radius - 20) * (radius - 20) - xOffset * xOffset));

                if (((int) Math.random() * 2) == 0) {
                    x = cx - xOffset;
                } else {
                    x = cx + xOffset;
                }
                if (((int) Math.random() * 2) == 0) {
                    y = cy - yOffset;
                } else {
                    y = cy + yOffset;
                }

                mRainDrops.add(new RainDrop(x, y, 0f, mRaindropColor));

            }
        }
    }

    private void removeRainDrops() {
        Iterator<RainDrop> iterator = mRainDrops.iterator();
        while (iterator.hasNext()) {
            RainDrop rainDrop = iterator.next();
            if (rainDrop.radius > 20 || rainDrop.alpha < 0) {
                iterator.remove();
            }
        }
    }


    /**
     * 圆的绘制
     *
     * @param canvas
     * @param radius
     * @param cx
     * @param cy
     */
    private void drawCircle(Canvas canvas, int cx, int cy, int radius) {
        for (int i = 0; i < mCircleNum; i++) {
            canvas.drawCircle(cx, cy, radius - (radius / mCircleNum) * i, mCirclePaint);
        }
    }


    /**
     * dp转px方法
     *
     * @param context
     * @param dpval
     * @return
     */
    private int dp2px(Context context, float dpval) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpval, context.getResources().getDisplayMetrics());
    }

    /**
     * 改变颜色的透明度
     *
     * @param color
     * @param alpha
     * @return
     */
    public static int changeAlpha(int color, int alpha) {
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);
        return Color.argb(alpha, red, blue, green);
    }

    /**
     * 水滴数据类
     */
    private static class RainDrop {
        int x;
        int y;
        float radius;
        int color;
        float alpha = 255;

        public RainDrop(int x, int y, float radius, int color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }

        public int changeAlpha() {
            return RadarView.changeAlpha(color, (int) alpha);
        }
    }
}
