package ckc.android.develophelp.lib.base.mvp;

import android.content.Context;

/**
 * Created by peter on 2018/3/10.
 * 基本表达者类
 * <p>
 * 实现了
 * - 子类调用父类的能力：getContext、getView
 * - 实现了基本表达者能力：IBasePresenter
 */
public class BasePresenter<V extends BaseContract.IBaseView> implements BaseContract.IBasePresenter {

    protected final String TAG = this.getClass().getSimpleName();
    //应用上下文
    private Context mContext = null;
    //页面
    private V mView = null;

    @Override
    public void onAttachView(BaseContract.IBaseView view) {
        mView = (V) view;
        mContext = view.getApplicationContext();
    }

    @Override
    public void onDetachView() {
        mView = null;
        mContext = null;
    }

    /**
     * 是否连接了页面
     */
    protected boolean isViewAttached() {
        return mView != null;
    }

    /**
     * 获得页面
     */
    protected V getView() {
        return mView;
    }

    /**
     * 获得应用上下文
     */
    protected Context getContext() {
        return mContext;
    }
}
