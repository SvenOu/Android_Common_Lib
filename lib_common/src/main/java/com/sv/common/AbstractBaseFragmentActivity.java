package com.sv.common;

import android.content.Context;
import android.location.Location;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.kayvannj.permission_utils.Func;
import com.github.kayvannj.permission_utils.PermissionUtil;
import com.sv.common.screen_navigation.ActivityCommonListener;
import com.sv.common.screen_navigation.BaseMenuData;
import com.sv.common.screen_navigation.ScreenNavigator;
import com.sv.common.util.Logger;
import com.sv.common.view.AsyncTaskProgressDialog;

import java.util.LinkedList;
import java.util.Queue;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
/*
	BaseFragmentActivity 必须与 BaseFragment 绑到一起才能使用
*/
public abstract class AbstractBaseFragmentActivity extends AppCompatActivity {
	private static final String TAG = AbstractBaseFragmentActivity.class.getSimpleName();
	/**
	 * 位于当前activity顶部的View (实现了监听接口)
	 */
	private ActivityCommonListener activityCommonListener;
	protected ScreenNavigator screenNavigator = new ScreenNavigator();
	protected AsyncTaskProgressDialog progressDialog;
	protected PermissionUtil.PermissionRequestObject mRequestObject;
	protected Queue<Runnable> switchFragmentTasks = new LinkedList<>();
	protected boolean isVisible = false;

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(layoutResID);
		initContentView();
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		initContentView();
	}

	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		initContentView();
	}

	private void initContentView() {
		//must behind setContentView()
		ButterKnife.bind(this);
	}

	private void executeSwitchFragmentTasks() {
		while (!switchFragmentTasks.isEmpty()) {
			Runnable runnable = switchFragmentTasks.remove();
			Logger.w(TAG, "executeResumeTasks: " + runnable.toString());
			runnable.run();
		}
	}

	public void switchFragmentByMenuData(final BaseMenuData targetMenuData) {
		if (isVisible) {
			screenNavigator.switchScreenByMenuData(targetMenuData);
		}else{
			switchFragmentTasks.offer(new Runnable() {
				@Override
				public void run() {
					Logger.w(TAG, "activity not in foreground, delay run in onResume.");
					screenNavigator.switchScreenByMenuData(targetMenuData);
				}
			});
		}
	}

	//--------------Permissions--------------------//
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(null != mRequestObject){
			mRequestObject.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	// 提供给子类调用,适用于单个或多个permission的简单请求（https://github.com/kayvannj/PermissionUtil）
	protected void doSimpleRequestPermission(String[] permissionStr, int requestCode, Func
			onAllGranted, Func onAnyDenied) {
		mRequestObject = PermissionUtil.with(this)
				.request(permissionStr)
				.onAllGranted(onAllGranted)
				.onAnyDenied(onAnyDenied)
				.ask(requestCode);
	}
	//---------------------Permissions end-------------------//

	/**
	 * for setFonts，Calligraphy配置
	 */
	@Override
	protected void attachBaseContext(Context newBase) {
		super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
	}

	@Override
	public void onPause() {
		super.onPause();
		isVisible = false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		isVisible = true;
		executeSwitchFragmentTasks();
	}

	@Override
	public void onBackPressed() {
		if(!screenNavigator.popBackScreen()){
//			super.onBackPressed();
			// TODO: 2019/2/2  
			finish();
		}
	}

	public void showLoadingDialog() {
		iniDialog();
		try {
			progressDialog.show();
		} catch (Exception e) {
			Logger.e(TAG,e.getMessage());
		}
	}

	public void cancelLoadingDialog() {
		iniDialog();
		try {
			progressDialog.cancel();
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage());
		}
	}

	private void iniDialog() {
		if (null == progressDialog){
			progressDialog = new AsyncTaskProgressDialog(this);
		}
	}
	protected void showToast(int resId){
		Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
	}
	protected void showToast(String resStr){
		Toast.makeText(this, resStr, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		Logger.i(TAG, "onDestroy");
		if (null != progressDialog && progressDialog.isShowing()){
			progressDialog.dismiss();
		}
		screenNavigator.unBind(this);
		super.onDestroy();
	}

	public ActivityCommonListener getActivityCommonListener() {
		return activityCommonListener;
	}

	public void setActivityCommonListener(ActivityCommonListener activityCommonListener) {
		this.activityCommonListener = activityCommonListener;
	}

	public ScreenNavigator getScreenNavigator() {
		return screenNavigator;
	}

	public void setScreenNavigator(ScreenNavigator screenNavigator) {
		this.screenNavigator = screenNavigator;
	}
}