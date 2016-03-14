package com.hustunique.jianguo.openkeychaindemo.ui;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flipboard.bottomsheet.ViewTransformer;
import com.flipboard.bottomsheet.commons.BottomSheetFragment;
import com.hustunique.jianguo.openkeychaindemo.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeyDetailFragment extends BottomSheetFragment {
    @Bind(R.id.key_info_list)
    NestedScrollView mList;
    @Bind(R.id.key_collasping_toolbar)
    CollapsingToolbarLayout mLayout;
    @Bind(R.id.key_detail_layout)
    AppBarLayout appBarLayout;

    public KeyDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_key_detail, container, false);
        ButterKnife.bind(this, view);
        return inflater.inflate(R.layout.fragment_key_detail, container, false);
    }

    @Override
    public ViewTransformer getViewTransformer() {
        return super.getViewTransformer();
    }
}
