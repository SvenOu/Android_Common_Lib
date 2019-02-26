package com.sv.common.screen_navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.sv.common.AbstractBaseFragment;
import com.sv.common.AbstractBaseFragmentActivity;
import com.sv.common.R;
import com.sv.common.util.Logger;

import java.util.Stack;

/**
 * 
 * 用于管理activity内部的fragment或者dialog,view跳转
 * @author sven-ou
 */

public class ScreenNavigator {

    private static final String TAG = ScreenNavigator.class.getSimpleName();

    //宿主activity，由@ContextSingleton修饰，不用担心内存泄漏
    private AbstractBaseFragmentActivity baseFragmentActivity;

    private FragmentManager fragmentManager;

    //界面跳转的历史栈
    private Stack<BaseMenuData> historyStack;

    //activity内部的主界面
    private String homeFragmentKey;

    private int fragmentContainerId;

    private NavigatorListener navigatorListener;

    //activity的onCreate事件处调用
    public void bind(AbstractBaseFragmentActivity baseFragmentActivity, int fragmentContainerId, String homeFragmentKey, NavigatorListener navigatorListener) {

        this.baseFragmentActivity = baseFragmentActivity;
        this.fragmentManager = baseFragmentActivity.getSupportFragmentManager();
        this.fragmentContainerId = fragmentContainerId;
        this.homeFragmentKey = homeFragmentKey;
        this.navigatorListener = navigatorListener;

        if(TextUtils.isEmpty(this.homeFragmentKey)){
            Logger.e(TAG , "the homeFragmentKey is empty.");
            this.homeFragmentKey = "";
        }

        if(this.fragmentContainerId <= 0){
            Logger.e(TAG, "fragmentContainerId is empty, use default id (R.id.fragment_container)");
            this.fragmentContainerId = R.id.common_fragment_container;
        }

        if(null == navigatorListener){
            Logger.e(TAG, "navigatorListener is empty, use default navigatorListener");
            this.navigatorListener = new NavigatorListener() {
                @Override
                public boolean beforeSwitchScreen(BaseMenuData targetMenuData) {
                    return true;
                }
                @Override
                public void onStopSwitchScreen(BaseMenuData targetMenuData) {
                    Logger.i(TAG, "force stop switch to fragment: " + targetMenuData.getKey());
                }

                @Override
                public void afterSwitchScreen(BaseMenuData targetMenuData) {
                    Logger.i(TAG, "after switch to fragment: " + targetMenuData.getKey());
                }
            };
        }
        if(null == historyStack){
            historyStack = new Stack<>();
        }
    }

    //在activity的onDestroy事件里调用
    public void unBind(AbstractBaseFragmentActivity baseFragmentActivity) {
        this.navigatorListener = null;
        this.baseFragmentActivity = null;
    }

    public void switchScreenByMenuData(final BaseMenuData targetMenuData) {
        ActivityCommonListener actCommonListener = baseFragmentActivity.getActivityCommonListener();
        if(!navigatorListener.beforeSwitchScreen(targetMenuData) ||
                null != actCommonListener && !actCommonListener.beforeSwitchScreen(targetMenuData)) {

            Logger.e(TAG + "--" + targetMenuData.getClass().getName(),
                    ".beforeSwitchScreen() return false, then stop replace fragment.");

            navigatorListener.onStopSwitchScreen(targetMenuData);
            return;
        }

        // 通过 Runnable 拦截非 AbstractBaseFragment 跳转
        if(null != actCommonListener){
            Runnable runnable = actCommonListener.handlerBeforeSwitchScreen(targetMenuData);
            if(null != runnable){
                runnable.run();
                return;
            }
        }

        AbstractBaseFragment fragment = null;
        Class<? extends AbstractBaseFragment> targetFragmentClass = null;
        targetFragmentClass = targetMenuData.getFragmentClass();
        if(null == targetFragmentClass){
            targetFragmentClass = AbstractBaseFragment.class;
            Logger.w(TAG, "targetFragmentClass is null, use BaseFragment class instead ...");
        }
        try {
            fragment = targetFragmentClass.newInstance();
            fragment.setMenuData(targetMenuData);

        } catch (InstantiationException e) {
            Logger.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Logger.e(TAG, e.getMessage());
        } catch (ClassCastException e) {
            Logger.e(TAG, targetMenuData.getFragmentClass().getSimpleName() +" "+ e.getMessage());
        }

        switchFragment(fragment);
    }

    private void switchFragment(AbstractBaseFragment targetFragment) {
        BaseMenuData targetMenuData = targetFragment.getMenuData();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String key = targetMenuData.getKey();
        //just clear backStack for homefragment
        if (homeFragmentKey.equals(key)) {
            if(!this.fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    || this.fragmentManager.getBackStackEntryCount() == 0){
                fragmentTransaction.replace(fragmentContainerId, targetFragment, key);
                if(targetMenuData.isAddToBackStack()){
                    fragmentTransaction.addToBackStack(key);
                }
                baseFragmentActivity.setActivityCommonListener(targetFragment);
            };
        }else{
            Fragment cacheFragment =  this.fragmentManager.findFragmentByTag(key);
            baseFragmentActivity.setActivityCommonListener((ActivityCommonListener) cacheFragment);
            // 有弹出该状态栈以上的栈（不包含自身栈）
            boolean hasPoppedBackStack = this.fragmentManager.popBackStackImmediate(key, 0);
            if(!hasPoppedBackStack && (null == cacheFragment)){
                fragmentTransaction.replace(fragmentContainerId, targetFragment, key);
                if(targetMenuData.isAddToBackStack()) {
                    fragmentTransaction.addToBackStack(key);
                }
                baseFragmentActivity.setActivityCommonListener(targetFragment);
            }
        }
        fragmentTransaction.commit();
        AbstractBaseFragment fragment = (AbstractBaseFragment) baseFragmentActivity.getActivityCommonListener();
        navigatorListener.afterSwitchScreen(fragment.getMenuData());
        pushHistory(targetMenuData);
    }


    //获取screen历史的总数
    public int getHistoryStackCount(){
        if(null == this.historyStack){
            return 0;
        }
        return this.historyStack.size();
    }

    //后退到上一个screen
    public boolean popBackScreen(){
        if(this.historyStack.size() <= 1){
            Logger.e(TAG, "historyStack size is less than 1, not allow pop.");
            return false;
        }
        BaseMenuData curBaseMenuData = this.historyStack.elementAt(this.historyStack.size()  - 2);
        switchScreenByMenuData(curBaseMenuData);
        return true;
    }

    //出栈
    private void popHistory(BaseMenuData targetMenuData){
        int index = -1;
        for(int j = 0 ; j < historyStack.size(); j ++){
            BaseMenuData baseMenuData = historyStack.get(j);
            if(null != baseMenuData && baseMenuData.getKey().equals(targetMenuData.getKey())){
                index = j;
                break;
            }
        }

        if(index == -1){
            return;
        }else{
            historyStack.pop();
        }

        popHistory(targetMenuData);
    }

    //入栈
    private BaseMenuData pushHistory(BaseMenuData targetMenuData){
        popHistory(targetMenuData);
        return historyStack.push(targetMenuData);
    }

    public NavigatorListener getNavigatorListener() {
        return navigatorListener;
    }

    public void setNavigatorListener(NavigatorListener navigatorListener) {
        this.navigatorListener = navigatorListener;
    }

    public Stack<BaseMenuData> getHistoryStack() {
        return historyStack;
    }
}
