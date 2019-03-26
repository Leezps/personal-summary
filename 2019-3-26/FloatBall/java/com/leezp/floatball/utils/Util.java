package com.leezp.floatball.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Util {
    private static int statusBarHeight = -1;

    /**
     * dp转换成pix
     * @param dp dp
     * @return pix
     */
    public static int dpToPx(int dp) {
        return ((int) (Resources.getSystem().getDisplayMetrics().density * dp + 0.5f));
    }

//    /**
//     * pix转换成dp
//     * @param px pix
//     * @return dp
//     */
//    public static int px2dp(int px){
//        float density = Resources.getSystem().getDisplayMetrics().density;
//        return (int) (px / density + 0.5f);
//    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight < 0) {
            if ((((Activity) context).getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                statusBarHeight = 0;
            } else {
                int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
                }
            }
        }
        return statusBarHeight;
    }

    /**
     * 读取raw中的文件
     */
    public static String readRawFile(Context context, int rawId) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.getResources().openRawResource(rawId));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String result;
            while ((result = reader.readLine()) != null) {
                stringBuffer.append(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
