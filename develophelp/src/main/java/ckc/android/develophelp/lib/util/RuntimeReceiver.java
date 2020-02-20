package ckc.android.develophelp.lib.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

public class RuntimeReceiver extends BroadcastReceiver {

    private static final String TAG = "RuntimeReceiver";
    private final String SYSTEM_REASON = "reason";
    private final String SYSTEM_HOME_KEY = "homekey";
    private final String SYSTEM_HOME_KEY_LONG = "recentapps";
    public static boolean sNetWorkValid = true;
    public static boolean sScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e(TAG, "-------------接收到广播---------------");
        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            sScreenOn = false;
            Log.e(TAG, "ACTION_SCREEN_OFF");
            // stopService(new Intent(context,
            // SendCommandService.class));
        } else if (Intent.ACTION_SCREEN_ON.equals(action) || Intent.ACTION_USER_PRESENT.equals(action)) {
            if (!sScreenOn) {
                sScreenOn = true;
//                SubscriberManager.getInstance().notifyDataArrived(MsgConstants.CODE_CLIENT, MsgConstants.ACT_CLIENT_SCREEN_ON, null);
                Log.e(TAG, "ACTION_USER_PRESENT");
            }
        } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent
                .getAction())) {
//            Log.e(TAG, "ACTION_CLOSE_SYSTEM_DIALOGS");
        } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            State wifiState = null;
            State mobileState = null;
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
                mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
            }
            if (wifiState != null && State.CONNECTED == wifiState
                    || mobileState != null && State.CONNECTED == mobileState) {
                // 网络连接成功
                if (!sNetWorkValid) {
                    sNetWorkValid = true;
//                    SubscriberManager.getInstance().notifyDataArrived(MsgConstants.CODE_CLIENT, MsgConstants.ACT_CLIENT_NET_VALID, null);
                }
            } else {
                // 无网络连接
                if (sNetWorkValid) {
                    sNetWorkValid = false;
                }
            }
        } else if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            String reason = intent.getStringExtra(SYSTEM_REASON);
            if (reason != null && reason.equals(SYSTEM_HOME_KEY)) {//表示按了home键,程序到了后台

            }
//            else if(TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)){
//                //表示长按home键,显示最近使用的程序列表
//            }
        }
        //安装apK
//        else if (action.equals(Constants.ACTION_INSTALL_APK)){
//            UpdateSingleton.getInstance().startUpdate();
//        }
    }

}
