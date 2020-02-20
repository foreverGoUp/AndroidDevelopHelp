package ckc.android.develophelp.lib.util;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.Vibrator;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/6/22 0022.
 * 存放android设备相关的工具方法
 */
public class DeviceUtil {

    private static final String TAG = DeviceUtil.class.getSimpleName();
    private static PowerManager.WakeLock sWakeLock;
    private static KeyguardManager.KeyguardLock sKeyguardLock;

    public static void setCurrentVolume(Context context, int volume) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (manager == null) {
            Log.e(TAG, "!!!!!!!!!!!!!!setCurrentVolume: AudioManager == null");
            return;
        }
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    public static int getCurrentVolume(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (manager == null) {
            Log.e(TAG, "!!!!!!!!!!!!!!getCurrentVolume: AudioManager == null");
            return -1;
        }
//        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>getCurrentVolume: "+manager.getStreamVolume(AudioManager.STREAM_MUSIC));
        return manager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static int getMaxVolume(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (manager == null) {
            Log.e(TAG, "!!!!!!!!!!!!!!getMaxVolume: AudioManager == null");
            return -1;
        }
//        Log.e(TAG, ">>>>>>>>>>>>>>>>>>>>>>getCurrentVolume: "+manager.getStreamVolume(AudioManager.STREAM_MUSIC));
        return manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 亮起屏幕
     */
    public static boolean screenOn(Context context) {
        if (sWakeLock != null) {
            Log.e(TAG, "screenOn: sWakeLock已存在");
            return false;
        }
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        sWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        sWakeLock.acquire();
        //隐藏键盘锁
        disableKeyguard(context);
        return true;
    }

    /**
     * 关闭屏幕
     */
    public static void screenOff() {
        if (sWakeLock != null) {
            sWakeLock.release();
            sWakeLock = null;
            Log.e(TAG, "screenOff: 释放了唤醒锁");
        }
        //恢复键盘锁
        reenableKeyguard();
    }

    /**
     * 隐藏键盘锁
     */
    public static boolean disableKeyguard(Context context) {
        if (sKeyguardLock != null) {
            Log.e(TAG, "disableKeyguard: 隐藏键盘锁");
            return false;
        }
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        sKeyguardLock = keyguardManager.newKeyguardLock("Tag");
        sKeyguardLock.disableKeyguard();
        return true;
    }

    /**
     * 恢复键盘锁
     */
    public static void reenableKeyguard() {
        if (sKeyguardLock != null) {
            sKeyguardLock.reenableKeyguard();
            sKeyguardLock = null;
            Log.e(TAG, "reenableKeyguard: 恢复键盘锁");
        }
    }

    //判断是否连接上wifi
    public boolean isWifiConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    //获取当前连接wifi的WifiInfo
//    public WifiInfo getConnectWifiInfo(Context context) {
//        if (!isWifiConnected(context)) {
//            return null;
//        }
//        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        if (wifiManager == null) {
//            return null;
//        }
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//        return wifiInfo;
//    }

    //获取wifi列表
    public static List<ScanResult> getWifis(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        wifiManager.startScan();
        List<ScanResult> lists = wifiManager.getScanResults();
        return lists;
    }

    //判断是不是5Gwifi
    public static boolean is5GWifi(ScanResult scanResult) {
        if (scanResult == null) {
            return false;
        }
        String str = String.valueOf(scanResult.frequency);
        if (str.length() > 0) {
            char a = str.charAt(0);
            if (a == '5') {
                return true;
            }
        }
        return false;
    }

    public static ScanResult getWifiScanResult(Context context, String ssid) {
        if (ssid == null) {
            return null;
        }
        List<ScanResult> wifis = getWifis(context);
        if (wifis == null) {
            return null;
        }
        for (ScanResult result : wifis) {
            if (result.SSID.equals(ssid)) {
                return result;
            }
        }
        return null;
    }

    //WiFi是否加密
    public static boolean isWifiEncrypt(ScanResult result) {
        return !(result.capabilities.toLowerCase().indexOf("wep") != -1
                || result.capabilities.toLowerCase().indexOf("wpa") != -1);
    }

    public static String getWifiSSID(Context ctx) {
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        String ssid = null;
        if (connectionInfo != null) {
            ssid = connectionInfo.getSSID();
            if (Build.VERSION.SDK_INT >= 17 && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            if (ssid != null && (ssid.equals("<unknown ssid>") || ssid.equals("0x"))) {
                Log.e(TAG, "getWifiSSID: 无效ssid：" + ssid);
                ssid = null;
            }
        }
        return ssid;
    }

    /**
     * 获取当前设备分配给应用的内存最大值
     */
    public static int getAllocationMemory(final Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return activityManager.getMemoryClass();
    }

    // 播放默认铃声
    // 返回Notification id
    public static int PlaySound(final Context context) {
        NotificationManager mgr = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification nt = new Notification();
        nt.defaults = Notification.DEFAULT_SOUND;
        int soundId = new Random(System.currentTimeMillis())
                .nextInt(Integer.MAX_VALUE);
        mgr.notify(soundId, nt);
        return soundId;
    }

    /**
     * 获取设备当前情景模式
     * 其中 ringerMode有三种情况，分别是：AudioManager.RINGER_MODE_NORMAL（铃音模式）
     * 、AudioManager.RINGER_MODE_SILENT（静音模式）
     * 、AudioManager.RINGER_MODE_VIBRATE（震动模式）
     */
    public static int getRingerMode(final Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getRingerMode();
    }

    /**
     * 开启震动
     */
    public static Vibrator startVibrate(Context context) {
        //获取震动服务
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        //震动模式隔1秒震动1.4秒
        long[] pattern = {1000, 1400};
        //震动重复，从数组的0开始（-1表示不重复）
        vibrator.vibrate(pattern, 0);
        return vibrator;
    }

    /**
     * 停止震动
     */
    public static void stopViberate(Vibrator vibrator) {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}
