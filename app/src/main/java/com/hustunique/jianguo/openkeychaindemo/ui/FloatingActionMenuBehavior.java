package com.hustunique.jianguo.openkeychaindemo.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by JianGuo on 3/13/16.
 * Behavior class for FloatingActionMenuBehavior
 */
public class FloatingActionMenuBehavior extends CoordinatorLayout.Behavior<FloatingActionsMenu> {
    public FloatingActionMenuBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatingActionMenuBehavior() {
        super();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionsMenu child, View dependency) {
        return dependency instanceof RecyclerView;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        ViewPropertyAnimator viewPropertyAnimator = child.animate();
        if(dyConsumed > 0){
            if (child.isExpanded()) {
                child.collapse();
                viewPropertyAnimator.setStartDelay(200);
            }
            viewPropertyAnimator.translationY(child.getHeight()).setDuration(200).start();
        } else if(dyConsumed < 0){
            if (child.isExpanded()) {
                child.collapse();
                viewPropertyAnimator.setStartDelay(200);
            }
            viewPropertyAnimator.translationY(0).setDuration(200).start();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionsMenu child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
