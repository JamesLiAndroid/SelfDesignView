package com.practice.jamesliandroid.selfdesignview.learning;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;

public class SelfView3 extends View {

    private static final int DEFAULT_GREEN_COLOR = 0x0CEA38;
    private static final int DEFAULT_BLACK_COLOR = 0x717D73;

    private int screenWidth;
    private int screenHeight;

    private int scWidth = 0;

    int transformDark = DEFAULT_BLACK_COLOR;

    Paint paint;

    int scanLineHeight = 0;

    public SelfView3(Context context) {
        super(context);
        initPaint(context);
    }

    public SelfView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        initPaint(context);
    }

    public SelfView3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        initPaint(context);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
    }

    private void initPaint(Context context) {
        scWidth = dp2px(context, 80f);
        // 屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm.getDefaultDisplay().getWidth();
        screenHeight = wm.getDefaultDisplay().getHeight();

        // 画笔
        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            screenWidth = 400;
            screenHeight = 600;
            scWidth = 40;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1);
        // 绘制中间的正方形
        RectF rectF = getRect(scWidth, screenWidth, screenHeight);
        paint.setColor(Color.parseColor("#00000000"));
        canvas.drawRect(rectF, paint);

        // 绘制扫描区域以外的区域--上部
        RectF top = new RectF(0, 0, screenWidth, rectF.top);
        paint.setColor(transformDark);
        canvas.drawRect(top, paint);

        // 绘制扫描区域以外的区域--左侧
        RectF left = new RectF(0, rectF.top, rectF.left, rectF.bottom);
        canvas.drawRect(left, paint);

        // 绘制扫描区域以外的区域--底部
        RectF bottom = new RectF(0, rectF.bottom, screenWidth, screenHeight);
        canvas.drawRect(bottom, paint);

        // 绘制扫描区域以外的区域--底部
        RectF right = new RectF(rectF.right, rectF.top, screenWidth, rectF.bottom);
        canvas.drawRect(right, paint);

        // 绘制四角的宽线
        strokeLine(rectF, canvas);
        // 绘制扫描线
        scanLine(rectF, canvas);
    }

    /**
     * 绘制各个角上的绿色短粗线
     * @param rectF
     * @param canvas
     */
    private void strokeLine(RectF rectF, Canvas canvas) {
        paint.setColor(Color.parseColor("#c000ff00"));
        paint.setStrokeWidth(10);

        // 左上角
        canvas.drawLine(rectF.left, rectF.top+5, rectF.left+40, rectF.top+5, paint);
        canvas.drawLine(rectF.left + 5, rectF.top, rectF.left+5, rectF.top+40, paint);

        // 右上角
        canvas.drawLine(rectF.right, rectF.top + 5, rectF.right - 40, rectF.top + 5, paint);
        canvas.drawLine(rectF.right - 5, rectF.top, rectF.right - 5, rectF.top + 40, paint);


        // 左下角
        canvas.drawLine(rectF.left, rectF.bottom - 5, rectF.left + 40, rectF.bottom - 5, paint);
        canvas.drawLine(rectF.left + 5, rectF.bottom, rectF.left + 5, rectF.bottom - 40, paint);

        // 右下角
        canvas.drawLine(rectF.right, rectF.bottom - 5, rectF.right - 40, rectF.bottom - 5, paint);
        canvas.drawLine(rectF.right - 5, rectF.bottom, rectF.right - 5, rectF.bottom - 40, paint);
    }

    /**
     * 绘制扫码时的扫描线
     * @param rectF
     * @param canvas
     */
    private void scanLine(RectF rectF, Canvas canvas) {
        paint.setColor(Color.BLUE);
        canvas.drawLine(rectF.left, rectF.top + scanLineHeight, rectF.left + rectF.width(), rectF.top + scanLineHeight, paint);

        // 每次移动两个单位
        scanLineHeight+= 2;
        if (scanLineHeight >= rectF.height() - 10) {
            scanLineHeight = 0;
        }
        // 重新绘制
        postInvalidate();
    }


    private RectF getRect(int scWidth, int screenWidth, int screenHeight) {
        RectF rectFr;
        float coderWidth = (float) (screenWidth * 0.6);
        // 距离左侧边的距离
        float offsetLeft = (screenWidth - coderWidth) / 2;
        // 距离顶部的距离
        float offsetTop = (screenHeight - coderWidth) / 2 - scWidth / 3;

        rectFr = new RectF(offsetLeft, offsetTop, offsetLeft+coderWidth, offsetTop + coderWidth);
        return rectFr;
    }

    /**
     * dp转px
     */
    private static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
