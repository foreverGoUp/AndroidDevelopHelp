package ckc.android.develophelp.lib.base.mvp;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import ckc.android.develophelp.lib.base.common.LazyFragment;
import ckc.android.develophelp.lib.util.ToastUtils;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;


/**
 * 基本片段类
 * <p>
 * 实现了：
 * -子类调用父类的能力：getPresenter()
 * - brx请求和界面生命周期的同步：bindToLifecycle()
 * - 页面基本的能力：IBaseView
 * - 子类可以按需配置父类：
 * 1、页面接收和不接收表达者数据的时机
 * 2、表达者实例
 */
public abstract class BaseFragment<P extends BaseContract.IBasePresenter> extends LazyFragment implements BaseContract.IBaseView, LifecycleProvider<FragmentEvent> {

    //是否调试
    public static boolean DEBUG = false;
    //rx lifecycle
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();
    //通用表达者声明
    private P mPresenter;

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }

    @Override
    @CallSuper
    public void onAttach(android.app.Activity activity) {
        super.onAttach(activity);
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
    }

    @Override
    @CallSuper
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
    }

    @Override
    @CallSuper
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
    }

    @Override
    @CallSuper
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
    }

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
        //页面接收表达者数据
        if (onConfigPresenterAttachViewWhileOnResume()) {
            doPresenterAttachView();
        }
    }

    @Override
    @CallSuper
    public void onPause() {
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        super.onPause();
    }

    @Override
    @CallSuper
    public void onStop() {
        lifecycleSubject.onNext(FragmentEvent.STOP);
        super.onStop();
        //页面不接收表达者数据
        if (onConfigPresenterDetachViewWhileOnStop()) {
            doPresenterDetachView();
        }
    }

    @Override
    @CallSuper
    public void onDestroyView() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    @CallSuper
    public void onDestroy() {
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        super.onDestroy();
    }

    @Override
    @CallSuper
    public void onDetach() {
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        super.onDetach();
    }


    /**
     * 执行页面接收表达者数据
     */
    protected void doPresenterAttachView() {
        if (DEBUG)
            Log.e(TAG, "[DEBUG] doPresenterAttachView 执行页面接收表达者数据 " + (mPresenter != null) + " hashcode=" + hashCode());
        if (mPresenter != null) {
            mPresenter.onAttachView(this);
        }
    }

    /**
     * 执行页面不接收表达者数据
     */
    protected void doPresenterDetachView() {
        if (DEBUG)
            Log.e(TAG, "[DEBUG] doPresenterDetachView 执行页面不接收表达者数据 " + (mPresenter != null) + " hashcode=" + hashCode());
        if (mPresenter != null) {
            mPresenter.onDetachView();
        }
    }

    /**
     * 是否配置在onResume中执行表达者连接页面，默认true
     */
    protected boolean onConfigPresenterAttachViewWhileOnResume() {
        return true;
    }

    /**
     * 是否配置在OnStop中执行表达者断开页面，默认true
     */
    protected boolean onConfigPresenterDetachViewWhileOnStop() {
        return true;
    }

    /**
     * 获取表达者
     */
    protected P getPresenter() {
        return mPresenter;
    }

    /**
     * 子类配置表达者
     */
    protected abstract P onConfigPresenter();

    /**
     * 初始化
     */
    protected void OnInit(View view) {
        if (DEBUG) Log.e(TAG, "[DEBUG] onInit");
        mPresenter = onConfigPresenter();
    }

    @Override
    public void onToast(String content) {
        ToastUtils.showToast(getActivity(), content);
    }

    @Override
    public void onTitle(String text) {

    }

    @Override
    public void onLoading(boolean isLoading, String msg) {
        //显示默认加载
    }


}
