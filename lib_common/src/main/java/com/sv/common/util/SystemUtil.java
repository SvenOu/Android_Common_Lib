package com.sv.common.util;

//import com.google.android.gms.common.GooglePlayServicesUtil;


import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;

public class SystemUtil {
    private static final String TAG = SystemUtil.class.getName();

    private static final int MIN_CLICK_DELAY_TIME = 1500;
    private static long lastClickTime = 0;
    public static boolean shouldExit(Context context, int tipResId){
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (lastClickTime == 0 || currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            Logger.d(TAG, "currentTime - lastClickTime:" + (currentTime - lastClickTime));
            lastClickTime = currentTime;
            Toast.makeText(context, tipResId, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            Logger.d(TAG, "currentTime - lastClickTime:" + (currentTime - lastClickTime));
            lastClickTime = 0;
            return true;
        }
    }

//    public static void openGooglePlayForApp(Activity activity, String updateUrl) {
//        if(TextUtils.isEmpty(updateUrl)){
//            updateUrl = "http://play.google.com/store/apps/details?id=com.tts.base";
//        }
//        try {
//            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.tts.base"));
//            boolean marketFound = false;
//            final List<ResolveInfo> otherApps = activity.getPackageManager().queryIntentActivities(rateIntent, 0);
//            for (ResolveInfo otherApp : otherApps) {
//                // look for Google Play application
//                if (otherApp.activityInfo.applicationInfo.packageName.equals(GooglePlayServicesUtil.GOOGLE_PLAY_STORE_PACKAGE)) {
//                    ActivityInfo otherAppActivity = otherApp.activityInfo;
//                    ComponentName componentName = new ComponentName(
//                            otherAppActivity.applicationInfo.packageName,
//                            otherAppActivity.name
//                    );
//                    rateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                    rateIntent.setComponent(componentName);
//                    activity.startActivity(rateIntent);
//                    marketFound = true;
//                    break;
//                }
//            }
//            // if GP not present on device, open web browser
//            if (!marketFound) {
//                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)));
//            }
//        } catch (android.content.ActivityNotFoundException anfe) {
//            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)));
//        }
//    }

    /**
     * 版本比较大小，versionA与versionB比较，如果大于则结果为正数，如果小于则结果为负数，相等则结果为0
     * @param versionA
     * @param versionB
     * @return
     * @throws Exception
     */
    public static int compare(String versionA, String versionB){
        // version 格式：x.y.z , 且 x、y、z小于1000
        // update: x、y、z 可以为空，但必须是数字。

        String[] splitListA = versionA.split("\\.");
        String[] splitListB = versionB.split("\\.");

        Integer hashValueA, hashValueB;

        int[] digitA = new int[3];
        int[] digitB = new int[3];

        if(splitListA.length > 0){
            digitA[0] = Integer.parseInt(splitListA[0]);
        } else{
            digitA[0] = 0;
        }
        if(splitListA.length >1){
            digitA[1] = Integer.parseInt(splitListA[1]);
        } else {
            digitA[1] = 0;
        }
        if(splitListA.length>2){
            digitA[2] = Integer.parseInt(splitListA[2]);
        } else {
            digitA[2] = 0;
        }

        if(splitListB.length>0){
            digitB[0] = Integer.parseInt(splitListB[0]);
        } else{
            digitB[0] = 0;
        }
        if(splitListB.length>1){
            digitB[1] = Integer.parseInt(splitListB[1]);
        } else{
            digitB[1] = 0;
        }
        if(splitListB.length>2){
            digitB[2] = Integer.parseInt(splitListB[2]);
        } else{
            digitB[2] = 0;
        }

        hashValueA = digitA[0] * 1000000 + digitA[1] * 1000 + digitA[2] * 1;
        hashValueB = digitB[0] * 1000000 + digitB[1] * 1000 + digitB[2] * 1;

        return hashValueA - hashValueB;
    }

    public static Boolean haveSDCard() {
        Boolean isSDPresent = android.os.Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        if (isSDPresent) {
            return isSDPresent;
        } else {
            return false;
        }

    }
}
