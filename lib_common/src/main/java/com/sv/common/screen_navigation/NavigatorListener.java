package com.sv.common.screen_navigation;
import com.sv.common.AbstractBaseFragment;
public interface NavigatorListener {
    /**
     *
     * @param targetMenuData  前进和后退的时候的目标fragment
     * @return  返回 false 去阻止跳转
     */
    boolean beforeSwitchScreen(BaseMenuData targetMenuData);
    /**
     *
     *  NavigatorListener接口的{@link NavigatorListener#beforeSwitchScreen(BaseMenuData)}方法
     *  或者 BaseFragment 的 {@link AbstractBaseFragment#beforeSwitchScreen(BaseMenuData)} 方法，返回false之后，
     *  执行onStopSwitchScreen事件，表示跳转被阻止
     */
    void onStopSwitchScreen(BaseMenuData targetMenuData);
    /**
     * 当Screen跳转成功之后执行此方法
     */
    void afterSwitchScreen(BaseMenuData targetMenuData);
}