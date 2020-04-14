package ckc.android.develophelp.lib.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * 可控制滑动惯性（fling）速度的RecyclerView
 * <p>
 * Created by ckc on 200414.
 * <p>
 * 功能：
 * setFlingRatio
 */
public class FlingSpeedRecyclerView extends RecyclerView {

    public static boolean DEBUG = false;
    private final String TAG = this.getClass().getSimpleName();

    private float mFlingRatio = 1f; // 惯性滑动比例
    private int mFlingMaxVelocity = 2000; // 最大顺时滑动速度

    public FlingSpeedRecyclerView(Context context) {
        super(context);
    }

    public FlingSpeedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingSpeedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
//        velocityX = solveVelocity(velocityX);
//        velocityY = solveVelocity(velocityY);
        if (DEBUG)
            Log.e(TAG, "fling velocityX=" + velocityX + ",velocityY=" + velocityY);
        velocityX *= mFlingRatio;
        velocityY *= mFlingRatio;
        return super.fling(velocityX, velocityY);
    }

    private int solveVelocity(int velocity) {
        if (velocity > 0) {
            return Math.min(velocity, mFlingMaxVelocity);
        } else {
            return Math.max(velocity, -mFlingMaxVelocity);
        }
    }

    /**
     * @param flingRatio 和滚动速度相乘。
     */
    public void setFlingRatio(float flingRatio) {
        mFlingRatio = flingRatio;
    }
}
