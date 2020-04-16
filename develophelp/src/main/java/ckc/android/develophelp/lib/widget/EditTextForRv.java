package ckc.android.develophelp.lib.widget;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 20191218 ckc
 * 完美解决recyclerView嵌套EditText，当EditText内容滚动与rv的滚动垂直方向上的冲突。
 */
public class EditTextForRv extends AppCompatEditText {

    private final String TAG = this.getClass().getSimpleName();
    private int mLatestScrollY = 0;
    private int mLastMoveEventY;

    public EditTextForRv(Context context) {
        super(context);
        init();
    }

    public EditTextForRv(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextForRv(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.e(TAG, "setOnScrollChangeListener>>>>>>>>>>>>>>>>>>>>>>>>");
            setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    Log.e(TAG, "onScrollChange scrollX=" + scrollX + ",scrollY=" + scrollY + ",oldScrollX=" + oldScrollX + ",oldScrollY=" + oldScrollY + ",contentH=" + (getLineCount() * getLineHeight()) + ",visibleH=" + (getHeight() - getPaddingTop() - getPaddingBottom() - 8));

                    mLatestScrollY = scrollY;
                }
            });
        }
    }

    private int getMaxScrollY() {
        int contentH = getLineCount() * getLineHeight();
        int visibleH = getHeight() - getPaddingTop() - getPaddingBottom() - 8;
        return contentH - visibleH;
    }

    /**
     * 所以触摸事件回调该方法和onTouchEvent方法
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "dispatchTouchEvent>>>>>>>>>>>>>>>>>>>>>>>>ACTION_DOWN,y=" + event.getY() + ",rawY=" + event.getRawY());
                getParent().requestDisallowInterceptTouchEvent(true);
                mLastMoveEventY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "dispatchTouchEvent>>>>>>>>>>>>>>>>>>>>>>>>ACTION_MOVE,y=" + event.getY() + ",rawY=" + event.getRawY());
//                if (mLatestMoveActionScrollY == mLatestScrollY){//输入框已经拉到最顶部或底部了
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                    return false;
//                }else {
//                    mLatestMoveActionScrollY = mLatestScrollY;
//                }
                /**
                 * 1、内容高度还没超过可见高度
                 * 2、内容高度超过可见高度后，滑动到最顶部，继续向下滑动
                 * 3、内容高度超过可见高度后，滑动到最底部，继续向上滑动
                 * */
                if (getMaxScrollY() < 0 || (getScrollY() == 0 && event.getY() > mLastMoveEventY) || (getScrollY() == getMaxScrollY() && event.getY() < mLastMoveEventY)) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                mLastMoveEventY = (int) event.getY();

                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "dispatchTouchEvent>>>>>>>>>>>>>>>>>>>>>>>>ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent>>>>>>>>>>>>>>>>>>>>>>>>ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "onTouchEvent>>>>>>>>>>>>>>>>>>>>>>>>ACTION_MOVE");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, "onTouchEvent>>>>>>>>>>>>>>>>>>>>>>>>ACTION_UP");
                break;
        }
//        if (event.getAction() == MotionEvent.ACTION_DOWN){
//            return true;
//        }
        return super.onTouchEvent(event);
    }
}
