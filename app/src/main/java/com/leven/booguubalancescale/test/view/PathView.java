package com.leven.booguubalancescale.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.leven.booguubalancescale.BuildConfig;
import com.leven.booguubalancescale.test.pojo.PointEntity;

import java.util.ArrayList;

/**
 * BooguuBalanceScale
 *
 * @author leven.chen
 * @email chenlong_cl@foxmail.com
 * 2017/3/25.
 */

public class PathView extends View {

    private int width;//设置高
    private int height;//设置高
    private int centerXY;
    private Path mPath;
    private Paint mPaint;
    private ArrayList<PointEntity> points;


    public PathView(Context context, ArrayList<PointEntity> points) {
        super(context);
        this.points = points;
        init();
    }


    public PathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }


    public void init() {
        // 初始化path
        mPath = new Path();
        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true); //抗锯齿
        mPaint.setTextSize(30);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);//非填充
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        centerXY = right / 2;
        super.onLayout(changed, left, top, right, bottom);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);//设置宽和高
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!points.isEmpty()) {
            for (int i = 0, len = points.size(); i < len; i++) {
                PointEntity entity = points.get(i);
                if (i == 0) {
                    mPath.moveTo(entity.getX(), entity.getY());
                } else {
                    mPath.lineTo(entity.getX(), entity.getY());
                }
            }
        }
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

}
