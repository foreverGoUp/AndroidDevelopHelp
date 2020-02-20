package ckc.android.develophelp.lib.util;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements UncaughtExceptionHandler {

    public static boolean DEBUG = false;
    private static String TAG = "CrashHandler";
    //系统默认的处理类
    private UncaughtExceptionHandler mDefaultHandler;
    //单例
    private static CrashHandler instance = new CrashHandler();
    //存储设备信息
    private Map<String, String> mDeviceInfoMap = new HashMap<String, String>();
    //应用实例
    private IApp mApp;
    //是否重启应用
    private boolean mIsRestartApp = false;


    private CrashHandler() {
    }

    /**
     * 获得上下文
     */
    public Context getContext() {
        return (Context) mApp;
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static CrashHandler getInstance() {
        return instance;
    }

    /**
     * 初始化
     */
    public void init(IApp app, boolean isRestartApp) {
        if (DEBUG) Log.e(TAG, "[DEBUG] onInit");
        if (app == null) {
            throw new IllegalArgumentException("初始化参数app为空");
        }
        if (!(app instanceof Application)) {
            throw new IllegalArgumentException("初始化参数app必须是Application的实例");
        }
        mApp = app;
        mIsRestartApp = isRestartApp;
        // 获取系统默认的UncaughtException处理器  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当异常发生时会回调该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex)) {// 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            if (mIsRestartApp) {//重启应用
                restartApp(getContext());
            } else {//结束app
                mApp.onFinishApp();
            }
        }
    }

    /**
     * 打印异常信息
     */
    private String printEx(Throwable ex) {
        if (DEBUG) Log.e(TAG, "[DEBUG] printEx");
        if (ex == null) return null;
        ex.printStackTrace();

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.flush();
        printWriter.close();
        String result = writer.toString();
        if (DEBUG) android.util.Log.e(TAG, "[DEBUG] " + result);
        return result;
    }

    /**
     * 重启应用
     */
    public void restartApp(Context ctx) {
        if (DEBUG) Log.e(TAG, "[DEBUG] restartApp");
        if (mApp.getLauncherActivityClass() != null) {
            Intent intent = new Intent(ctx, mApp.getLauncherActivityClass());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent restartIntent = PendingIntent.getActivity(
                    ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            //1秒钟后重启应用
            AlarmManager mgr = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
                    restartIntent);
        } else {
            android.util.Log.e(TAG, "重启应用失败：mApp.getLauncherActivityClass() == null");
        }
        //结束app
        mApp.onFinishApp();
    }


    /**
     * 处理异常
     * 1、打印
     * 2、保存文件
     */
    private boolean handleException(Throwable ex) {
//        try {
//            // 使用Toast来显示异常信息
//            AppExecutors.getInstance().mainThread().execute(new Runnable() {
//                @Override
//                public void run() {
//                    ToastUtils.showToast(mContext, "很抱歉，程序出现异常");
//                }
//            });
//            // 收集设备参数信息
//            collectDeviceInfo(mContext);
//            // 保存日志文件
//            mCrashFileName = saveCrashInfoFile(ex);
//            sendMail();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        //打印异常信息
        String exInfo = printEx(ex);
        //保存到文件
        if (exInfo != null) saveToFile(exInfo);
        return true;
    }

    /**
     * 收集设备参数信息
     */
    private void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName + "";
                String versionCode = pi.versionCode + "";
                mDeviceInfoMap.put("versionName", versionName);
                mDeviceInfoMap.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info");
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mDeviceInfoMap.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "an error occured when collect crash info");
            }
        }
    }

    /**
     * 保存到文件中
     *
     * @return 返回文件名称, 便于将文件传送到服务器
     */
    private String saveToFile(String exInfo) {
        StringBuffer sb = new StringBuffer();
        try {
            //添加日期
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String date = dateFormat.format(new Date());
            sb.append("\r\n" + date + "\n");
            //添加设备信息
            collectDeviceInfo(getContext());
            for (Map.Entry<String, String> entry : mDeviceInfoMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                sb.append(key + "=" + value + "\n");
            }
            //添加异常信息
            sb.append(exInfo);
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...");
            sb.append("an error occured while writing file...\r\n");
        }
        //按天记录闪退信息
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = "crash-" + formatter.format(new Date()) + ".log";
        FileUtil.write(FileUtil.DIR_APP_CRASH, fileName, sb.toString(), true);
        return fileName;
    }

    //    private void sendMail() {
//        Log.writeCacheLog();
//        String filePath = FileUtil.getFilePath(FileUtil.DIR_APP_CRASH, mCrashFileName);
//        if (FileUtil.existFile(FileUtil.DIR_APP_CRASH, mCrashFileName)) {
//            String[] fPaths = new String[2];
//            fPaths[0] = filePath;
//            fPaths[1] = FileUtil.getFilePath(FileUtil.DIR_APP_LOGOUT, FileUtil.getLogoutFileName());
//            String subject = String.format(mContext.getString(R.string.crash_mail_subject)
//                    , mContext.getString(R.string.app_name));
//            Mail.send(true, subject, fPaths);
//        } else {
//            Log.e(TAG, "发送邮件失败，文件不存在...");
//        }
//    }

//    private void sendMail() {
//        new Thread(new Runnable() {
//            
//            @Override
//            public void run() {
//                send();
//            }
//        }).start();
//    }

//    private void send() {
//        Log.e(TAG, "准备发送邮件");
//        Mail mail = new Mail("successfulpeter@163.com", "Kcinwyyx1");
//        mail.setTo(new String[] { "successfulpeter@163.com" });
//        mail.setSubject("shp服务端异常报告");
//        mail.setBody("请看附件~");
//        mail.setFrom("successfulpeter@163.com");
//        try {
//            mail.addAttachment(FileUtil.getFilePath(FileUtil.DIR_APP_CRASH, mCrashFileName));
//        } catch (Exception e1) {
//            Log.e(TAG, "添加附件异常...");
//            e1.printStackTrace();
//            return;
//        }
//        try {
//            if (mail.send()) {
//                //删除本地已发送邮件
//                Log.e(TAG, "邮件发送成功！");
//            } else {
//                Log.e(TAG, "邮件发送失败！");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e(TAG, "邮件发送异常！");
//        }
//    }

    /**
     * 给应用类实现的接口
     */
    public interface IApp {

        void onFinishApp();

        Class getLauncherActivityClass();
    }

}
