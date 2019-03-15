package com.sv.common.test.bean;

public enum CommonTestMenuData {
    /**
     *
     */
    TEST_WIGHET_FRAGMENT("TestWigetFragment"),
    TEST_LIST_FRAGMENT("TestListFragment"),
    TEST_CROP_FRAGMENT("TestCropFragment"),
    ;

    private String value;

    CommonTestMenuData(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
