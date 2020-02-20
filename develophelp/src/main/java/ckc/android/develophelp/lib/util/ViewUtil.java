package ckc.android.develophelp.lib.util;

import android.view.View;

/**
 * @author ckc
 * @date 2018/12/7 16:20
 * 说明：跟View相关的工具方法
 */
public class ViewUtil {

    /**
     * 单选
     *
     * @param selectedView 选中的控件
     */
    public static void setViewsSingleSelected(View selectedView, View... views) {
        for (int i = 0; i < views.length; i++) {
            views[i].setSelected(false);
        }
        selectedView.setSelected(true);
    }

    public static void setViewsSingleVisible(View visibleView, View... views) {
        for (int i = 0; i < views.length; i++) {
            views[i].setVisibility(View.GONE);
        }
        visibleView.setVisibility(View.VISIBLE);
    }

    public static boolean isViewGone(View... views) {
        boolean gone = true;
        for (int i = 0; i < views.length; i++) {
            if (views[i].getVisibility() == View.VISIBLE) {
                gone = false;
                break;
            }
        }
        return gone;
    }

    public static void setViewsUnselected(View... views) {
        for (int i = 0; i < views.length; i++) {
            if (views[i].isSelected()) {
                views[i].setSelected(false);
            }
        }
    }
}
