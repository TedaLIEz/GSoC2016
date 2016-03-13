package com.hustunique.jianguo.openkeychaindemo.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flipboard.bottomsheet.commons.BottomSheetFragment;
import com.hustunique.jianguo.openkeychaindemo.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KeyDetailFragment extends BottomSheetFragment {


    public KeyDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_key_detail, container, false);
    }

}
