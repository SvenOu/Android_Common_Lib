package com.sv.common.test.view;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.sv.common.AbstractBaseFragmentActivity;
import com.sv.common.R;
import com.sv.common.R2;
import com.sv.common.screen_navigation.BaseMenuData;
import com.sv.common.screen_navigation.NavigatorListener;
import com.sv.common.test.bean.CommonTestMenuData;
import com.sv.common.test.bean.TestMenuData;
import com.sv.common.util.Logger;
import com.sv.common.util.SystemUtil;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

@Route(path = "/lib_common/test/TestWigetActivity")
public class TestWigetActivity extends AbstractBaseFragmentActivity {
    private static final String TAG = AbstractBaseFragmentActivity.class.getName();
    @BindView(R2.id.top_slide_menu_button) Button topSlideMenuButton;
    @BindView(R2.id.tv_header_title) TextView tvHeaderTitle;
    @BindView(R2.id.header_bar) LinearLayout headerBar;
    @BindView(R2.id.common_fragment_container) FrameLayout commonFragmentContainer;
    @BindView(R2.id.fl_parent) FrameLayout flParent;
    @BindView(R2.id.content_frame) LinearLayout contentFrame;
    @BindView(R2.id.left_drawer) ListView leftDrawer;
    @BindView(R2.id.drawer_layout) DrawerLayout drawerLayout;
    private TestSlideMenuAdapter testSlideMenuAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_wighet);
        init();
        intNavigator();
        iniFragment();
    }

    private void init() {
        testSlideMenuAdapter = new TestSlideMenuAdapter(this);
        leftDrawer.setAdapter(testSlideMenuAdapter);
        leftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TestMenuData menuData = testSlideMenuAdapter.getItem(position);
                switchFragmentByMenuData(menuData);
                drawerLayout.closeDrawers();
            }
        });
    }

    private void intNavigator() {
        screenNavigator.bind(this, R.id.common_fragment_container, CommonTestMenuData.TEST_WIGHET_FRAGMENT.getValue(), new NavigatorListener() {
            @Override
            public boolean beforeSwitchScreen(BaseMenuData targetMenuData) {
                return true;
            }

            @Override
            public void onStopSwitchScreen(BaseMenuData targetMenuData) {
                drawerLayout.closeDrawers();
                return;
            }

            @Override
            public void afterSwitchScreen(BaseMenuData targetMenuData) {
                if(targetMenuData instanceof TestMenuData){
                    tvHeaderTitle.setText(((TestMenuData)targetMenuData).getTestResId());
                }
            }
        });
    }
    private void iniFragment() {
        switchFragmentByMenuData(TestMenuData.newInstance(R.string.test_wiget_fragment,
                CommonTestMenuData.TEST_WIGHET_FRAGMENT.getValue(), TestWigetFragment.class));
    }

    @OnClick(R2.id.top_slide_menu_button)
    public void onViewClicked(View view) {
        if(view.getId() == R.id.top_slide_menu_button){
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //关闭侧边栏
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawers();
                return true;
            }
            if(SystemUtil.shouldExit(this, R.string.pressBackExit)){
                finish();
                System.exit(0);
            }
        }
        return false;
    }
}
