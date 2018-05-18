package com.practice.jamesliandroid.selfdesignview.learning;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

public class SelfView5 extends View {
    // 时间
    long time = 0;
    // 倒计时的文本
    String outText = "";
    // 外圈的圆环
    Path pathCircle;
    // 测量类
    PathMeasure measure;
    // 路径总长度
    float pathLength = 0;
    // 圆的x，y坐标
    float[] xy;
    // 圆环路径
    Path workerPath;
    // 画笔
    Paint paint;

    int w;
    int h;

    public SelfView5(Context context) {
        super(context);
        initPaint(context);
    }

    public SelfView5(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public SelfView5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        w = wm.getDefaultDisplay().getWidth();
        h = wm.getDefaultDisplay().getHeight();

        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        // 初始化路径
        initPath();
    }

    private void initPath() {
        // 细圆环路径
        pathCircle = new Path();
        RectF rectF = new RectF(-300, -300, 300, 300);
        pathCircle.addArc(rectF, 270f, 359.9f);
        measure = new PathMeasure();
        measure.setPath(pathCircle,false);
        // 出圆环path
        workerPath = new Path();
        // 总路径长度
        pathLength = measure.getLength();
        xy = new float[2];
        initAnimation();
    }

    private void initAnimation() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, pathLength);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentLength = (float) animation.getAnimatedValue();
                // 获取当前路径长度的终点
                measure.getPosTan(currentLength, xy, null);
                // 获取路径
                measure.getSegment(0, currentLength, workerPath, true);
                // 获取动画时长
                time = (animation.getDuration() - animation.getCurrentPlayTime());
                if (time > 0) {
                    outText = "00:0" + (time/ 1000 + 1);
                } else {
                    outText = "00:00";
                }
                postInvalidate();
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(8000);
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 移动画布圆心
        canvas.translate(w / 2, h / 2);
        // 固定圆环
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        paint.setColor(Color.parseColor("#f5dcc0"));
        canvas.drawCircle(0,0,300, paint);

        // 绘制外层路径
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(9);
        paint.setColor(Color.parseColor("#f4bf69"));
        canvas.drawPath(workerPath, paint);

        // 绘制移动的圆
        paint.setColor(Color.parseColor("#f19734"));
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawCircle(xy[0], xy[1], 50, paint);

        // 绘制移动圆内的时间
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setTextSize(25);
        canvas.drawText(outText, xy[0] - 39, xy[1] + 10, paint);
    }
}
