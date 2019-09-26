package com.example.testjavaapp.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.Button;

public class CustomAnimator {
    private static final long SHRINK_DURATION = 200;
    private static final long EXPAND_DURATION = 200;
    private static final long DELAY_DURATION = 200;
    private static boolean isAnimate = false;

    public static void pressAnimation(final Button view) {
        if (view != null && !isAnimate) {
            AnimatorSet set = new AnimatorSet();
            set.playSequentially(
                    shrinkAnimatorSet(view),
                    expandAnimatorSet(view));
            set.addListener(getLikeEndListener(view));
            set.start();
        }
    }

    private static AnimatorListenerAdapter getLikeEndListener(final Button view) {
        return new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimate = true;
                view.setEnabled(false);
                view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimate = false;
                view.setEnabled(true);
                view.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        };
    }

    private static AnimatorSet shrinkAnimatorSet(View view) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(SHRINK_DURATION).playTogether(
                ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f),
                ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 0f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 0f)
        );
        return set;
    }

    private static AnimatorSet expandAnimatorSet(View view) {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(EXPAND_DURATION).playTogether(
                ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f),
                ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f)
        );
        set.setStartDelay(DELAY_DURATION);
        return set;
    }
}