package ckc.android.develophelp.lib.widget.avatarOverlapList;

import android.content.Context;
import android.widget.ImageView;

/**
 * created by ckc on 2020-04-09
 * successfulpeter@163.com
 * <p>
 * 头像水平排列部分重叠的列表控件的适配器
 * <p>
 * 功能：
 * -暴露加载图片方法
 * -暴露生成控件方法
 */
public abstract class Adapter<T> {

    public abstract void onDisplay(Context context, ImageView imageView, T t);

    public abstract ImageView onGenerateItemView(Context context);
}
