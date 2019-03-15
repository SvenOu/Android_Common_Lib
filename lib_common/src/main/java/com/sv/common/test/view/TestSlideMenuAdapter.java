package com.sv.common.test.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.sv.common.R;
import com.sv.common.test.bean.CommonTestMenuData;
import com.sv.common.test.bean.TestMenuData;
import com.sv.common.widget.AbstractSimpleListBaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestSlideMenuAdapter extends AbstractSimpleListBaseAdapter<TestMenuData> {
    private List<TestMenuData> menuDatas = new ArrayList<>();

    public TestSlideMenuAdapter(Context context) {
        super(context, null);
        recreateMenuData();
        setData(menuDatas);
    }

    public void recreateMenuData() {
        menuDatas.clear();
        menuDatas.add(TestMenuData.newInstance(R.string.test_wiget_fragment, CommonTestMenuData.TEST_WIGHET_FRAGMENT.getValue(), TestWigetFragment.class));
        menuDatas.add(TestMenuData.newInstance(R.string.test_list_fragment, CommonTestMenuData.TEST_LIST_FRAGMENT.getValue(), TestListFragment.class));
        menuDatas.add(TestMenuData.newInstance(R.string.test_crop_fragment, CommonTestMenuData.TEST_CROP_FRAGMENT.getValue(), TestCropFragment.class));
    }

    @Override
    protected void onBindViewHolder(AbstractSimpleListBaseAdapter.ViewBinder viewBinder) {
        viewBinder.itemId = R.layout.test_slide_menu_item;
        viewBinder.elementIds = new String[] {"id.menu_title"};
    }

    @Override
    protected void onBindView(TestMenuData itemData, Map viewHolder, View convertView, int position) {
        TextView menuTitle = (TextView) viewHolder.get("id.menu_title");
        menuTitle.setText(itemData.getTestResId());
    }
}
