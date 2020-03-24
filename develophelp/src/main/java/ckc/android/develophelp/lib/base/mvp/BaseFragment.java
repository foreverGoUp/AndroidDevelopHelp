package ckc.android.develophelp.lib.base.mvp;

import android.support.annotation.CallSuper;
import android.util.Log;
import android.view.View;

import ckc.android.develophelp.lib.base.common.LazyFragment;
import ckc.android.develophelp.lib.util.ToastUtils;


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
public abstract class BaseFragment<P extends BaseContract.IBasePresenter> extends LazyFragment implements BaseContract.IBaseView {

    //是否调试
    public static boolean DEBUG = false;
    //通用表达者声明
    private P mPresenter;

    @Override
    @CallSuper
    public void onResume() {
        super.onResume();
        //页面接收表达者数据
        if (onConfigPresenterAttachViewWhileOnResume()) {
            doPresenterAttachView();
        }
    }

    @Override
    @CallSuper
    public void onStop() {
        super.onStop();
        //页面不接收表达者数据
        if (onConfigPresenterDetachViewWhileOnStop()) {
            doPresenterDetachView();
        }
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
