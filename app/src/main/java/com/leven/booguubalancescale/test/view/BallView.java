package com.leven.booguubalancescale.test.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.leven.booguubalancescale.R;

/**
 * BooguuBalanceScale
 * 自定义小球
 *
 * @author leven.chen
 * @email chenlong_cl@foxmail.com
 * 2017/3/19.
 */

public class BallView extends View {
    private static final String TAG="BallView";
    //Path
    private Bitmap mBitmap;
    private Paint pathPaint;
    private Paint mBitmapPaint;
    private Path mPath;
    private Canvas mCanvas;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 2;
    //Ball
    private float centerXY=0;
    private float bitmapX = 0;
    private float bitmapY = 0;
    private Paint ballPaint;
    private Bitmap ballBitmap;
    private Context mContext;

    public BallView(Context context) {
        super(context);
        init(context);
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext=context;
        ballPaint = new Paint(); // 创建并实例化Paint的对象
        ballBitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ball); // 根据图片生成位图对象
        //init path
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setDither(true);
        pathPaint.setColor(Color.GREEN);
        pathPaint.setStyle(Paint.Style.STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(12);
    }


    /**
     * 移动小球
     *
     * @param x
     * @param y
     */
    public void move(float x, float y) {
        bitmapX = x;
        bitmapY = y;
        invalidate();
    }

    public void calibrate(){
        move(centerXY,centerXY);
    }

    /**
     * 绘制路径
     *
     * @param x
     * @param y
     */
    public void drawPath(float x, float y) {
        bitmapX = centerXY;
        bitmapY = centerXY;
        touch_move(x, y);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG, "onLayout: :"+left+"--"+top+"--"+right+"--"+bottom);
        centerXY=right/2-35;
        bitmapY=centerXY;
        bitmapX=centerXY;
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pathX = 0;
        float pathY = 0;
        if (true) {
            bitmapX = event.getX();
            bitmapY = event.getY();
        } else {
            pathX = event.getX();
            pathY = event.getY();
        }


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(pathX, pathY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(pathX, pathY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(ballBitmap, bitmapX, bitmapY, ballPaint); // 绘制小球
        if (ballBitmap.isRecycled()) { // 判断图片是否回收
            ballBitmap.recycle(); // 强制回收图片
        }
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, pathPaint);
    }



    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, pathPaint);
        mPath.reset();
    }


}
