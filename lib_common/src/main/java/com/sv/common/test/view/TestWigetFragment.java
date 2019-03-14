package com.sv.common.test.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sv.common.AbstractBaseFragment;
import com.sv.common.R;
import com.sv.common.R2;
import com.sv.common.databinding.DataBindingObject;
import com.sv.common.test.bean.TestWigetViewModel;
import com.sv.common.widget.CycleTextView;
import com.sv.common.widget.ReactiveRatingBar;

import butterknife.BindView;

public class TestWigetFragment extends AbstractBaseFragment implements ReactiveRatingBar.ReactiveRatingBarListener {
    private static final String TAG = TestWigetFragment.class.getName();

    @BindView(R2.id.cycleCountView) CycleTextView cycleCountView;
    @BindView(R2.id.reactiveRatingBar1) ReactiveRatingBar reactiveRatingBar1;
    @BindView(R2.id.reactiveRatingBar2) ReactiveRatingBar reactiveRatingBar2;
    private DataBindingObject<TestWigetViewModel> bindingObject;

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
        bindingObject = new DataBindingObject<TestWigetViewModel>(new TestWigetViewModel()) {
            @Override
            protected void onSaveModel(TestWigetViewModel testWigetViewModel) {
                testWigetViewModel.setRating1(reactiveRatingBar1.getRating());
                testWigetViewModel.setRating2(reactiveRatingBar2.getRating());
            }

            @Override
            protected void onRefreshUI(TestWigetViewModel testWigetViewModel) {
                reactiveRatingBar1.setRating(testWigetViewModel.getRating1());
                reactiveRatingBar2.setRating(testWigetViewModel.getRating2());
            }
        };
        reactiveRatingBar1.setReactiveRatingBarListener(this);
        reactiveRatingBar2.setReactiveRatingBarListener(this);

        testChangeModel();
    }

    private void testChangeModel() {
        // 以下逻辑 UI 改动后，通过触发保存数据
        reactiveRatingBar1.setRating(1);
        reactiveRatingBar2.setRating(4);
        bindingObject.triggerSaveModel();
    }

    @Override
    public void onRatingTouchChanged(ReactiveRatingBar reactiveRatingBar, int rating) {
        int i = reactiveRatingBar.getId();
        // 以下逻辑通过数据驱动UI
        TestWigetViewModel model = bindingObject.getModel();
        if (i == R.id.reactiveRatingBar1) {
            model.setRating1(rating);
            model.setRating2(rating);
        }
        else if (i == R.id.reactiveRatingBar2){
            model.setRating1(rating);
            model.setRating2(rating);
        }
        bindingObject.triggerRefreshUI();
    }
}
