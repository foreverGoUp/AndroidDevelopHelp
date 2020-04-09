package ckc.android.develophelp.lib.base.common;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 懒加载片段
 * created by ckc on 20191130
 * successfulpeter@163.com
 *
 * 建议当片段通过viewpager adapter展示时使用懒加载方式。fm方式也可以模拟使用此懒加载方式。
 * 参考代码：
 * private void switchFragment(int pos, boolean init){
 *         FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
 *         if (!init){
 *             if (mCurrentTabPos != -1){
 *                 Fragment hideFragment = getFragment(mCurrentTabPos);
 *                 fragmentTransaction.hide(hideFragment);
 * //            hideFragment.setUserVisibleHint(false);//模拟片段交给vp adapter显示以致可以使用片段的懒加载方法
 *             }
 *         }
 *
 *         mCurrentTabPos = pos;
 *         Fragment showFragment = getFragment(pos);
 * //        showFragment.setUserVisibleHint(true);//模拟片段交给vp adapter显示以致可以使用片段的懒加载方法
 *         if (!showFragment.isAdded()) {
 *             fragmentTransaction.add(R.id.fragment_container, showFragment, mFragmentTags[pos]);
 *         }
 *         fragmentTransaction.show(showFragment).commitAllowingStateLoss();
 *     }
 *
 * 懒加载相关方法
 * onActivityCreated、setUserVisibleHint、onLazyLoad、onUserVisibleAfterLazyLoad、onResumeWithUserVisibleAfterLazyLoad
 *
 * 解决片段回收对懒加载的影响。
 */
public abstract class LazyFragment extends RootFragment {

    public static boolean DEBUG = false;
    private boolean mIsPreparedForLazyLoad = false;//是否为懒加载的视图准备好了
    private boolean mIsLazyLoaded = false;//是否已经懒加载

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.e(TAG, hashCode() + " onCreate >>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (DEBUG) Log.e(TAG, hashCode() + " onAttach >>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (DEBUG) Log.e(TAG, hashCode() + " onStart >>>>>>>>>>>>>>>>>>>>>>");
    }

    /**
     * 经常被回调
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (DEBUG)
            Log.e(TAG, hashCode() + " setUserVisibleHint >>>>>>>>>>>" + isVisibleToUser + ">>>>>>>>>>>");
        //当片段被回收了，会完整执行以下生命周期
        if (getView() == null) {
            mIsLazyLoaded = false;
            mIsPreparedForLazyLoad = false;
        }
        if (isVisibleToUser && mIsLazyLoaded) {
            onUserVisibleAfterLazyLoad();
        }
        //懒加载
        if (isVisibleToUser && mIsPreparedForLazyLoad && !mIsLazyLoaded) {
            mIsLazyLoaded = true;
            onLazyLoad();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (DEBUG)
            Log.e(TAG, hashCode() + " onCreateView >>>>>>>>>>>>>>>>>>>>>>");
        if (super.mViewSoftReference.get() != null) {
            mIsLazyLoaded = true;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (DEBUG) Log.e(TAG, hashCode() + " onViewCreated >>>>>>>>>>>>>>>>>>>>>>");
    }

    /**
     * 在onCreateView执行完后被回调
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (DEBUG) Log.e(TAG, hashCode() + " onActivityCreated >>>>>>>>>>>>>>>>>>>>>>");
        mIsPreparedForLazyLoad = true;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (DEBUG) Log.e(TAG, hashCode() + " onViewStateRestored >>>>>>>>>>>>>>>>>>>>>>");

    }


    ///////////////////////////////////////////////////////////////////////////
    // 懒加载回调
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 当片段通过viewpager adapter展示时。
     * <p>
     * 开始懒加载
     * 子类重写改方法执行第一次加载数据
     */
    protected void onLazyLoad() {
        if (DEBUG) Log.e(TAG, hashCode() + " onLazyLoad -------------------------");
    }

    /**
     * 当片段通过viewpager adapter展示时。
     * 懒加载之后，业务若需要片段每次可见时刷新数据则重写该方法执行启动刷新
     */
    protected void onUserVisibleAfterLazyLoad() {
        if (DEBUG)
            Log.e(TAG, hashCode() + " onUserVisibleAfterLazyLoad -------------------------");
    }

    /**
     * 当片段通过viewpager adapter展示时。
     * 懒加载之后，业务若需要片段每次活动从后台返回前台并处于可见时刷新数据则重写该方法执行启动刷新
     * ，该方法与onUserVisibleAfterLazyLoad和onLazyLoad不会被同时回调。
     */
    protected void onResumeWithUserVisibleAfterLazyLoad() {
        if (DEBUG)
            Log.e(TAG, hashCode() + " onResumeWithUserVisibleAfterLazyLoad -------------------------");
    }

    ///////////////////////////////////////////////////////////////////////////
    // 懒加载回调end
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 当片段通过viewpager adapter展示时。
     * 若
     */
    @Override
    public void onResume() {
        super.onResume();
        if (DEBUG) Log.e(TAG, hashCode() + " onResume >>>>>>>>>>>>>>>>>>>>>>");
        if (getUserVisibleHint()) {
            if (!mIsLazyLoaded) {
                mIsLazyLoaded = true;
                onLazyLoad();
            } else {
                onResumeWithUserVisibleAfterLazyLoad();
            }
        }
    }

    /**
     * 活动从前台切换到后台会回调该方法
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (DEBUG) Log.e(TAG, hashCode() + " onSaveInstanceState >>>>>>>>>>>>>>>>>>>>>>");

    }

    @Override
    public void onPause() {
        super.onPause();
        if (DEBUG) Log.e(TAG, hashCode() + " onPause >>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (DEBUG) Log.e(TAG, hashCode() + " onStop >>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (DEBUG) Log.e(TAG, hashCode() + " onDestroyView >>>>>>>>>>>>>>>>>>>>>>");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (DEBUG) Log.e(TAG, hashCode() + " onDetach >>>>>>>>>>>>>>>>>>>>>>");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG) Log.e(TAG, hashCode() + " onDestroy >>>>>>>>>>>>>>>>>>>>>>");

    }
}
