package ckc.android.develophelp.lib.widget.rvDivider;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * @author ckc
 * 2019-10-17
 * 水平方向的Grid布局的分割线
 * <p>
 * 适配对象：Grid布局方向为水平的RecyclerView。
 * 功能：十字形样式分割线,适合无论Item四周有无阴影的RecyclerView。
 * 若期望Item四周有相同宽度的分割线，此时在布局文件的RecyclerView中添加【padding=分割线宽度】属性即可
 * ，这样的实现方式是经过三思得到的最佳方案，因此该类没有打算实现回字型的分割线的功能。
 */
public class DividerHorGridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;//分割线
    private int mHeightOfBottomShadowOfItem = 0;//item底部阴影高度

    /**
     * 适合Item四周无阴影的RecyclerView
     *
     * @param context       上下文
     * @param drawableResId 图像资源ID
     */
    public DividerHorGridItemDecoration(Context context, int drawableResId) {
        this(context, drawableResId, 0);
    }

    /**
     * 适合Item底部有阴影的RecyclerView。
     * 原理是处理最后一行Item的时候，让Item的底部多占用参数dpOfHeightOfBottomShadowOfItem的值的高度，从而让Item底部阴影能够显示出来。
     *
     * @param context                        上下文
     * @param drawableResId                  图像资源ID
     * @param dpOfHeightOfBottomShadowOfItem item底部阴影高度的dp值
     */
    public DividerHorGridItemDecoration(Context context, int drawableResId, int dpOfHeightOfBottomShadowOfItem) {
        mDivider = context.getResources().getDrawable(drawableResId);
        mHeightOfBottomShadowOfItem = (int) (dpOfHeightOfBottomShadowOfItem * (context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    /**
     * 在绘制Item前回调，相当于画Item的背景图案。
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

        drawHorizontal(c, parent);
        drawVertical(c, parent);

    }

    /**
     * 在绘制Item后回调，相当于画Item的覆盖图案。
     */
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    /**
     * 获得列数
     */
    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager && ((GridLayoutManager) layoutManager).getOrientation() == GridLayoutManager.HORIZONTAL) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else {
            throw new IllegalStateException("This item decoration is not for the RecyclerView!");
        }
        return spanCount;
    }

    /**
     * 在item底部画分割线，分隔线宽度大于item宽度一个分割线内在宽度，高度为分割线内在高度。
     */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin
                    + mDivider.getIntrinsicWidth();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 在item右边画分割线，分隔线高度与item一致，宽度为分割线内在宽度。
     */
    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    /**
     * 是否是第一列
     */
    private boolean isFirstColumn(RecyclerView parent, int childCount, int spanCount, int pos) {
        return false;
    }

    /**
     * 是否是最后一列
     */
    private boolean isLastColumn(RecyclerView parent, int childCount, int spanCount, int pos) {
        if (childCount % spanCount == 0) {
            if (pos >= childCount - spanCount) return true;
        } else {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount) return true;
        }
        return false;
    }

    /**
     * 是否是第一行
     */
    private boolean isFirstRaw(RecyclerView parent, int childCount, int spanCount, int pos) {
        if (pos % spanCount == 0) return true;
        return false;
    }

    /**
     * 是否是最后一行
     */
    private boolean isLastRaw(RecyclerView parent, int childCount, int spanCount, int pos) {
        if ((pos % spanCount) == (spanCount - 1)) return true;
        return false;
    }

    /**
     * 控制Item使用的区域，使分割线显示出来。
     */
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int itemPosition = parent.getChildLayoutPosition(view);
        if (isFirstRaw(parent, childCount, spanCount, itemPosition)) {//如果是第一行，画item底部一半分割线和右边分割线。
            if (isLastColumn(parent, childCount, spanCount, itemPosition)) {//如果是最后一列，则item右边不画分割线
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight() / 2);
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight() / 2);
            }
        } else if (isLastRaw(parent, childCount, spanCount, itemPosition))// //如果是最后一行，画item顶部一半分割线和右边分割线。
        {
            if (isLastColumn(parent, childCount, spanCount, itemPosition)) {//如果是最后一列，则item右边不画分割线
                outRect.set(0, mDivider.getIntrinsicHeight() / 2, 0, mHeightOfBottomShadowOfItem);
            } else {
                outRect.set(0, mDivider.getIntrinsicHeight() / 2, mDivider.getIntrinsicWidth(), mHeightOfBottomShadowOfItem);
            }
        } else//非第一行和非最后一行的，画item顶底两边各一半分割线和右边分割线。
        {
            if (isLastColumn(parent, childCount, spanCount, itemPosition)) {//如果是最后一列，则item右边不画分割线
                outRect.set(0, mDivider.getIntrinsicHeight() / 2, 0,
                        mDivider.getIntrinsicHeight() / 2);
            } else {
                outRect.set(0, mDivider.getIntrinsicHeight() / 2, mDivider.getIntrinsicWidth(),
                        mDivider.getIntrinsicHeight() / 2);
            }
        }
    }
}
