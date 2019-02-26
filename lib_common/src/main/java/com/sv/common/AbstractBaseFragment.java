package com.sv.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.kayvannj.permission_utils.Func;
import com.sv.common.screen_navigation.ActivityCommonListener;
import com.sv.common.screen_navigation.BaseMenuData;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class AbstractBaseFragment extends Fragment implements ActivityCommonListener {
    private static final String TAG = AbstractBaseFragment.class.getSimpleName();
    private BaseMenuData menuData;
    private Unbinder unbinder;
    protected View rootView;
    protected boolean cacheContentData = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (null == rootView || !cacheContentData) {
            rootView = onCreateFragmentView(inflater, container, savedInstanceState);
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
    /**
     * @return use for {@link AbstractBaseFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}, for butterknife bind.
     */
    protected abstract View onCreateFragmentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /**
     * 提供给子类调用,适用于单个或多个permission的简单请求（https://github.com/kayvannj/PermissionUtil）
     */
    protected void doSimpleRequestPermission(String[] permissionStr, int requestCode, Func
            onAllGranted, Func onAnyDenied) {
        AbstractBaseFragmentActivity baseFragmentActivity = (AbstractBaseFragmentActivity) getActivity();
        baseFragmentActivity.doSimpleRequestPermission(permissionStr, requestCode, onAllGranted, onAnyDenied);
    }


    protected void switchFragmentByMenuData(BaseMenuData targetMenuData) {
        if (null != getActivity() && getActivity() instanceof AbstractBaseFragmentActivity) {
            AbstractBaseFragmentActivity baseFragmentActivity = (AbstractBaseFragmentActivity) getActivity();
            baseFragmentActivity.switchFragmentByMenuData(targetMenuData);
        }
    }

    protected void showLoadingDialog() {
        if (null != getActivity() && getActivity() instanceof AbstractBaseFragmentActivity) {
            AbstractBaseFragmentActivity baseFragmentActivity = (AbstractBaseFragmentActivity) getActivity();
            baseFragmentActivity.showLoadingDialog();
        }
    }

    protected void cancelLoadingDialog() {
        if (null != getActivity() && getActivity() instanceof AbstractBaseFragmentActivity) {
            AbstractBaseFragmentActivity baseFragmentActivity = (AbstractBaseFragmentActivity) getActivity();
            baseFragmentActivity.cancelLoadingDialog();
        }
    }

    /**
     * 提供给子类重写
     */
    @Override
    public boolean beforeSwitchScreen(BaseMenuData targetMenuData) {
        //return false to stop fragment replace
        return true;
    }

    /**
     * 提供给子类重写
     */
    @Override
    public Runnable handlerBeforeSwitchScreen(BaseMenuData targetMenuData) {
        //return not null to stop fragment replace
        return null;
    }

    protected void popBackScreen() {
        ((AbstractBaseFragmentActivity) Objects.requireNonNull(this.getActivity())).screenNavigator.popBackScreen();
    }
    protected void showToast(int resId) {
        if(null != getActivity()){
            Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
        }
    }
    protected void showToast(String resStr) {
        if(null != getActivity()){
            Toast.makeText(getActivity(), resStr, Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    public BaseMenuData getMenuData() {
        return menuData;
    }

    public void setMenuData(BaseMenuData menuData) {
        this.menuData = menuData;
    }

    public boolean isCacheContentData() {
        return cacheContentData;
    }

    public void setCacheContentData(boolean cacheContentData) {
        this.cacheContentData = cacheContentData;
    }
    public View getRootView() {
        return rootView;
    }
}