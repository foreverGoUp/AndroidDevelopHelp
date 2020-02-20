package ckc.android.develophelp.lib.base.mvp;

import android.content.Context;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.List;

/**
 * Created by peter on 2018/3/12.
 * 基本合约
 */

public interface BaseContract {

    /**
     * 基本表达者接口
     */
    interface IBasePresenter {
        /**
         * 连接界面
         *
         * @param view 界面
         */
        void onAttachView(IBaseView view);

        /**
         * 断开界面
         */
        void onDetachView();
    }

    /**
     * 基本界面接口.一般由BaseActivity和BaseFragment继承。
     */
    interface IBaseView {
        /**
         * 获得应用上下文
         */
        Context getApplicationContext();

        /**
         * 显示吐司
         *
         * @param content 吐司内容
         */
        void onToast(String content);

        /**
         * 显示标题
         *
         * @param text 显示的标题
         */
        void onTitle(String text);

        /**
         * 屏幕中间是否加载中
         *
         * @param isLoading true则显示加载中，false隐藏加载中
         * @param msg       加载具体行为，如登录中，优化中等等
         */
        void onLoading(boolean isLoading, String msg);

        /**
         * 绑定到界面的rx请求的生命周期
         */
        <T> LifecycleTransformer<T> bindToLifecycle();
    }

    /**
     * 基本登录界面接口
     * <p>
     * 用于登录界面或包含登录功能的界面使用
     */
    interface IBaseLoginView extends IBaseView {
        /**
         * 登录结果
         *
         * @param scs     是否成功
         * @param failMsg 失败消息
         */
        void onLoginResult(boolean scs, String failMsg);
    }

    /**
     * 基本列表界面接口
     * <p>
     * 用于列表界面使用
     */
    interface IBaseListView<ENTITY> extends IBaseView {

        /**
         * 刷新完成。当data数据为空时，表示无数据。
         *
         * @param data 列表数据
         */
        void onRefreshComplete(List<ENTITY> data);

        /**
         * 加载完成.当data数据为空时，表示无数据。
         *
         * @param data 列表数据
         */
        void onLoadMoreComplete(List<ENTITY> data);

        /**
         * 刷新失败
         */
        void onRefreshFail();

        /**
         * 加载更多失败
         */
        void onLoadMoreFail();
    }

}
