package ckc.android.develophelp.lib.widget.avatarOverlapList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ckc.android.develophelp.lib.R;

/**
 * created by ckc on 2020-04-09
 * successfulpeter@163.com
 * <p>
 * 头像水平排列部分重叠的列表控件
 * <p>
 * 功能：
 * -自定义属性：重叠宽度、头像宽度、显示样式
 */
public class AvatarOverlapListView<T> extends ViewGroup {

    public static final int STYLE_LEFT_TO_RIGHT = 0;
    public static final int STYLE_RIGHT_TO_LEFT = 1;

    //图片的直径
    private int mAvatarWidth;

    //覆盖的宽度
    private int mOverlapWidth;

    //最终显示的图片数量
    private int mShowCount = 0;

    private List<T> mData;
    //展示的数据
    private List<T> mShowData = new ArrayList<>();

    private Adapter<T> mAdapter;

    private List<ImageView> mImageViews = new ArrayList<>();

    private int mDisplayStyle;

    public AvatarOverlapListView(Context context) {
        this(context, null, 0);
    }

    public AvatarOverlapListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarOverlapListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AvatarOverlapListView);
        mOverlapWidth = (int) array.getDimension(R.styleable.AvatarOverlapListView_overlap_width, 0);
        mAvatarWidth = (int) array.getDimension(R.styleable.AvatarOverlapListView_avatar_width, 50);
        mDisplayStyle = array.getInt(R.styleable.AvatarOverlapListView_display_style, STYLE_LEFT_TO_RIGHT);
        //两个图片重叠的宽度不应该大于图片的直径
        if (mOverlapWidth >= mAvatarWidth) {
            mOverlapWidth = 0;
        }
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mData == null || mData.isEmpty()) return;
        int usableWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        int maxShowCounts = (usableWidth - mOverlapWidth) / (mAvatarWidth - mOverlapWidth);
        mShowData.clear();
        if (maxShowCounts < mData.size()) {
            for (int i = 0; i < maxShowCounts; i++) {
                mShowData.add(mData.get(i));
            }
        } else {
            mShowData.addAll(mData);
        }
        mShowCount = mShowData.size() > 6 ? 6 : mShowData.size();
        //测量子view
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int widthSpec = MeasureSpec.makeMeasureSpec(mAvatarWidth, MeasureSpec.EXACTLY);
            child.measure(widthSpec, widthSpec);
        }

        int width = (mAvatarWidth - mOverlapWidth) * mShowCount + mOverlapWidth;
        int height = mAvatarWidth + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed) {
            if (mShowData == null || mAdapter == null) return;
            if (mDisplayStyle == STYLE_RIGHT_TO_LEFT) {
                Collections.reverse(mShowData);
            }
            for (int i = 0; i < mShowCount; i++) {
                ImageView childView = (ImageView) getChildAt(i);
                mAdapter.onDisplay(getContext(), childView, mShowData.get(i));
                int left;
                if (mDisplayStyle == STYLE_RIGHT_TO_LEFT) {
                    left = getPaddingLeft() + (mAvatarWidth - mOverlapWidth) * (mShowCount - i - 1);
                } else {
                    left = getPaddingLeft() + (mAvatarWidth - mOverlapWidth) * i;
                }
                int right = left + mAvatarWidth;
                int top = getPaddingTop();
                int bottom = getPaddingTop() + mAvatarWidth;
                childView.layout(left, top, right, bottom);
            }
        }
    }

    public void setData(List<T> list) {
        if (list == null || list.isEmpty()) {
            setVisibility(View.GONE);
            return;
        } else {
            setVisibility(View.VISIBLE);
        }
        mData = list;

        for (int i = 0; i < mData.size(); i++) {
            ImageView iv = getImageView(i);
            if (iv == null) {
                return;
            }
            addView(iv, generateDefaultLayoutParams());
        }
        requestLayout();
    }

    private ImageView getImageView(final int position) {
        if (position < mImageViews.size()) {
            return mImageViews.get(position);
        } else {
            if (mAdapter != null) {
                ImageView iv = mAdapter.onGenerateItemView(getContext());
                mImageViews.add(iv);
                return iv;
            } else {
                return null;
            }
        }
    }

    public void setAdapter(Adapter<T> adapter) {
        this.mAdapter = adapter;
    }
}
