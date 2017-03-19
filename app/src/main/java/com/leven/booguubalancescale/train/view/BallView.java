package com.leven.booguubalancescale.train.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

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
    private static final String TAG = "BallView";
    private float bitmapX = 446;
    private float bitmapY = 446;

    public BallView(Context context) {
        super(context);
    }

    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public BallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void move(float x, float y) {
        bitmapX = x;
        bitmapY = y;
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        bitmapX = event.getX();
        bitmapY = event.getY();
        Log.i(TAG, "onTouchEvent: " + bitmapY + "-" + bitmapX);
        postInvalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint(); // 创建并实例化Paint的对象
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.ball); // 根据图片生成位图对象
        canvas.drawBitmap(bitmap, bitmapX, bitmapY, paint); // 绘制小球
        if (bitmap.isRecycled()) { // 判断图片是否回收
            bitmap.recycle(); // 强制回收图片
        }
    }


}
