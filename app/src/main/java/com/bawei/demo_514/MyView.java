package com.bawei.demo_514;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 类用途：
 * 作者：ShiZhuangZhuang
 * 时间：2017/5/14 19:36
 */

public class MyView extends View {
    private float XR = 360;
    private float YR = 360;
    private float R = 200;

    private int width;
    private int height;

    private final float increment = 10;
    private final float min = 50;


    private Paint mPaint;

    private float distance;

    public MyView(Context context) {
        super(context);
        initPaint();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initPaint();


    }

    private void initPaint() {

        mPaint = new Paint();

        mPaint.setAntiAlias(true);//抗锯齿


        mPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getWidth();
        height = getHeight();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        drawBg(canvas);//画黑色大圆
        drawLeftHalfCirle(canvas);//画左边白色半圆
        drawTBCirle(canvas);//画上下的四个圆
    }

    /**
     * 画黑色的背景底板
     */
    private void drawBg(Canvas canvas) {
        mPaint.setColor(Color.BLACK);//设置背景颜色为黑色
        canvas.drawCircle(XR, YR, R, mPaint);//这里加padding才有边框效果
    }

    /**
     * 画左边半圆
     */
    private void drawLeftHalfCirle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawArc(new RectF(XR - R, YR - R, XR + R, YR + R), 90, 180, true, mPaint);//90度开始画  画180度
    }

    private void drawTBCirle(Canvas canvas) {
        //画上面的白中圆
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(10);
        canvas.drawCircle(XR, YR - R / 2, R / 2, mPaint);
        //画上面的黑小圆
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10);
        canvas.drawCircle(XR, YR - R / 2, R / 6, mPaint);
        //画下面的黑中圆
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10);
        canvas.drawCircle(XR, YR + R / 2, R / 2, mPaint);
        //画下面的白小圆
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(10);
        canvas.drawCircle(XR, YR + R / 2, R / 6, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointer = event.getPointerCount();

        float x = event.getX();
        float y = event.getY();

        invalidate();

        int k = event.getAction();

        if (k == MotionEvent.ACTION_POINTER_DOWN) {
            distance = getDistance(event);
        }

        if (pointer == 1) {
            if (k == MotionEvent.ACTION_UP || k == MotionEvent.ACTION_MOVE) {
                if (x > width - R) {
                    XR = width - R;
                } else if (x < R) {
                    XR = R;
                } else {
                    XR = x;
                }

                if (y > height - R) {
                    YR = height - R;
                } else if (y < R) {
                    YR = R;
                } else {
                    YR = y;
                }
                postInvalidate();
            }

        } else if (pointer == 2 && k == MotionEvent.ACTION_MOVE) {

            float maxR = 0;

            if (XR < YR) {
                maxR = XR;
            } else {
                maxR = YR;
            }

            //判断手指方向
            if (distance > getDistance(event)) {
                R -= increment;
                if (R < min) {
                    R = min;
                }
            } else if (distance < getDistance(event)) {
                R += increment;
                if (R > maxR) {
                    R = maxR;
                } else if (R > width / 2) {
                    R = width / 2;
                }
            }


            distance = getDistance(event);
            postInvalidate();
        }
        return true;
    }


    private float getDistance(MotionEvent event) {

        float xOne = event.getX(0);
        float yOne = event.getY(0);
        float xTwo = event.getX(1);
        float yTwo = event.getY(1);

        return (xOne - xTwo) * (xOne - xTwo) + (yOne - yTwo) * (yOne - yTwo);
    }
}
