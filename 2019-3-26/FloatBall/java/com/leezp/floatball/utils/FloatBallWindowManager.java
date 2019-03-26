package com.leezp.floatball.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.WindowManager;

import com.leezp.floatball.view.FloatBallView;

public class FloatBallWindowManager {
    private FloatBallView mFloatBallView;
    private WindowManager.LayoutParams mFloatBallParams;
    private WindowManager mWindowManager;

    private static class SingletonHolder {
        private static final FloatBallWindowManager INSTANCE = new FloatBallWindowManager();
    }

    /**
     * 单例模式
     */
    public static FloatBallWindowManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 创建悬浮球
     */
    public void createFloatBallWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;
        if (mFloatBallView == null) {
            mFloatBallView = new FloatBallView(context);
            if (mFloatBallParams == null) {
                mFloatBallParams = new WindowManager.LayoutParams();
//                mFloatBallParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                mFloatBallParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
                //must need
                mFloatBallParams.token = ((Activity) context).getWindow().getDecorView().getWindowToken();

                mFloatBallParams.format = PixelFormat.RGBA_8888;
                mFloatBallParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                mFloatBallParams.gravity = Gravity.START | Gravity.TOP;
                mFloatBallParams.width = FloatBallView.viewWidth;
                mFloatBallParams.height = FloatBallView.viewHeight;
                mFloatBallParams.x = screenWidth - FloatBallView.viewWidth;
                mFloatBallParams.y = (screenHeight - FloatBallView.viewHeight) / 2;
            }
            if (mFloatBallParams.x > screenWidth / 2) {
                mFloatBallParams.x = screenWidth - FloatBallView.viewWidth;
            } else {
                mFloatBallParams.x = 0;
            }
            mFloatBallView.setParams(mFloatBallParams);
            windowManager.addView(mFloatBallView, mFloatBallParams);
        }
    }

    /**
     * 移除悬浮球
     */
    public void removeFloatBallWindow(Context context) {
        if (mFloatBallView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(mFloatBallView);
            mFloatBallView = null;
        }
    }

    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        }
        return mWindowManager;
    }

    /**
     * 释放内存
     */
    public void release(Context context) {
        removeFloatBallWindow(context);
        if (mFloatBallParams != null) mFloatBallParams = null;
        if (mWindowManager != null) mWindowManager = null;
    }
}
