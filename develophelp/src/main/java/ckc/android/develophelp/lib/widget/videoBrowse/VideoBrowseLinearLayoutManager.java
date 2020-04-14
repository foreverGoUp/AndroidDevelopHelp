package ckc.android.develophelp.lib.widget.videoBrowse;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * ckc 200414
 * successfulpeter@163.com
 * <p>
 * 使用：
 * VideoBrowseLinearLayoutManager linearLayoutManager = new VideoBrowseLinearLayoutManager(this);
 * linearLayoutManager.setItemStopMidWhenSlideByFinger(mDataBinding.rv);
 * mDataBinding.rv.setLayoutManager(linearLayoutManager);
 * <p>
 * 功能：
 * 调用mDataBinding.rv.smoothScrollToPosition(num)方法跳转到指定位置后item在屏幕中心
 * setItemStopMidWhenSlideByFinger
 */
public class VideoBrowseLinearLayoutManager extends LinearLayoutManager {

    private float mScrollRatio = 1f;


    public VideoBrowseLinearLayoutManager(Context context) {
        super(context);
    }

    public VideoBrowseLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public VideoBrowseLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 缓慢滚动
     */
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//        super.smoothScrollToPosition(recyclerView, state, position);
        CenterSmoothScroller scroller = new CenterSmoothScroller(recyclerView.getContext());
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    /**
     * 控制水平滚动的速度
     * <p>
     * 被RecyclerView调用
     * <p>
     * 缺点：smoothScrollToPosition方法可能也会调用该方法，所以会影响smoothScrollToPosition的正常使用。
     */
    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollHorizontallyBy((int) (dx * mScrollRatio), recycler, state);
    }

    /**
     * 控制垂直滚动的速度
     * <p>
     * 被RecyclerView调用
     */
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        return super.scrollVerticallyBy((int) (dy * mScrollRatio), recycler, state);
    }

    /**
     * 提供手指滑动列表停止时item在屏幕中心
     */
    public VideoBrowseLinearLayoutManager setItemStopMidWhenSlideByFinger(RecyclerView recyclerView) {
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        return this;
    }

}
