package com.practice.jamesliandroid.selfdesignview.learning;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import com.practice.jamesliandroid.selfdesignview.R;

public class SelfView6 extends View {
    Paint paint;

    // 屏幕宽高
    int w;
    int h;

    // 定义了一个高度变化
    float y;
    // xformode的类型选择
    PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    // 图片
    Bitmap bitmap;
    public SelfView6(Context context) {
        super(context);
        initPaint(context);
    }

    public SelfView6(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public SelfView6(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    private void initPaint(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        initOthers(context);

    }

    private void initOthers(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        w = wm.getDefaultDisplay().getWidth();
        h = wm.getDefaultDisplay().getHeight();
        // 加载bitmap图片
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.usage);
        // 开始动画
        animator();
    }

    private void animator() {
        ValueAnimator animator = ValueAnimator.ofFloat(bitmap.getHeight(), 0);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                y = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.setDuration(8000);
        animator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置图层
        int layer = canvas.saveLayer(0, 0, w, h, paint, Canvas.ALL_SAVE_FLAG);
        // 绘制背景图片
        canvas.drawBitmap(bitmap, 0, 0, paint);
        // 设置xformode模式
        paint.setXfermode(xfermode);
        // 绘制矩形
        paint.setColor(Color.RED);
        RectF rectF = new RectF(0, y, bitmap.getWidth(), bitmap.getHeight());
        canvas.drawRect(rectF, paint);
        // 最后设置为空
        paint.setXfermode(null);
        canvas.restoreToCount(layer);
    }
}
