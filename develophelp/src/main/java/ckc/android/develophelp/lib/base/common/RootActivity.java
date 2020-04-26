package ckc.android.develophelp.lib.base.common;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import ckc.android.develophelp.lib.immersive.StatusBarUtil;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * 根活动
 * created by ckc on 2019-12-01
 * successfulpeter@163.com
 */
public abstract class RootActivity extends AppCompatActivity implements LifecycleProvider<ActivityEvent> {

    protected final String TAG = this.getClass().getSimpleName();
    //是否调试
    public static boolean DEBUG = false;
    //rx lifecycle
    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        int statusBarColor = onConfigStatusBarBackgroundColor();
        if (statusBarColor != Color.TRANSPARENT)
            StatusBarUtil.setStatusBarColor(this, statusBarColor);

        super.onCreate(savedInstanceState);
        if (DEBUG) Log.e(TAG, "[DEBUG] onCreate");

        //rx lifecycle
        lifecycleSubject.onNext(ActivityEvent.CREATE);

        //如果不是子类设置内容视图，则交给我来设置
        if (!onConfigSubActivitySetContentView()) {
            setContentView(onConfigContentViewLayout());
        }

        //状态栏
        changeStatusBar();

        //初始化
        onInit(savedInstanceState);
    }

    private void changeStatusBar(){
        if (onConfigRootViewFitsSystemWindows()){
            //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
            StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        }
        if (onConfigStatusBarTransparent()){
            //设置状态栏透明
            StatusBarUtil.setTranslucentStatus(this);
        }
        if (onConfigStatusBarDarkTheme()){
            //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
            //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
            if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
                //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
                //这样半透明+白=灰, 状态栏的文字能看得清
                StatusBarUtil.setStatusBarColor(this,0x55000000);
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (DEBUG) Log.e(TAG, "[DEBUG] onRestoreInstanceState(Bundle)");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        if (DEBUG) Log.e(TAG, "[DEBUG] onRestoreInstanceState(Bundle,PersistableBundle)");

    }

    @Override
    @CallSuper
    protected void onStart() {
        super.onStart();
        if (DEBUG) Log.e(TAG, "[DEBUG] onStart");
        //rx lifecycle
        lifecycleSubject.onNext(ActivityEvent.START);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) Log.e(TAG, "[DEBUG] onResume");
        //rx lifecycle
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (DEBUG) Log.e(TAG, "[DEBUG] onPause");
        //rx lifecycle
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (DEBUG) Log.e(TAG, "[DEBUG] onStop");
        //rx lifecycle
        lifecycleSubject.onNext(ActivityEvent.STOP);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if (DEBUG) Log.e(TAG, "[DEBUG] onSaveInstanceState(Bundle,PersistableBundle)");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (DEBUG) Log.e(TAG, "[DEBUG] onSaveInstanceState(Bundle)");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (DEBUG) Log.e(TAG, "[DEBUG] onDestroy");
        //rx lifecycle
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
    }

    /**
     * 配置是否让子页面设置内容视图
     */
    protected abstract boolean onConfigSubActivitySetContentView();

    /**
     * 配置界面内容布局
     */
    protected abstract int onConfigContentViewLayout();

    /**
     * 配置根view适应系统窗口
     * 注意：mvvm布局文件可能需要手动在布局文件中将根布局设置fitsSystemWindows = true
     * */
    protected abstract boolean onConfigRootViewFitsSystemWindows();
    /**
     * 配置状态栏透明
     * */
    protected abstract boolean onConfigStatusBarTransparent();
    /**
     * 配置状态栏黑色主题
     * */
    protected abstract boolean onConfigStatusBarDarkTheme();

    /**
     * 配置状态栏背景色
     * 默认返回透明值，但不设置状态栏背景为透明。若需要配置状态栏背景为透明则重写onConfigStatusBarTransparent()
     */
    protected int onConfigStatusBarBackgroundColor() {
        return Color.TRANSPARENT;
    }



    /**
     * 界面初始化
     */
    protected abstract void onInit(Bundle savedInstanceState);

    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

}
