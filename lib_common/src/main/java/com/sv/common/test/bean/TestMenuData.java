package com.sv.common.test.bean;

import com.sv.common.AbstractBaseFragment;
import com.sv.common.screen_navigation.BaseMenuData;

public class TestMenuData extends BaseMenuData {
    private int testResId;

    public TestMenuData(int testResId, String key, Class<? extends AbstractBaseFragment> fragmentClass) {
        super(key, fragmentClass);
        this.testResId = testResId;
    }

    public static TestMenuData newInstance(int testResId, String key, Class<? extends AbstractBaseFragment> fragmentClass) {
        return new TestMenuData(testResId, key, fragmentClass);
    }

    public int getTestResId() {
        return testResId;
    }

    public void setTestResId(int testResId) {
        this.testResId = testResId;
    }
}