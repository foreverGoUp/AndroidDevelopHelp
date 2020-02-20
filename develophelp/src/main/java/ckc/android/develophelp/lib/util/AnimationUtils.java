package ckc.android.develophelp.lib.util;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/8/30.
 */

public class AnimationUtils {

    private static final String TAG = AnimationUtils.class.getSimpleName();

    public static void stopAnimationDrawable(ImageView imageView, Integer drawableResId) {
        if (imageView == null) return;
        Drawable drawable = imageView.getBackground();
        if (drawable == null) return;

        if (drawable instanceof AnimationDrawable) {
            AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
            if (anim == null) return;
            if (anim.isRunning()) {
                anim.selectDrawable(0);
                anim.stop();
//                Log.e(TAG, "anim.setVisible: "+anim.setVisible(true, true));
            }
        }
        if (drawableResId != null) {
            imageView.setBackgroundResource(drawableResId);
        }
    }

    public static void startAnimationDrawable(ImageView imageView, Integer drawableResId) {
        if (imageView == null) return;
        if (drawableResId != null) {
            imageView.setBackgroundResource(drawableResId);
        }
        final AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
        if (anim == null) return;
        imageView.post(new Runnable() {

            @Override
            public void run() {
                anim.start();
            }
        });
    }
}
