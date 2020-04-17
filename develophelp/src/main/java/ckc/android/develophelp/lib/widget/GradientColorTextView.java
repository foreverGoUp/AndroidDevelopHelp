package ckc.android.develophelp.lib.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import ckc.android.develophelp.lib.R;


/**
 * @author ckc
 * @date 2018/11/7 9:31
 * 文本颜色渐变的TextView
 */
public class GradientColorTextView extends AppCompatTextView {

    private LinearGradient mLinearGradient;
    private Paint mPaint;
    private int mViewWidth = 0;
    private Rect mTextBound = new Rect();
    private int mStartColor;
    private int mEndColor;

    public GradientColorTextView(Context context) {
        super(context);
    }

    public GradientColorTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GradientColorTextView);
        mStartColor = typedArray.getColor(R.styleable.GradientColorTextView_startColor, Color.RED);
        mEndColor = typedArray.getColor(R.styleable.GradientColorTextView_endColor, Color.BLUE);
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mViewWidth = getMeasuredWidth();
        mPaint = getPaint();
        String mTipText = getText().toString();
        mPaint.getTextBounds(mTipText, 0, mTipText.length(), mTextBound);
        mLinearGradient = new LinearGradient(0, 0, mViewWidth, 0,
                new int[]{mStartColor, mEndColor},
                null, Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
        canvas.drawText(mTipText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
    }


}
