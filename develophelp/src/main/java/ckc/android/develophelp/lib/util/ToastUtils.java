package ckc.android.develophelp.lib.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by peter on 2018/3/14.
 */

public class ToastUtils {

    private static Toast mToast;
    private static final String TAG = "ToastUtils";

    private static long sLastToastShowTime;
    private static String sLastToastShowText;

    public static void showToast(Context context,
                                 String content) {
        if (context == null || content == null) {
            return;
        }
        //如果2次文本相同且显示间隔小于Toast.LENGTH_SHORT（2s）,则不显示
        if (sLastToastShowText != null && sLastToastShowText.equals(content)) {
            if (System.currentTimeMillis() - sLastToastShowTime < 2000) {
                return;
            }
        }
        //设置显示时长
        if (content.length() > 10) {
            Toast.makeText(context, content, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }

        //记录
        sLastToastShowTime = System.currentTimeMillis();
        sLastToastShowText = content;
    }

}
