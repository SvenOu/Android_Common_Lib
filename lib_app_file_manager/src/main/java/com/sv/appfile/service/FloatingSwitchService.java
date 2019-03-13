package com.sv.appfile.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sv.appfile.R;
import com.sv.appfile.componet.AndroidWebServer;
import com.sv.appfile.componet.TouchMoveListener;
import com.sv.appfile.service.impl.FileServiceImpl;

import fi.iki.elonen.NanoHTTPD;

public class FloatingSwitchService extends Service implements View.OnClickListener {
    private static final String TAG = FloatingSwitchService.class.getName();

    private String applicationId;
    private int serverPort;

    // INSTANCE OF ANDROID WEB SERVER
    private AndroidWebServer androidWebServer;
    private FileService fileService;

    // VIEW
    private FrameLayout rootFrameLayout;
    private FloatingActionButton floatingActionButtonOnOff;
    private View textViewMessage;
    private TextView textViewIpAccess;

    private WindowManager mWindowManager;
    private View mFloatingView;
    private View collapsedView;
    private View expandedView;
    private ImageView closeImageIcon;
    private RelativeLayout rootContainer;
    private Button btnBack;
    private ImageView btnExpandView;

    public FloatingSwitchService(){}

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate();
        fileService = new FileServiceImpl(this);
        bindViews();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            Log.e(TAG, "intent is null, stopSelf");
            stopSelf();
        }
        applicationId = intent.getStringExtra("applicationId");
        serverPort = intent.getIntExtra("serverPort",
                AndroidWebServer.SERVER_PORT);
        AndroidWebServer.SERVER_PORT = serverPort;
        initViews();
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initViews() {
        androidWebServer = AndroidWebServer.getInstance().init(getApplication(),
                new AndroidWebServer.Listener() {
                    @Override
                    public void onNetChange(Context context, Intent intent) {
                        setIpAccess();
                    }

                    @Override
                    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
                        return fileService.handerUri(applicationId, session);
                    }
                    @Override
                    public void onStartAndroidWebServer() {}
                    @Override
                    public void onStopAndroidWebServer() {}
                });

        setIpAccess();

        floatingActionButtonOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSerer();
            }
        });
        toggleSerer();

        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //Add the view to the window.
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //Drag and move floating view using user's touch action.
        TouchMoveListener listener = new TouchMoveListener(params, new TouchMoveListener.Callback() {
            @Override
            public void onClick(int xdiff, int ydiff) {
                if (isViewCollapsed()) {
                    //When user clicks on the image view of the collapsed layout,
                    //visibility of the collapsed layout will be changed to "View.GONE"
                    //and expanded view will become visible.
                    collapsedView.setVisibility(View.GONE);
                    expandedView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onMove(WindowManager.LayoutParams params) {
                //Update the layout with new X & Y coordinate
                mWindowManager.updateViewLayout(mFloatingView, params);
            }
        });
        rootContainer.setOnTouchListener(listener);
    }

    private void bindViews() {
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_switch, null);

        //The root element of the collapsed view layout
        collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        expandedView = mFloatingView.findViewById(R.id.expanded_container);
        //Set the close button
        closeImageIcon = (ImageView) mFloatingView.findViewById(R.id.iv_close);
        rootContainer = (RelativeLayout) mFloatingView.findViewById(R.id.root_container);

        btnBack = (Button) mFloatingView.findViewById(R.id.btn_Back);
        btnExpandView = (ImageView) mFloatingView.findViewById(R.id.btn_expandView);

        btnBack.setOnClickListener(this);
        closeImageIcon.setOnClickListener(this);

        rootFrameLayout = mFloatingView.findViewById(R.id.coordinatorLayout);
        textViewMessage = mFloatingView.findViewById(R.id.tv_message);
        textViewIpAccess = mFloatingView.findViewById(R.id.tv_ipAccess);
        floatingActionButtonOnOff = mFloatingView.findViewById(R.id.fbtn_OnOff);
    }

    private void toggleSerer() {
        if (isConnectedInWifi()) {
            if (!androidWebServer.isStarted() && androidWebServer.startAndroidWebServer()) {
                androidWebServer.setStarted(true);
                textViewMessage.setVisibility(View.VISIBLE);
                floatingActionButtonOnOff.setBackgroundTintList(ContextCompat.getColorStateList(FloatingSwitchService.this, R.color.androidServerColorGreen));
            } else if (androidWebServer.stopAndroidWebServer()) {
                androidWebServer.setStarted(false);
                textViewMessage.setVisibility(View.INVISIBLE);
                floatingActionButtonOnOff.setBackgroundTintList(ContextCompat.getColorStateList(FloatingSwitchService.this, R.color.androidServerColorRed));
            }
        } else {
            Snackbar.make(rootFrameLayout, getString(R.string.android_server_wifi_message), Snackbar.LENGTH_LONG).show();
        }
    }

    //region Private utils Method
    private void setIpAccess() {
        textViewIpAccess.setText(getIpAccess());
    }

    private String getIpAccess() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return "http://" + formatedIpAddress + ":" + serverPort + "/"+ applicationId;
    }

    public boolean isConnectedInWifi() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()
                && wifiManager.isWifiEnabled() && networkInfo.getTypeName().equals("WIFI")) {
            return true;
        }
        return false;
    }
    //endregion

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_expandView) {
            collapsedView.setVisibility(View.GONE);
            expandedView.setVisibility(View.VISIBLE);
        }
        else if(id == R.id.btn_Back) {
            collapsedView.setVisibility(View.VISIBLE);
            expandedView.setVisibility(View.GONE);
        }
        else if(id == R.id.iv_close){
            //close the service and remove the from from the window
            stopSelf();
        }
    }
    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null ||
                collapsedView.getVisibility() == View.VISIBLE;
    }


    @Override
    public void onDestroy() {
        if(androidWebServer != null){
            androidWebServer.stopAndroidWebServer();
            androidWebServer.destoryInstance();
        }
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
        }
        super.onDestroy();
    }
}