package ckc.android.develophelp.lib.util;


import android.support.v7.app.AppCompatActivity;

import java.util.Stack;

public class ActivityCollector {

    private static final String TAG = "ActivityCollector";
    private Stack<AppCompatActivity> mActivityStack = new Stack<AppCompatActivity>();
    private static ActivityCollector INSTANCE;

    private ActivityCollector() {
    }

    public static ActivityCollector getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ActivityCollector();
        }
        return INSTANCE;
    }

    //写在activity的onDestory中
    public void removeActivity(AppCompatActivity activity) {
        if (activity != null) {
            if (mActivityStack.contains(activity)) {
                mActivityStack.remove(activity);
            }
        }
    }

    //写在activity的onCreate中
    public void addActivity(AppCompatActivity activity) {
        if (activity != null) {
            mActivityStack.add(activity);
        }
    }

    public void finishActivity(AppCompatActivity activity) {
        if (activity != null) {
            Log.e(TAG, ">>>>>>>>>>>>>>>finishActivity: 结束活动---" + activity.getClass().getSimpleName());
            activity.finish();
            mActivityStack.remove(activity);
        }
    }

    public void finishActivity(Class clazz) {
        for (int i = 0; i < mActivityStack.size(); i++) {
            if (mActivityStack.get(i).getClass() == clazz) {
                mActivityStack.get(i).finish();
                Log.d("sdsd", "清理了活动：" + clazz.getSimpleName());
            }
        }
    }

    public AppCompatActivity currentActivity() {
        AppCompatActivity activity = null;
        if (!mActivityStack.empty()) {
            activity = mActivityStack.lastElement();
        }

        return activity;
    }

    public void finishAll() {
        while (!mActivityStack.empty()) {
            AppCompatActivity activity = currentActivity();
            finishActivity(activity);
            Log.e(TAG, "结束活动：" + activity.getClass().getSimpleName());
        }
    }

    public void finishAllExcept(Class clazz) {
        while (!mActivityStack.empty()) {
            AppCompatActivity activity = mActivityStack.firstElement();
            if (activity.getClass() == clazz) {
                Log.e(TAG, "!!!finishAllExcept: 存在排除的活动---" + clazz.getSimpleName());
                if (mActivityStack.indexOf(activity) == mActivityStack.size() - 1) {//处在队列尾巴
                    Log.e(TAG, "!!!finishAllExcept: 处在队列尾巴");
                    break;
                } else {
                    Log.e(TAG, "!!!finishAllExcept: 不处在队列尾巴");
                    mActivityStack.remove(activity);
                    mActivityStack.add(activity);
                }
            }
            finishActivity(activity);
        }
    }

    public boolean isOnly(Class class1) {
        boolean ret = false;
        for (AppCompatActivity aty : mActivityStack) {
            if (aty.getClass() != class1) {
                Log.e(TAG, "isOnly: 存在其他活动---" + aty.getClass().getSimpleName());
                return false;
            } else {
                Log.e(TAG, "isOnly: 存在活动---" + class1.getSimpleName());
                ret = true;
            }
        }
        return ret;
    }

    public boolean isFirstIndex(Class class1) {
        for (AppCompatActivity aty : mActivityStack) {
            Log.e(TAG, "isFirstIndex: " + aty.getClass().getSimpleName() + "-->" + mActivityStack.indexOf(aty));
            if (aty.getClass() == class1) {
                if (mActivityStack.indexOf(aty) == 0) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public AppCompatActivity getTopActivity() {
        int size = mActivityStack.size();
        if (size > 0) {
            return mActivityStack.get(size - 1);
        }
        return null;
    }

}
