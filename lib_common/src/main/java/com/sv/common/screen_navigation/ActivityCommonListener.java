package com.sv.common.screen_navigation;

import android.support.annotation.Nullable;

/**
 * @author sven-ou
 */
public interface ActivityCommonListener {
    /**
     * @param targetMenuData
     * 离开当前screen 时触发
     * 返回 false 阻止跳转
     */
    boolean beforeSwitchScreen(@Nullable BaseMenuData targetMenuData);

    /**
     * @param targetMenuData
     * 离开当前screen 时触发
     * 返回 Runnable 阻止跳转,并执行 Runnable
     */
    Runnable handlerBeforeSwitchScreen(BaseMenuData targetMenuData);
}