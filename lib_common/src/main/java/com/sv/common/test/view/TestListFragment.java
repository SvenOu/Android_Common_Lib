package com.sv.common.test.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sv.common.AbstractBaseFragment;
import com.sv.common.R;
import com.sv.common.R2;
import com.sv.common.widget.AbstractSimpleListBaseAdapter;
import com.sv.common.widget.CheckedLinelayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TestListFragment extends AbstractBaseFragment implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = TestListFragment.class.getName();

    @BindView(R2.id.btn_toggle) ToggleButton btnToggle;
    @BindView(R2.id.list1) ListView list1;
    private ListAdapter listAdapter;

    @Override
    protected View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        btnToggle.setOnCheckedChangeListener(this);
        list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listAdapter = new ListAdapter(getContext(), createListData());
        list1.setAdapter(listAdapter);
    }

    private List<String> createListData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.add("item_" + i);
        }
        return data;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            list1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        }else {
            list1.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    private static class ListAdapter extends AbstractSimpleListBaseAdapter<String>{
        public ListAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        protected void onBindViewHolder(ViewBinder viewBinder) {
            viewBinder.itemId = R.layout.example_simple_list_item;
            viewBinder.elementIds = new String[] {"id.checkedLinelayout","id.btn_item"};
        }

        @Override
        protected void onBindView(String itemText, Map viewHolder, View convertView, int position) {
            TextView btnItem = (TextView) viewHolder.get("id.btn_item");
            CheckedLinelayout checkedLinelayout = (CheckedLinelayout) viewHolder.get("id.checkedLinelayout");
            btnItem.setText(itemText);
            checkedLinelayout.setOnCheckChangeListener(new CheckedLinelayout.OnCheckChangeListener() {
                @Override
                public void onCheckChange(boolean mChecked) {
                    btnItem.setSelected(mChecked);
                }
            });
        }
    }
}
