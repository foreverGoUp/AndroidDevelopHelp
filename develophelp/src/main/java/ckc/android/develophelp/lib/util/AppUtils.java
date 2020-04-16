package ckc.android.develophelp.lib.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AppUtils {

    private static final String TAG = "AppUtils";
    public static final String LANGUAGE_ZH_CN = "zh_ch";
    //    public static final String LANGUAGE_ZH_TW = "zh_tw";
    public static final String LANGUAGE_EN_US = "en_us";


    /**
     * 判断安装包是否有效，无效则删除
     *
     * @param apkPath   文件路径
     * @param apkLength 文件理论大小
     * @return 是否有效
     */
    public static boolean isApkValid(String apkPath, long apkLength) {
        if (!FileUtil.existFile(apkPath)) {
            return false;
        }
        double localApkLen = FileSizeUtil.getFileOrFilesSize(apkPath, FileSizeUtil.SIZETYPE_B);
        Log.e(TAG, "isApkValid: localApkLen=" + localApkLen + ",remote=" + apkLength);
        boolean isValid = localApkLen == apkLength;
        if (!isValid) {
            Log.e(TAG, ">>>>>>>>>>>>删除无效安装成功？: " + FileUtil.deleteFile(apkPath));
        }
        return isValid;
    }

    /**
     * 安装apk
     *
     * @param
     */
    public static void installApk(Context context, String apkPath, @NonNull String authority) {
        File file = new File(apkPath);
        if (!file.exists()) {
            Log.e(TAG, "!!!!!!!!!!!!!!!!!!!!!install fail, apk not exist");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "包名.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(context, authority, file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void enableEditText(Context context, EditText editText) {
        if (context == null || editText == null) {
            return;
        }
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        showKeyBoard(context, editText);
    }

    /**
     * 显示键盘
     *
     * @see
     * @since V1.8.2
     */
    public static void showKeyBoard(Context context, EditText editText) {
        if (context == null || editText == null) {
            return;
        }
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘
     *
     * @see
     * @since V1.8.2
     */
    public static void hideKeyBoard(Context context, EditText editText) {
        if (context == null || editText == null) {
            return;
        }
        // 键盘隐藏是个坑
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        // 隐藏键盘，最坑的就是隐藏键盘
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager im = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(activity.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获得包名
     * 获得版本名称
     */
    public static String[] getAppInfo(Context context) {
        String[] arr = new String[2];
        try {
            arr[0] = context.getPackageName();
            arr[1] = context.getPackageManager().getPackageInfo(
                    arr[0], 0).versionName;
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得版本名称
     */
    public static String getVersionName(Context context) {
        String packageName = context.getPackageName();
        try {
            String vn = context.getPackageManager().getPackageInfo(
                    packageName, 0).versionName;
            return vn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得版本码
     */
    public static Integer getVersionCode(Context context) {
        String packageName = context.getPackageName();
        try {
            return context.getPackageManager().getPackageInfo(
                    packageName, 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * app类初始化应用语言
     * private static void initAppLanguage(Context context) {
     * String userSetLang = UserTool.getUserSetLanguageTag();
     * if (userSetLang == null) {
     * Log.e(TAG, "正在设置默认语言。");
     * AppUtils.changeLanguage(context, AppUtils.getDefaultLanguage());
     * } else {
     * AppUtils.changeLanguage(context, userSetLang);
     * }
     * }
     */
    public static boolean changeLanguage(Context context, String languageTag) {
        Locale locale = null;
        switch (languageTag) {
            case LANGUAGE_ZH_CN:
                locale = Locale.SIMPLIFIED_CHINESE;
                break;
            case LANGUAGE_EN_US:
                locale = Locale.US;
                break;
            default:
                Locale.getDefault();
                break;
        }
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        Log.e(TAG, "当前语言环境：" + config.locale.getCountry() + "," + config.locale.getLanguage() + "," + config.locale.getDisplayCountry() + "," + config.locale.getDisplayLanguage());
        if (config.locale == locale) {
            Log.e(TAG, "重复设置app语言");
            return false;
        } else {
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }

        return true;
    }

    /**
     * 只支持简体中文和美国英语
     * <p/>
     * 当默认地区为中国和美国之外的国家，默认语言为美国英语
     */
    public static String getDefaultLanguage() {
        String languageType = LANGUAGE_EN_US;

        Locale locale = Locale.getDefault();
        String lang = locale.getLanguage();
        if (lang.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())) {
            languageType = LANGUAGE_ZH_CN;
        }
        return languageType;
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    public static void setFullScreen(Activity activity, boolean hideStateBar, boolean hideVirtualKey) {
        View decorView = activity.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            decorView.setSystemUiVisibility(hideVirtualKey ? View.GONE : View.VISIBLE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            int uiOptions;
            if (hideStateBar && hideVirtualKey) {
                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            } else if (hideStateBar) {
                uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            } else if (hideVirtualKey) {
                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
            } else {
                uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            }
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 隐藏虚拟按键和状态栏
     */
    public static void setFullScreen(Activity activity, boolean enable) {
        setFullScreen(activity, enable, enable);

//        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
//            View v = activity.getWindow().getDecorView();
//            v.setSystemUiVisibility(enable ? View.GONE : View.VISIBLE);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            //for new api versions.
//            View decorView = activity.getWindow().getDecorView();
//            int uiOptions;
//            if (enable) {
//                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_FULLSCREEN;
//            } else {
//                uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
//            }
//            decorView.setSystemUiVisibility(uiOptions);
//        }
    }

    /**
     * 是否隐藏状态栏
     */
//    public static void setFullScreen(Activity activity, boolean hideStateBar) {
//        Window window = activity.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        if (hideStateBar) {
//            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//        } else {
//            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
//        window.setAttributes(lp);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//    }
    public static void startActivity(FragmentActivity activity, Class clazz, boolean finish) {
        activity.startActivity(new Intent(activity, clazz));
        if (finish) activity.finish();
    }

    /**
     * 用广播方式发送数据给其他App
     *
     * @param action 想要其他app干什么，app之间自定义
     * @param dstAppId 目标app的应用ID，如com.xxx.ad
     * @param dstReceiverPackageName 目标接收器完整包名，如com.xxx.tool.BootCompleteReceiver
     */
    public static void sendDataToOtherAppByBroadcast(Context context, String action, String dstAppId, String dstReceiverPackageName, Bundle data) {
        Intent intent = new Intent(action);
        ComponentName componentName = new ComponentName(dstAppId, dstReceiverPackageName);
        intent.setComponent(componentName);
        if (data != null) intent.putExtras(data);
        context.sendBroadcast(intent);
    }
}
