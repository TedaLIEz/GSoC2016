package com.hustunique.jianguo.openkeychaindemo.ui;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewPropertyAnimator;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

/**
 * Created by JianGuo on 3/13/16.
 * Behavior class for CustomFabBehavior
 */
public class CustomFabBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {
    public CustomFabBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFabBehavior() {
        super();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof RecyclerView | dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        ViewPropertyAnimator viewPropertyAnimator = child.animate();
        if(dyConsumed > 0){
            child.hide();
        } else if(dyConsumed < 0){
            child.show();
        }
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
