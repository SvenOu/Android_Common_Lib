package com.sv.common.test.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sv.common.AbstractBaseFragment;
import com.sv.common.R;
import com.sv.common.R2;
import com.sv.common.widget.CycleTextView;

import butterknife.BindView;

public class TestWigetFragment extends AbstractBaseFragment {
    private static final String TAG = TestWigetFragment.class.getName();
    @BindView(R2.id.cycleCountView) CycleTextView cycleCountView;

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_wiget, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {

    }

}
