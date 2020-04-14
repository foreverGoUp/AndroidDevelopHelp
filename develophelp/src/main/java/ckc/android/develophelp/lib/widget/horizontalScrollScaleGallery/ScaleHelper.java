package ckc.android.develophelp.lib.widget.horizontalScrollScaleGallery;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by ckc on 200224.
 */
class ScaleHelper {

    private final String TAG = this.getClass().getSimpleName();

    private RecyclerView mRecyclerView;
    private int mIntervalOfItem;//条目之间的间隔
    private float mScaleRatio;//最小条目Y方向的缩放值
    private int mItemWidth; // 条目宽度

    int mCurrentOffset;//列表偏移距离

    private LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {//滚动停止
                if (HorizontalScrollScaleGalleryHelper.DEBUG)
                    Log.e(TAG, "onScrollStateChanged 滚动停止");
                onScrollStop();
            } else {//滚动中,或手指滑动停止且未松开
                if (HorizontalScrollScaleGalleryHelper.DEBUG)
                    Log.e(TAG, "onScrollStateChanged 滚动中：" + newState);
            }
        }

        /**
         * RecyclerView初始化完成后会回调一次该方法
         * */
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // dx>0则表示左滑, dx<0表示右滑
            mCurrentOffset += dx;
            if (HorizontalScrollScaleGalleryHelper.DEBUG)
                Log.e(TAG, String.format("onScrolled dx=%d, dy=%d, mCurrentOffset=%d", dx, dy, mCurrentOffset));

            if (mItemWidth > 0) {
                onHandleScroll();
            }
        }
    };


//    void pauseScale(){
//        mRecyclerView.removeOnScrollListener(mOnScrollListener);
//    }
//
//    void resumeScale(int pos){
//        mCurrentOffset = pos * (mItemWidth+mIntervalOfItem);
//        mRecyclerView.addOnScrollListener(mOnScrollListener);
//
//        View view = mRecyclerView.getLayoutManager().findViewByPosition(pos - 1);
//        if (view != null){
//            view.setScaleY(mScaleRatio);
//        }
//        view = mRecyclerView.getLayoutManager().findViewByPosition(pos);
//        if (view != null){
//            view.setScaleY(1);
//        }
//        view = mRecyclerView.getLayoutManager().findViewByPosition(pos+1);
//        if (view != null){
//            view.setScaleY(mScaleRatio);
//        }
//    }


    void init(HorizontalScrollScaleGalleryHelper helper) {
        mRecyclerView = helper.mRecyclerView;
        mIntervalOfItem = helper.mIntervalOfItem;
        mScaleRatio = helper.mScaleRatio;
        mItemWidth = helper.mItemWidth;

        mRecyclerView.addOnScrollListener(mOnScrollListener);

        mLinearSnapHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * 由于mCurrentOffset的值可能并不是准确值，会和准确值相差1或-1，所以不能直接根据mCurrentOffset确定当前条目位置。
     */
    private void onScrollStop() {
        int firstCompletelyVisibleItemPosition = getFirstCompletelyVisibleItemPosition();
        if (firstCompletelyVisibleItemPosition >= 0) {
            mCurrentOffset = firstCompletelyVisibleItemPosition * (mItemWidth + mIntervalOfItem);
        }
    }

    private int getFirstCompletelyVisibleItemPosition() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (HorizontalScrollScaleGalleryHelper.DEBUG)
            Log.e(TAG, "完全可见位置：" + firstCompletelyVisibleItemPosition);
        return firstCompletelyVisibleItemPosition;
    }

    /**
     * 缩放控制原理：
     * 显示在屏幕最中间的条目是原尺寸（未缩放）。
     * 从位置0条目开始分析，当0 <= mCurrentOffset < 一个条目宽度时，
     * 表达式int currentPos = mCurrentOffset / (mItemWidth)得到的结果currentPos一直为0，
     * 所以，当位置0条目从中间原尺寸向左移动距离n,则0条目就得从原尺寸相应缩小一定值s，(s=缩放量*条目向左偏移距离/条目宽度)
     * 而此时位置1条目由于0条目向左移动了距离n，则1条目也自然移动距离n，因为1条目原来处于最大缩放值，
     * 所以1条目随着向左移动距离n，同时它要从最小尺寸开始放大一定值s.同理，当位置0又向右偏移距离m（肯定<= n ）,
     * 也可以当作位置0向左移动距离n-m。
     * <p>
     * <p>
     * 在滚动时使用mRecyclerView.getChildAt(currentPos+1)获取下一个正在首次出现的itemView可能为空，
     * 所以可以使用mRecyclerView.getLayoutManager().findViewByPosition(currentPos+1);确保能够获取。
     */
    private void onHandleScroll() {
        int currentPos = mCurrentOffset / (mItemWidth + mIntervalOfItem);

        View currentView = mRecyclerView.getLayoutManager().findViewByPosition(currentPos);
        View nextView = null;
        if (currentPos + 1 <= mRecyclerView.getAdapter().getItemCount() - 1) {
            nextView = mRecyclerView.getLayoutManager().findViewByPosition(currentPos + 1);
        }

        int currentPosOffset = mCurrentOffset - currentPos * (mItemWidth + mIntervalOfItem);
        float ratio = (1 - mScaleRatio) * currentPosOffset / (mItemWidth + mIntervalOfItem);
        if (currentView != null) currentView.setScaleY(1 - ratio);
        if (nextView != null) nextView.setScaleY(mScaleRatio + ratio);

        if (HorizontalScrollScaleGalleryHelper.DEBUG)
            Log.e(TAG, "ItemCount=" + mRecyclerView.getAdapter().getItemCount() + ",curV=null?" + (currentView == null) + ",nextV=null?" + (nextView == null));
        if (HorizontalScrollScaleGalleryHelper.DEBUG)
            Log.e(TAG, "onHandleScroll currentPos= " + currentPos + ",currentPosOffset=" + currentPosOffset + "，ratio=" + ratio + ",curView sY=" + (1 - ratio) + ",nextV sy=" + (mScaleRatio + ratio));

    }

    int getCurrentItemPos() {
        return getFirstCompletelyVisibleItemPosition();
    }
}
