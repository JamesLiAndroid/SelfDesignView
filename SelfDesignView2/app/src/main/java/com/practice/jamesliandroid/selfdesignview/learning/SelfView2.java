package com.practice.jamesliandroid.selfdesignview.learning;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * 第二课，为View添加动画
 *
 */
public class SelfView2 extends View {

    int[] colors;

    int widthSize;
    int heightSize;

    int progress = 0;
    int line = 0;
    int line2 = 0;

    AnimationIsComplete isComplete;

    public void setIsComplete(AnimationIsComplete isComplete) {
        this.isComplete = isComplete;
    }

    public SelfView2(Context context) {
        super(context);
        initColors();
    }

    public SelfView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initColors();
    }

    public SelfView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initColors();
    }

    private void initColors() {
        colors = new int[] {
          0xFF4081,
          0xFFFFFF80,
          0x303F9F,
          0xFFFF0000,
          0xFF660099,
        };
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 实现颜色的过度渐变
        Shader s = new SweepGradient(0, 0, colors, null);
        // 矩形
        RectF rectF = new RectF(15, 15, widthSize - 15, heightSize - 15);
        Paint paint = new Paint();
        paint.setShader(s);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        // 绘制圆弧，第三个参数，是从第二个参数的角度算起
        canvas.drawArc(rectF, 120, progress, false, paint);

        if (progress < 300) {
            progress += 10;
        }

        if (progress >= 300) {
            if ((widthSize - 70 -line) > 70) {
                line += 7;
            } else if ((widthSize - 70 - line2) > 70) {
                line2 += 7;
                // 动画结束，调用接口
                isComplete.isComplete();
            }

            // 设置线宽
            paint.setStrokeWidth(20);
            // 画线
            canvas.drawLine(widthSize - 70 - line, heightSize - 70 - line, widthSize - 70, heightSize - 70, paint);
            canvas.drawLine(widthSize - 70, 70, widthSize - 70 - line2,  70 + line2, paint);
        }
        // 重绘
        postInvalidate();
    }

    public interface AnimationIsComplete {
        public void isComplete();
    }
}
