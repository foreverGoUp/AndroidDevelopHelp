package ckc.android.develophelp.lib.util;

import java.util.Vector;

public class Log {

    private static final String TAG = "Log";

    private static boolean ENABLE_V = true;//verbose
    private static boolean ENABLE_D = true;//debug
    private static boolean ENABLE_I = true;//info
    private static boolean ENABLE_W = true;//warn
    private static boolean ENABLE_E = true;//error
    private static boolean ENABLE_LOG_FILE = true;//是否启用日志文件
    private static int MAX_CACHE_COUNT = 20;//收集到最大数量的日志数后统一输出到文件中
    private static Vector<String> sLogToFileVector = new Vector(MAX_CACHE_COUNT);

    public static void init(boolean enableV, boolean enableD, boolean enableI, boolean enableW, boolean enableE, boolean enableLogFile, int maxCacheCount) {
        ENABLE_V = enableV;
        ENABLE_D = enableD;
        ENABLE_I = enableI;
        ENABLE_W = enableW;
        ENABLE_E = enableE;
        ENABLE_LOG_FILE = enableLogFile;
        MAX_CACHE_COUNT = maxCacheCount;
    }

    public static void v(String tag, String content) {
        if (ENABLE_V) {
            android.util.Log.v(tag, content);
        }
        if (ENABLE_LOG_FILE) {
            saveToFile(tag, content);
        }
    }

    public static void d(String tag, String content) {
        if (ENABLE_D) {
            android.util.Log.d(tag, content);
        }
        if (ENABLE_LOG_FILE) {
            saveToFile(tag, content);
        }
    }

    public static void i(String tag, String content) {
        if (ENABLE_I) {
            android.util.Log.i(tag, content);
        }
        if (ENABLE_LOG_FILE) {
            saveToFile(tag, content);
        }
    }

    public static void w(String tag, String content) {
        if (ENABLE_W) {
            android.util.Log.w(tag, content);
        }
        if (ENABLE_LOG_FILE) {
            saveToFile(tag, content);
        }
    }

    public static void e(String tag, String content) {
        if (ENABLE_E) {
            android.util.Log.e(tag, content);
        }
        if (ENABLE_LOG_FILE) {
            saveToFile(tag, content);
        }
    }

    public static void e(String tag, String content, Throwable throwable) {
        if (ENABLE_E) {
            android.util.Log.e(tag, content);
            throwable.printStackTrace();
        }
        if (ENABLE_LOG_FILE) {
            saveToFile(tag, content);
        }
    }

    /**
     * 保存到文件
     */
    public static void saveToFile(String tag, String content) {
        sLogToFileVector.add("\n" + TimeUtils.getDefaultDateTime() + " " + tag + " " + content);
        if (sLogToFileVector.size() >= MAX_CACHE_COUNT) {
            FileUtil.writeToLogoutFile(sLogToFileVector.toString());
            sLogToFileVector.clear();
        }
    }

}
