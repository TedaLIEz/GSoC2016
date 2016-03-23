package com.hustunique.jianguo.openkeychaindemo.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hustunique.jianguo.openkeychaindemo.R;

/**
 * Created by JianGuo on 3/19/16.
 */
public class AnimationUtil {
    public static final int ANIMATION_DURATION_SHORT = 150;
    public static final int ANIMATION_DURATION_MEDIUM = 400;
    public static final int ANIMATION_DURATION_LONG = 800;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void reveal(final View view, final AnimationListener animationListener) {
        int cx = view.getWidth() - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, view.getResources().getDisplayMetrics());
        int cy = view.getHeight() / 2;
        int finalRadius = Math.max(view.getWidth(), view.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                animationListener.onAnimationCancel(view);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                animationListener.onAnimationEnd(view);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                animationListener.onAnimationStart(view);
            }
        });
        anim.start();
    }


    public interface AnimationListener {
        boolean onAnimationStart(View view);
        boolean onAnimationEnd(View view);
        boolean onAnimationCancel(View view);
    }


    private static void fadeOutView(View view, int duration) {
        fadeOutView(view, duration, null);
    }

    /**
     * Fade in a view from 1 to 0
     * @param view the animating view
     * @param duration animation duration
     * @param listener the animationListener
     */
    public static void fadeOutView(View view, int duration, @Nullable final AnimationListener listener) {
        view.setVisibility(View.GONE);
        view.setAlpha(1f);
        ViewPropertyAnimatorListener viewPropertyAnimator = null;
        if (listener != null) {
            viewPropertyAnimator = new ViewPropertyAnimatorListener() {

                @Override
                public void onAnimationStart(View view) {
                    if (!listener.onAnimationStart(view))
                        view.setDrawingCacheEnabled(true);
                }

                @Override
                public void onAnimationEnd(View view) {
                    if (!listener.onAnimationEnd(view))
                        view.setDrawingCacheEnabled(false);
                }

                @Override
                public void onAnimationCancel(View view) {

                }
            };
        }
        ViewCompat.animate(view).alpha(0f).setDuration(duration).setListener(viewPropertyAnimator);
    }

    private static void fadeInView(View view, int duration) {
        fadeInView(view, duration, null);
    }


    /**
     * Fade a view from alpha 0 to 1
     * @param view the animating view
     * @param duration the time duration
     * @param listener the animationListener
     */
    public static void fadeInView(View view, int duration, @Nullable final AnimationListener listener) {
        view.setVisibility(View.VISIBLE);
        view.setAlpha(0f);
        ViewPropertyAnimatorListener viewPropertyAnimator = null;
        if (listener != null) {
            viewPropertyAnimator = new ViewPropertyAnimatorListener() {

                @Override
                public void onAnimationStart(View view) {
                    if (!listener.onAnimationStart(view))
                        view.setDrawingCacheEnabled(true);
                }

                @Override
                public void onAnimationEnd(View view) {
                    if (!listener.onAnimationEnd(view))
                        view.setDrawingCacheEnabled(false);
                }

                @Override
                public void onAnimationCancel(View view) {

                }
            };
        }
        ViewCompat.animate(view).alpha(1f).setDuration(duration).setListener(viewPropertyAnimator);
    }



}
