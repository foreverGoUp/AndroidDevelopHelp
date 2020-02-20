package ckc.android.develophelp.lib.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ckc on 2016/11/30.
 */
public class SPreferencesHelper {

    //不同命名则可以建立多个SharedPreferences表
    public static final String SP_MAIN = "SHPreferences";
    public static final String SP_DOORBELL = "DOORBELL";

    public static void saveString(Context context, String sp, String key, String value) {
        SharedPreferences sP = context.getSharedPreferences(sp, Context.MODE_PRIVATE);
        if (sP != null) {
            SharedPreferences.Editor editor = sP.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String getString(Context context, String sp, String key) {
        SharedPreferences sP = context.getSharedPreferences(sp, Context.MODE_PRIVATE);
        if (sP != null) {
            return sP.getString(key, sp);
        } else {
            return null;
        }
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sP = context.getSharedPreferences(SP_MAIN, Context.MODE_PRIVATE);
        if (sP != null) {
            SharedPreferences.Editor editor = sP.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String getString(Context context, String key) {
        SharedPreferences sP = context.getSharedPreferences(SP_MAIN, Context.MODE_PRIVATE);
        if (sP != null) {
            return sP.getString(key, null);
        } else {
            return null;
        }
    }

    public static void saveBool(Context context, String key, boolean value) {
        SharedPreferences sP = context.getSharedPreferences(SP_MAIN, Context.MODE_PRIVATE);
        if (sP != null) {
            SharedPreferences.Editor editor = sP.edit();
            editor.putBoolean(key, value);
            editor.commit();
        }
    }

    public static boolean getBool(Context context, String key) {
        SharedPreferences sP = context.getSharedPreferences(SP_MAIN, Context.MODE_PRIVATE);
        if (sP != null) {
            return sP.getBoolean(key, false);
        } else {
            return false;
        }
    }

    public static boolean getBool(Context context, String key, boolean defValue) {
        SharedPreferences sP = context.getSharedPreferences(SP_MAIN, Context.MODE_PRIVATE);
        if (sP != null) {
            return sP.getBoolean(key, defValue);
        } else {
            return defValue;
        }
    }

    public static void saveInt(Context context, String key, int value) {
        SharedPreferences sP = context.getSharedPreferences(SP_MAIN, Context.MODE_PRIVATE);
        if (sP != null) {
            SharedPreferences.Editor editor = sP.edit();
            editor.putInt(key, value);
            editor.commit();
        }
    }

    public static void saveLong(Context context, String key, long value) {
        SharedPreferences sP = context.getSharedPreferences(SP_MAIN, Context.MODE_PRIVATE);
        if (sP != null) {
            SharedPreferences.Editor editor = sP.edit();
            editor.putLong(key, value);
            editor.commit();
        }
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sP = context.getSharedPreferences(SP_MAIN, Context.MODE_PRIVATE);
        if (sP != null) {
            return sP.getInt(key, -404);
        } else {
            return -404;
        }
    }

    public static int getInt(Context context, String key, int defValue) {
        SharedPreferences sP = context.getSharedPreferences(SP_MAIN, Context.MODE_PRIVATE);
        if (sP != null) {
            return sP.getInt(key, defValue);
        } else {
            return defValue;
        }
    }

    public static Long getLong(Context context, String key, long defValue) {
        SharedPreferences sP = context.getSharedPreferences(SP_MAIN, Context.MODE_PRIVATE);
        if (sP != null) {
            return sP.getLong(key, defValue);
        } else {
            return defValue;
        }
    }

    /**
     * 清空指定sp数据
     */
    public static void clear(Context context, String spName) {
        SharedPreferences sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        if (sp == null) {
            return;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
