package ckc.android.develophelp.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * created by ckc on 2020-04-05
 * successfulpeter@163.com
 * 后部优先显示线性布局
 * 说明：在系统的源码里发现一个继承与LinearLayout的布局EllipsizeLayout
 * <p>
 * 功能：
 * -LinearLayout后部控件优先完整显示，前部太长需要设置省略
 * <p>
 * 缺点：
 * -第一个控件距离后一个有未知的间隔。
 * -控件无法使用weight参数。
 */
public class RearFirstLinearLayout extends LinearLayout {

    public RearFirstLinearLayout(Context context) {
        this(context, null);
    }

    public RearFirstLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getOrientation() == HORIZONTAL
                && (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY)) {
            int totalLength = 0;
            boolean outOfSpec = false;
            TextView ellipView = null;
            final int count = getChildCount();

            for (int ii = 0; ii < count && !outOfSpec; ++ii) {
                final View child = getChildAt(ii);
                if (child != null && child.getVisibility() != GONE) {
                    if (child instanceof TextView) {
                        final TextView tv = (TextView) child;
                        if (tv.getEllipsize() != null) {
                            if (ellipView == null) {
                                ellipView = tv;
                                // clear maxWidth on mEllipView before measure
                                ellipView.setMaxWidth(Integer.MAX_VALUE);
                            } else {
                                // TODO: support multiple android:ellipsize
                                outOfSpec = true;
                            }
                        }
                    }
                    final LayoutParams lp = (LayoutParams) child
                            .getLayoutParams();
                    outOfSpec |= (lp.weight > 0f);
                    measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                    totalLength += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                }
            }
            outOfSpec |= (ellipView == null) || (totalLength == 0);
            final int parentWidth = MeasureSpec.getSize(widthMeasureSpec);

            if (!outOfSpec && totalLength > parentWidth) {
                int maxWidth = ellipView.getMeasuredWidth() - (totalLength - parentWidth);
                // TODO: Respect android:minWidth (easy with @TargetApi(16))
                int minWidth = 0;
                if (maxWidth < minWidth) {
                    maxWidth = minWidth;
                }
                ellipView.setMaxWidth(maxWidth);
            }
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}