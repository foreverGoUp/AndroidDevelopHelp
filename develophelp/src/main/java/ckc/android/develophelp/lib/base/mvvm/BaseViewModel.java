package ckc.android.develophelp.lib.base.mvvm;

/**
 * created by ckc on 19-10-19
 * successfulpeter@163.com
 * 基本的界面vm
 * */
public class BaseViewModel<NAVI extends IBaseViewNavigator> {

    //界面导航器
    protected NAVI mViewNavigator;

    public void onAttachView(NAVI view) {
        mViewNavigator = view;
    }

    public void onDetachView() {
        mViewNavigator = null;
    }

    protected boolean isAttachedView() {
        return mViewNavigator != null;
    }
}
