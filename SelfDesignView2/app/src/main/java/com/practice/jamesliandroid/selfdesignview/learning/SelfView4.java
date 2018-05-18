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

public class SelfView4 extends View {

    Paint paint;
    Path searchPath; // 搜索的圆
    Path circlePath; // 辅助外圆

    // 布局的宽高
    int w;
    int h;

    // 保存截取时返回的坐标点
    float serpos[];
    float cicpos[];

    // 测量path的类
    PathMeasure measure;
    // 动画产生的实时数据
    float value;

    public SelfView4(Context context) {
        super(context);
        initAll(context);
    }

    public SelfView4(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAll(context);
    }

    public SelfView4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAll(context);
    }

    private void initAll(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        w = wm.getDefaultDisplay().getWidth();
        h = wm.getDefaultDisplay().getHeight();
        // 初始化数据
        serpos = new float[2];
        cicpos = new float[2];

        // 画笔
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setDither(true);
        paint.setStrokeWidth(6);
        paint.setAntiAlias(true);
        // 初始化路径
        initPath();
        // 初始化动画
        initAnimator();
    }

    /**
     * 初始化所有路径
     */
    private void initPath() {
        paint.setColor(Color.BLUE);
        // 搜索的圆
        searchPath = new Path();
        RectF rectF = new RectF(-50, -50, 50, 50); // 范围大小
        searchPath.addArc(rectF, 45, 359.9f); // 从45度，到360度
        // 辅助圆
        circlePath = new Path();
        RectF rectF1 = new RectF(-100, -100, 100, 100);
        circlePath.addArc(rectF1, 45, 359.9f);
        // 测量类
        // 重点：
        measure = new PathMeasure();
        measure.setPath(searchPath, false);
        // 获取坐标
        measure.getPosTan(0, serpos, null);

        measure.setPath(circlePath, false);
        // 获取坐标
        measure.getPosTan(0, cicpos, null);
        // 根据两点坐标绘制线
        searchPath.lineTo(cicpos[0], cicpos[1]);
    }

    /**
     * 初始化动画
     */
    private void initAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(2600);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                value = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 移动画布的原点到屏幕中心
        canvas.translate(w / 2, h / 5);
        paint.setColor(Color.BLUE);
        // 这是截取方法返回的路径
        Path pathDra = new Path();
        // 重置，防止硬件加速的影响
        pathDra.reset();
        pathDra.lineTo(0,0);
        // 设置当前需要测量的path
        measure.setPath(searchPath, false);
        // 开始截取
        boolean s = measure.getSegment(measure.getLength() * value, measure.getLength(), pathDra, true);
        // 绘制路径
        canvas.drawPath(pathDra, paint);
    }
}
