package ckc.android.develophelp.lib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import ckc.android.develophelp.lib.R;

/**
 * @author zhy https://blog.csdn.net/lmj623565791/article/details/41967509
 * created by ckc on 2020-04-09
 * successfulpeter@163.com
 * <p>
 * 圆形图片控件
 * <p>
 * 功能：
 * 支持圆形
 * 支持自定义角落半径的方形
 * 支持绘制圆形类型时边界某种颜色的圆圈
 * 自定义属性：详见style
 * 支持多种创建方式：1、xml布局2、静态方法createCircleImageView创建圆形图片控件
 */
public class CircleImageView extends AppCompatImageView {

    /**
     * 图片类型，圆角或圆形
     */
    private int mType;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_RECT = 1;

    /**
     * 圆角大小的默认值
     */
    private static final int BORDER_RADIUS_DEFAULT = 10;

    /**
     * 圆角大小
     */
    private int mCornerRadius;

    /**
     * 圆角半径
     */
    private int mRadius;

    private Paint mBitmapPaint;
    private Paint mBorderPaint;

    /**
     * 3x3矩阵，主要用于缩小放大
     */
    private Matrix mMatrix;

    /**
     * 渲染图象，使用图像为绘制图形着色
     */
    private BitmapShader mBitmapShader;

    private int mWidth;
    private RectF mRoundRectF;
    /**
     * 边界
     */
    private int mBorderColor;//边界颜色
    private int mBorderWidth;//边界宽度
    private int mBorderRadius;//边界半径

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        mType = typedArray.getInt(R.styleable.CircleImageView_type, TYPE_CIRCLE);
        mCornerRadius = typedArray.getDimensionPixelOffset(R.styleable.CircleImageView_corner_radius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BORDER_RADIUS_DEFAULT, getResources().getDisplayMetrics()));
        mBorderWidth = (int) typedArray.getDimension(R.styleable.CircleImageView_border_width, 0);
        mBorderColor = typedArray.getColor(R.styleable.CircleImageView_border_color, Color.WHITE);
        typedArray.recycle();

        init();
    }

    public CircleImageView(Context context, int type, int cornerRadius, int borderWidth, int borderColor) {
        super(context);
        mType = type;
        mCornerRadius = cornerRadius;
        mBorderWidth = borderWidth;
        mBorderColor = borderColor;
        init();
    }

    public static CircleImageView createCircleImageView(Context context, int borderWidth, int borderColor) {
        return new CircleImageView(context, TYPE_CIRCLE, 0, borderWidth, borderColor);
    }

    private void init() {
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        if (mBorderWidth > 0) {
            mBorderPaint = new Paint();
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setAntiAlias(true);
            mBorderPaint.setColor(mBorderColor);
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 如果类型为圆形，则强制使View的宽高一致，以宽高最小值为准
         */
        if (mType == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            if (mBorderWidth > 0) {
                mBorderRadius = mRadius - mBorderWidth + 2;
            }
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    /**
     * 初始化BitmapShader
     */
    private boolean setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return false;
        }
        Bitmap bmp = drawableToBitmap(drawable);
        if (bmp == null) return false;

        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        if (mType == TYPE_CIRCLE) {
            //获取bitmap宽高的最小值
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bSize;
        } else if (mType == TYPE_RECT) {
            //如果图片的宽高与view的宽高不匹配，计算出需要缩放的比例
            scale = Math.max(getWidth() * 1.0f / bmp.getWidth(),
                    getHeight() * 1.0f / bmp.getHeight());
        }
        //shader的变换矩阵，我们这里主要用于放大或者缩小
        mMatrix.setScale(scale, scale);
        //设置变换矩阵
        mBitmapShader.setLocalMatrix(mMatrix);
        //设置shader
        mBitmapPaint.setShader(mBitmapShader);
        return true;
    }

    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return Bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if (!(w > 0 && h > 0)) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        boolean b = setUpShader();
        if (b) {
            if (mType == TYPE_RECT) {
                canvas.drawRoundRect(mRoundRectF, mCornerRadius, mCornerRadius, mBitmapPaint);
            } else {
                //画图片
                canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);
                //画边界
                if (mBorderWidth > 0)
                    canvas.drawCircle(mRadius, mRadius, mBorderRadius, mBorderPaint);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /**
         * 圆角图片范围
         */
        if (mType == TYPE_RECT) {
            mRoundRectF = new RectF(0, 0, getWidth(), getHeight());
        }
    }

    public void setCornerRadius(int cornerRadius) {
        int pxVal = dp2px(cornerRadius);
        if (this.mCornerRadius != pxVal) {
            this.mCornerRadius = pxVal;
            invalidate();
        }
    }

    /**
     * 改变图片显示效果
     *
     * @param type
     */
    public void setType(int type) {
        if (this.mType != type) {
            this.mType = type;
            if (this.mType != TYPE_RECT && this.mType != TYPE_CIRCLE) {
                this.mType = TYPE_CIRCLE;
            }
            requestLayout();
        }
    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}

