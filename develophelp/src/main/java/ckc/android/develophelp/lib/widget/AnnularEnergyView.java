package ckc.android.develophelp.lib.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ckc on 2017/11/23.
 * <p>
 * 功能:
 * -控件时正方形的，宽度取决于布局文件中设定控件宽高值中的最小值。
 * <p>
 * 使用方法：
 * 1、在布局文件中加入控件
 * <com.kc.module_home.widget.AnnularEnergyView
 * android:id="@+id/annularEnergyView"
 * android:layout_width="@dimen/dp_150"
 * android:layout_height="@dimen/dp_150"/>
 * 2、在代码中找到控件，通过setCurrentEnergyWithAnimation方法改变控件状态
 * mDataBinding.annularEnergyView.setCurrentEnergyWithAnimation(num);
 */

public class AnnularEnergyView extends View {

    private static final String TAG = "AnnularEnergyView";
    private final int DEF_WIDTH = 200;
    private Paint mBgTrackPaint, mEnergyPaint, mTextPaint;
    private final String COLOR_BG_TRACK = "#B4D9CD";
    private final String COLOR_Energy = "#2EC98D";
    private final int DEF_WIDTH_BG_TRACK = 6;
    private final int DEF_WIDTH_ENERGY = 18;
    private final int DEF_WIDTH_VALUE_TEXT = 8;
    private final int DEF_WIDTH_UNIT_TEXT = 6;
    private final int DEF_WIDTH_TIP_TEXT = 2;
    private final int DEF_TEXT_SIZE_VALUE_TEXT = 56;
    private final int DEF_TEXT_SIZE_UNIT_TEXT = 14;
    private final int DEF_TEXT_SIZE_TIP_TEXT = 28;
    private final int DEF_MARGIN_VALUE_TIP = 20;

    private int mMaxEnergy = 100;
    private int currentEnergy = 50;

    public AnnularEnergyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBgTrackPaint = new Paint();
        mBgTrackPaint.setAntiAlias(true);
        mBgTrackPaint.setColor(Color.parseColor(COLOR_BG_TRACK));
        mBgTrackPaint.setStrokeWidth(DEF_WIDTH_ENERGY / 3);
        mBgTrackPaint.setStyle(Paint.Style.STROKE);
        //
        mEnergyPaint = new Paint();
        mEnergyPaint.setAntiAlias(true);
        mEnergyPaint.setColor(Color.parseColor(COLOR_Energy));
        mEnergyPaint.setStrokeWidth(DEF_WIDTH_ENERGY);
        mEnergyPaint.setStyle(Paint.Style.STROKE);
        mEnergyPaint.setStrokeCap(Paint.Cap.ROUND);
        //
        mTextPaint = new TextPaint();
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(DEF_TEXT_SIZE_VALUE_TEXT);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStrokeWidth(DEF_WIDTH_VALUE_TEXT);
        mTextPaint.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            int size = Math.min(widthSize, heightSize);
            setMeasuredDimension(size, size);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(widthSize, widthSize);
        } else if (heightMode == MeasureSpec.EXACTLY) {
            setMeasuredDimension(heightSize, heightSize);
        } else {
            setMeasuredDimension(DEF_WIDTH, DEF_WIDTH);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景固定圆轨道
        int trackMargin = (DEF_WIDTH_ENERGY - DEF_WIDTH_BG_TRACK) / 2;
        float cx = getWidth() / 2;
        float r = cx - (trackMargin + DEF_WIDTH_BG_TRACK / 2);
        canvas.drawCircle(cx, cx, r, mBgTrackPaint);
        //画弧形"能量部分"
        RectF rectF = new RectF(0 + DEF_WIDTH_ENERGY / 2, 0 + DEF_WIDTH_ENERGY / 2, getWidth() - DEF_WIDTH_ENERGY / 2, getWidth() - DEF_WIDTH_ENERGY / 2);
        canvas.drawArc(rectF, -90, -(360 * currentEnergy / mMaxEnergy), false, mEnergyPaint);
        //画文字"当前能量值"
        drawText(canvas, DEF_TEXT_SIZE_VALUE_TEXT, DEF_WIDTH_VALUE_TEXT, String.valueOf(currentEnergy), cx, cx);
        //画文字"KM"
        float x = cx + DEF_TEXT_SIZE_VALUE_TEXT + DEF_MARGIN_VALUE_TIP / 2;
        float y = cx - DEF_TEXT_SIZE_VALUE_TEXT + DEF_TEXT_SIZE_UNIT_TEXT;
        drawText(canvas, DEF_TEXT_SIZE_UNIT_TEXT, DEF_WIDTH_UNIT_TEXT, "KM", x, y);
        //画文字"剩余电量"
        y = cx + DEF_MARGIN_VALUE_TIP + DEF_TEXT_SIZE_TIP_TEXT;
        drawText(canvas, DEF_TEXT_SIZE_TIP_TEXT, DEF_WIDTH_TIP_TEXT, "剩余电量", cx, y);
    }

    private void drawText(Canvas canvas, float textSize, float strokeW, String text, float x, float y) {
        mTextPaint.setTextSize(textSize);
        mTextPaint.setStrokeWidth(strokeW);
        canvas.drawText(text, x, y, mTextPaint);
    }

    public int getCurrentEnergy() {
        return currentEnergy;
    }

    public void setCurrentEnergy(int currentEnergy) {
        this.currentEnergy = currentEnergy;
        int green = 255 * currentEnergy / mMaxEnergy;
        int red = 255 - green;
        Log.e(TAG, "setCurrentEnergy: " + currentEnergy);
        mBgTrackPaint.setColor(Color.argb(255, red, green, 0));
        mEnergyPaint.setColor(Color.argb(255, red, green, 0));
        invalidate();
    }

    public void setCurrentEnergyWithAnimation(int currentEnergy) {
        this.currentEnergy = currentEnergy;
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(this, "currentEnergy", mMaxEnergy, currentEnergy);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }

}
