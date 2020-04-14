package ckc.android.develophelp.lib.widget.horizontalScrollScaleGallery;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ckc on 200224.
 * <p>
 * 功能：
 * -条目竖直方向内容从屏幕左边或右边移动到屏幕中间会逐渐放大，当条目完全到达屏幕中间时为原尺寸;
 * 条目竖直方向内容从屏幕中间移动到屏幕左边或右边会逐渐缩小，当条目完全到达屏幕左边或右边时为最小缩放尺寸。
 * -个性化配置
 * -高性能快速跳转到指定位置的方法scrollToPosition()
 * -获取当前显示位置的方法getCurrentItemPos()
 * <p>
 * <p>
 * 用法：
 * -实例化时传入相应的个性化配置
 * -在RecyclerView的adapter中对应方法中调用onCreateViewHolder, onBindViewHolder
 * 如：private class HorizontalScrollScaleGalleryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
 * <p>
 * HorizontalScrollScaleGalleryHelper mHelper;
 * <p>
 * public HorizontalScrollScaleGalleryAdapter(HorizontalScrollScaleGalleryHelper helper, @Nullable List<String> data) {
 * super(R.layout.item_horizontal_scroll_scale_gallery, data);
 * mHelper = helper;
 * }
 *
 * @NonNull
 * @Override public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
 * BaseViewHolder baseViewHolder = super.onCreateViewHolder(parent, viewType);
 * mHelper.onCreateViewHolder(parent, baseViewHolder.itemView, baseViewHolder.getLayoutPosition());
 * return baseViewHolder;
 * }
 * @Override public void onBindViewHolder(BaseViewHolder holder, int position) {
 * super.onBindViewHolder(holder, position);
 * mHelper.onBindViewHolder(holder.itemView, position, getItemCount());
 * }
 * @Override protected void convert(@NonNull BaseViewHolder helper, String item) {
 * helper.setText(R.id.tv_content, item);
 * }
 * }
 * -使用该类中提供的scrollToPosition方法高性能跳转到指定条目。若想要缓慢滚动到指定位置，可直接使用RecyclerView的方法。
 */
public class HorizontalScrollScaleGalleryHelper {

    public static boolean DEBUG = false;
    private final String TAG = this.getClass().getSimpleName();

    RecyclerView mRecyclerView;
    int mIntervalOfItemToScreen;//条CsmColumnProgress.java目两边距离屏幕的大小
    int mIntervalOfItem;//条目之间的间隔
    float mScaleRatio;//最小条目Y方向的缩放值
    int mItemWidth; // 条目宽度

    private ScaleHelper mScaleHelper = new ScaleHelper();
    private boolean mAttachRv;
//    private boolean mIsAddedOnScrollListener = false;

    public HorizontalScrollScaleGalleryHelper(RecyclerView recyclerView, int currentItemPos, int intervalOfItemToScreen, int intervalOfItem, float scaleRatio) {
        mRecyclerView = recyclerView;
        mIntervalOfItemToScreen = intervalOfItemToScreen;
        mIntervalOfItem = intervalOfItem;
        mScaleRatio = scaleRatio;

        scrollToPosition(currentItemPos);
    }

    /**
     * 即完美达到理想效果，又达到最大性能（路径上的item的onBindViewHolder方法不会被调用，避免了不必要的数据加载）
     */
    public void scrollToPosition(final int pos) {
        if (pos < 0) return;
        //等效于mRecyclerView.scrollToPosition(mCurrentItemPos);
//        mRecyclerView.getLayoutManager().scrollToPosition(mCurrentItemPos);
        //该方法可以直达指定item,提高了性能.但无法对齐屏幕中心。
        mRecyclerView.scrollToPosition(pos);

        //该方法滚动过程中会多次调用每个经过的item的onBindViewHolder和滚动监听的回调，造成不必要的资源浪费。
//        mRecyclerView.smoothScrollToPosition(mCurrentItemPos);

        //将会在RecyclerView默认显示都初始化后执行该任务
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                int itemCount = mRecyclerView.getAdapter().getItemCount();
                if (itemCount == 0 || pos >= itemCount) return;
                if (DEBUG)
                    Log.e(TAG, "HorizontalScrollScaleGalleryHelper mRecyclerView post mItemWidth=" + mItemWidth);
                //该方法滚动过程中会多次调用每个经过的item的onBindViewHolder，造成不必要的资源浪费。
//                    mRecyclerView.scrollBy(mCurrentItemPos * (mItemWidth+mIntervalOfItem), 0);
                //该方案虽然首次能直达目标位置，但是滑动后会出现其他item无法对齐屏幕中心
//                int dx = mIntervalOfItemToScreen - mIntervalOfItem/2;
//                mScaleHelper.mCurrentOffset = pos * (mItemWidth+mIntervalOfItem) + dx;
//                mRecyclerView.scrollBy(-dx, 0);
                //触发LinearSnapHelper将条目移至屏幕中心上。该方案item最后恢复缩放的过程太明显，有明显的感知。
//                mScaleHelper.pauseScale();
//                Log.e(TAG, "-----第1次校准：条目对齐屏幕中心");
//                if (!mIsAddedOnScrollListener){
//                    mIsAddedOnScrollListener = true;
//                    mRecyclerView.addOnScrollListener(mOnScrollListener);
//                }
//                mRecyclerView.smoothScrollBy(1, 0);
//                mRecyclerView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e(TAG, "-----第2次校准:恢复滑动缩放显示");
//                        mScaleHelper.resumeScale(pos);
//                    }
//                }, 1000);
                //最佳方案：跳到指定条目后，先设置缩放后调整位置，最后滚动停止后重新确定当前偏移值。
                //设置缩放
                View view = mRecyclerView.getLayoutManager().findViewByPosition(pos - 1);
                if (view != null) {
                    view.setScaleY(mScaleRatio);
                }
                view = mRecyclerView.getLayoutManager().findViewByPosition(pos);
                if (view != null) {
                    view.setScaleY(1);
                }
                view = mRecyclerView.getLayoutManager().findViewByPosition(pos + 1);
                if (view != null) {
                    view.setScaleY(mScaleRatio);
                }
                //调整位置
                mRecyclerView.smoothScrollBy(1, 0);
            }
        });
    }

//    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//            super.onScrollStateChanged(recyclerView, newState);
//            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                Log.e(TAG, "监听到校准滚动结束");
//            } else {
//                Log.e(TAG, "监听到校准滚动中：" + newState);
//            }
//        }
//
//        @Override
//        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//            super.onScrolled(recyclerView, dx, dy);
//            Log.e(TAG, "校准滚动：dx=" + dx);
//        }
//    };

    /**
     * @param pos 值为-1
     */
    public void onCreateViewHolder(ViewGroup parent, View itemView, int pos) {
        if (DEBUG) Log.e(TAG, "onCreateViewHolder pos=" + pos);

        //设置缩放辅助
        if (!mAttachRv) {
            mAttachRv = true;

            mRecyclerView = (RecyclerView) parent;
            mItemWidth = mRecyclerView.getWidth() - 2 * mIntervalOfItemToScreen;

            mScaleHelper.init(this);
        }

        //对新条目设置宽度
        setItemWidth(itemView);
        //对新条目设置最小尺寸
        if (itemView != null) {
            itemView.setScaleY(mScaleRatio);
        }
    }

    private void setItemWidth(View itemView) {
        int itemWidth = mItemWidth;

        ViewGroup.LayoutParams lp = itemView.getLayoutParams();
        if (lp.width != itemWidth) {
            lp.width = itemWidth;
            itemView.setLayoutParams(lp);
        }
    }

    public void onBindViewHolder(View itemView, final int position, int itemCount) {
        if (DEBUG) Log.e(TAG, "onBindViewHolder pos=" + position);
        //设置item margin,仅第一个和最后一个
        int leftMarin = position == 0 ? mIntervalOfItemToScreen : mIntervalOfItem / 2;
        int rightMarin = position == itemCount - 1 ? mIntervalOfItemToScreen : mIntervalOfItem / 2;
        setViewMargin(itemView, leftMarin, 0, rightMarin, 0);
    }

    private void setViewMargin(View view, int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp.leftMargin != left || lp.topMargin != top || lp.rightMargin != right || lp.bottomMargin != bottom) {
            lp.setMargins(left, top, right, bottom);
            view.setLayoutParams(lp);
        }
    }

    /**
     * 某些情况下会返回-1，例如列表还在滑动状态。
     */
    public int getCurrentItemPos() {
        return mScaleHelper.getCurrentItemPos();
    }
}
