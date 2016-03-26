package com.hustunique.jianguo.openkeychaindemo.ui;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.View;

import com.hustunique.jianguo.openkeychaindemo.R;

/**
 * Created by JianGuo on 3/26/16.
 * Bottom sheet fragment showing key information.
 */
public class KeyDetailFragment extends BottomSheetDialogFragment {


    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getActivity(), R.layout.sheet_key, null);
        dialog.setContentView(contentView);
        View parent = (View) contentView.getParent();
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(parent);
        contentView.measure(0, 0);
        bottomSheetBehavior.setPeekHeight(contentView.getMeasuredHeight());
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
        if (params.getBehavior() instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior)params.getBehavior()).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        parent.setLayoutParams(params);
    }
}
