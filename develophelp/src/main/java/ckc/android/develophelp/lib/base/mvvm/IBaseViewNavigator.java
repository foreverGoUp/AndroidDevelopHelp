package ckc.android.develophelp.lib.base.mvvm;

import android.support.annotation.NonNull;

/**
 * created by ckc on 19-10-19
 * successfulpeter@163.com
 *
 * 基本界面导航器接口
 * */
public interface IBaseViewNavigator {

    /**
     * 吐司
     */
    void onToast(@NonNull String text);

    /**
     * 吐司
     */
    void onToast(int resId);
}
