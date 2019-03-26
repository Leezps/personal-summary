package com.leezp.floatball.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leezp.floatball.R;
import com.leezp.floatball.listener.FloatBallMenuListener;
import com.leezp.floatball.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FloatBallView extends FrameLayout implements View.OnClickListener {
    //悬浮窗的窗口管理器，用于更新悬浮球的位置
    private final WindowManager windowManager;
    //悬浮窗的布局参数
    private WindowManager.LayoutParams mParams;
    //悬浮球的ImageView
    private final ImageView floatBallImg;
    //长时间不动悬浮球的定时处理任务
    private final FloatBallViewHandler mHandler;
    //悬浮球的宽度、高度
    public static int viewWidth;
    public static int viewHeight;
    //屏幕的宽度、高度
    private final int screenWidth;
    private final int screenHeight;
    //当前手指在悬浮球的x、y坐标
    private float xInView;
    private float yInView;
    //手指在按在屏幕上时的x、y坐标
    private float xDownInScreen;
    private float yDownInScreen;
    //当前手指在屏幕上的x、y坐标
    private float xInScreen;
    private float yInScreen;
    //当前悬浮窗是否是悬浮球状态
    private boolean isFloatBall;
    //当前悬浮窗为左边打开菜单界面时的内容布局
    private LinearLayout mLeftContentLayout;
    //当前悬浮窗为右边打开菜单界面时的内容布局
    private LinearLayout mRightContentLayout;
    //菜单界面的按钮点击事件
    private FloatBallMenuListener menuListener;
    //菜单界面的宽度
    private int menuWidth;

    //---------------------------------  以下是初始化相关函数  --------------------------------------//
    public FloatBallView(Context context) {
        super(context);
        //获取窗口管理器以及屏幕宽度和高度
        windowManager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        Point size = new Point();
        windowManager.getDefaultDisplay().getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        //将悬浮球添加到悬浮窗中
        floatBallImg = new ImageView(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(Util.dpToPx(50), Util.dpToPx(50));
        params.gravity = Gravity.START | Gravity.TOP;
        floatBallImg.setLayoutParams(params);
        floatBallImg.setImageResource(R.mipmap.channel_img_zulong);
        floatBallImg.setScaleType(ImageView.ScaleType.FIT_XY);
        addView(floatBallImg);

        //获取悬浮球的宽、高
        viewWidth = floatBallImg.getLayoutParams().width;
        viewHeight = floatBallImg.getLayoutParams().height;

        //初始化悬浮球的长时间闲置的定时任务
        mHandler = new FloatBallViewHandler(this);
        isFloatBall = true;

        //初始化左右打开菜单界面的内容布局
        List<Map<String, String>> buttonList = getButtonList();
        menuListener = new FloatBallMenuListener(this);
        initContentLayout(buttonList);
    }

    /**
     * 初始化菜单界面的内容布局
     */
    private void initContentLayout(List<Map<String, String>> list) {
        //初始化左边打开菜单界面的内容布局
        mLeftContentLayout = new LinearLayout(getContext());
        mLeftContentLayout.setOrientation(LinearLayout.HORIZONTAL);
        mLeftContentLayout.setGravity(Gravity.CENTER_VERTICAL);
        for (int i = list.size() - 1; i >= 0; i--) {
            Map<String, String> param = list.get(i);
            TextView button = new TextView(getContext());
            button.setBackgroundResource(R.drawable.float_ball_menu_button_selector);
            button.setGravity(Gravity.CENTER);
            button.setSingleLine();
            button.setText(param.get("name"));
            button.setTextSize(Util.dpToPx(5));
            button.setTextColor(Color.WHITE);
            button.setTag(param.get("tag"));
            button.setOnClickListener(menuListener);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = Util.dpToPx(5);
            mLeftContentLayout.addView(button, layoutParams);
        }
        ImageView floatBallLeftView = new ImageView(getContext());
        ViewGroup.LayoutParams leftParams = new ViewGroup.LayoutParams(viewWidth, viewHeight);
        floatBallLeftView.setLayoutParams(leftParams);
        floatBallLeftView.setImageResource(R.mipmap.channel_img_zulong);
        floatBallLeftView.setScaleType(ImageView.ScaleType.FIT_XY);
        floatBallLeftView.setOnClickListener(this);
        mLeftContentLayout.addView(floatBallLeftView);

        //初始化右边打开菜单界面的内容布局
        mRightContentLayout = new LinearLayout(getContext());
        mRightContentLayout.setOrientation(LinearLayout.HORIZONTAL);
        mRightContentLayout.setGravity(Gravity.CENTER_VERTICAL);
        ImageView floatBallRightView = new ImageView(getContext());
        ViewGroup.LayoutParams rightParams = new ViewGroup.LayoutParams(viewWidth, viewHeight);
        floatBallRightView.setLayoutParams(rightParams);
        floatBallRightView.setImageResource(R.mipmap.channel_img_zulong);
        floatBallRightView.setScaleType(ImageView.ScaleType.FIT_XY);
        floatBallRightView.setOnClickListener(this);
        mRightContentLayout.addView(floatBallRightView);
        for (Map<String, String> param : list) {
            TextView button = new TextView(getContext());
            button.setBackgroundResource(R.drawable.float_ball_menu_button_selector);
            button.setGravity(Gravity.CENTER);
            button.setSingleLine();
            button.setText(param.get("name"));
            button.setTextSize(Util.dpToPx(5));
            button.setTextColor(Color.WHITE);
            button.setTag(param.get("tag"));
            button.setOnClickListener(menuListener);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.rightMargin = Util.dpToPx(5);
            mRightContentLayout.addView(button, layoutParams);
        }
    }

    /**
     * 获取按钮参数的集合
     */
    private List<Map<String, String>> getButtonList() {
        List<Map<String, String>> buttonList = new ArrayList<>();
        try {
            String json = Util.readRawFile(getContext(), getResources().getIdentifier("float_ball_menu_buttons", "raw", getContext().getPackageName()));
            JSONObject object = new JSONObject(json);
            JSONArray buttons = object.optJSONArray("buttons");
            for (int i = 0; i < buttons.length(); i++) {
                HashMap<String, String> map = new HashMap<>();
                JSONObject buttonJSONObject = buttons.optJSONObject(i);
                String buttonName = buttonJSONObject.optString("name", "无");
                String buttonTag = buttonJSONObject.optString("tag", "-1");
                map.put("name", buttonName);
                map.put("tag", buttonTag);
                buttonList.add(map);
            }
        } catch (JSONException e) {
            Toast.makeText(getContext(), "悬浮框的按钮集合解析有误，请联系技术客服支持", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return buttonList;
    }

    /**
     * 将悬浮球的参数传入，用于更新悬浮球的位置
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
        startHideViewTask();
    }
    //---------------------------------  以上是初始化相关函数  --------------------------------------//

    //---------------------------------  以下是悬浮球操作的相关函数以及类  --------------------------------------//

    /**
     * 点击悬浮球打开菜单界面
     */
    private void openMenuWindow() {
        isFloatBall = false;
        //移除悬浮球
        removeView(floatBallImg);
        //将悬浮框的大小设置为菜单界面布局时的大小
        mParams.width = getMenuWidth();
        windowManager.updateViewLayout(this, mParams);
        //加入菜单内容
        if (mParams.x < screenWidth / 2) {
            addView(mLeftContentLayout);
        } else {
            addView(mRightContentLayout);
        }
        showMenuView();
    }

    /**
     * 修改悬浮球的位置
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    /**
     * 改变悬浮球的显示状态（整个球、半个球）
     *
     * @param leftMargin 悬浮球图片相对于父布局的左边距，左边距为-viewWidth/2，则显示右半球，左边距为0，则显示全球，左边距为viewWidth/2，则显示左半球
     */
    public void changeViewStatus(final int leftMargin) {
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) floatBallImg.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(params.leftMargin, leftMargin);
        animator.setDuration(100);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                params.leftMargin = (int) animation.getAnimatedValue();
                floatBallImg.setLayoutParams(params);
            }
        });
        animator.start();
    }

    /**
     * 启动隐藏悬浮球任务
     */
    private void startHideViewTask() {
        Message message = Message.obtain();
        message.what = FloatBallViewHandler.LACK;
        if (mParams.x == 0) {
            message.arg1 = -viewWidth / 2;
        } else {
            message.arg1 = viewWidth / 2;
        }
        mHandler.sendMessageDelayed(message, 3000);
    }

    /**
     * 移除隐藏悬浮球的任务并使悬浮球恢复
     */
    private void removeHideViewTask() {
        Message message = Message.obtain();
        message.what = FloatBallViewHandler.RESTORE;
        mHandler.sendMessage(message);
    }

    /**
     * 控制悬浮球的淡化、显示全球、显示半球的状态
     */
    private static class FloatBallViewHandler extends Handler {
        private final WeakReference<FloatBallView> mFloatBallView;
        private static final int LACK = 1000;
        private static final int FOGGY = 1001;
        private static final int RESTORE = 1002;

        private FloatBallViewHandler(FloatBallView mFloatBallView) {
            this.mFloatBallView = new WeakReference<>(mFloatBallView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FloatBallView floatBallView = mFloatBallView.get();
            switch (msg.what) {
                case LACK:
                    floatBallView.changeViewStatus(msg.arg1);
                    Message message = Message.obtain();
                    message.what = FOGGY;
                    this.sendMessageDelayed(message, 2000);
                    break;
                case FOGGY:
                    floatBallView.animate().alpha(0.2f).setDuration(1000);
                    break;
                case RESTORE:
                    this.removeMessages(LACK);
                    this.removeMessages(FOGGY);
                    floatBallView.animate().alpha(1f).setDuration(100);
                    floatBallView.changeViewStatus(0);
                    break;
                default:
                    break;
            }
        }
    }
    //---------------------------------  以上是悬浮球操作的相关函数以及类  --------------------------------------//

    //---------------------------------  以下是悬浮框菜单界面操作的相关函数以及类  --------------------------------------//

    /**
     * 获取当前悬浮框菜单界面的宽度
     *
     * @return 宽度
     */
    public int getMenuWidth() {
        if (menuWidth <= 0) {
            //左右打开的菜单界面布局的宽度一致，所有获取左边打开的菜单界面的宽度
            int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            mLeftContentLayout.measure(w, h);
            menuWidth = mLeftContentLayout.getMeasuredWidth();
        }
        return menuWidth;
    }

    /**
     * 显示悬浮框的菜单界面动画
     */
    public void showMenuView() {
        ValueAnimator animator;
        if (mParams.x < screenWidth / 2) {
            animator = ValueAnimator.ofInt(viewWidth - getMenuWidth(), 0);
        } else {
            animator = ValueAnimator.ofInt(getMenuWidth() - viewWidth, 0);
        }
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mParams.x < screenWidth / 2) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mLeftContentLayout.getLayoutParams();
                    params.leftMargin = ((int) animation.getAnimatedValue());
                    mLeftContentLayout.setLayoutParams(params);
                } else {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRightContentLayout.getLayoutParams();
                    params.leftMargin = ((int) animation.getAnimatedValue());
                    mRightContentLayout.setLayoutParams(params);
                }
            }
        });
        animator.start();
    }

    /**
     * 隐藏悬浮框的菜单界面动画
     */
    public void hideMenuView() {
        ValueAnimator animator;
        if (mParams.x < screenWidth / 2) {
            animator = ValueAnimator.ofInt(0, viewWidth - getMenuWidth());
        } else {
            animator = ValueAnimator.ofInt(0, getMenuWidth() - viewWidth);
        }
        animator.setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mParams.x < screenWidth / 2) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mLeftContentLayout.getLayoutParams();
                    params.leftMargin = ((int) animation.getAnimatedValue());
                    mLeftContentLayout.setLayoutParams(params);
                } else {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRightContentLayout.getLayoutParams();
                    params.leftMargin = ((int) animation.getAnimatedValue());
                    mRightContentLayout.setLayoutParams(params);
                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mParams.x < screenWidth / 2) {
                    mParams.x = 0;
                    removeView(mLeftContentLayout);
                } else {
                    mParams.x = screenWidth - viewWidth;
                    removeView(mRightContentLayout);
                }
                //将悬浮框的大小设置为悬浮球时的大小
                mParams.width = viewWidth;
                windowManager.removeView(FloatBallView.this);
                windowManager.addView(FloatBallView.this, mParams);
                //以下方式虽然更改了悬浮窗的属性，但UI视图没有变化，因为UI更新会检测自身的视图是否发生变化，如果视图不可见，属性即使变化了，UI也不会改变
//                windowManager.updateViewLayout(FloatBallView.this, mParams);
//                setLayoutParams(mParams);
                isFloatBall = true;
                addView(floatBallImg);
                startHideViewTask();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    @Override
    public void onClick(View v) {
        if (!isFloatBall) {
            hideMenuView();
        }
    }
    //---------------------------------  以上是悬浮框菜单界面操作的相关函数以及类  --------------------------------------//


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isFloatBall) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    xInView = event.getX();
                    yInView = event.getY();
                    xDownInScreen = event.getRawX();
                    yDownInScreen = event.getRawY();
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY();
                    removeHideViewTask();
                    break;
                case MotionEvent.ACTION_MOVE:
                    xInScreen = event.getRawX();
                    yInScreen = event.getRawY();
                    //当前手指在屏幕上的x坐标小于在控件的x坐标或大于控件在屏幕最右侧时的坐标需做如下处理
                    if (xInScreen < xInView) {
                        xInScreen = xInView;
                    } else if (xInScreen > screenWidth - viewWidth + xInView) {
                        xInScreen = screenWidth - viewWidth + xInView;
                    }
                    //y坐标做x坐标同样处理并加上最上方时的状态栏处理
                    if (yInScreen < yInView + Util.getStatusBarHeight(getContext())) {
                        yInScreen = yInView + Util.getStatusBarHeight(getContext());
                    } else if (yInScreen > screenHeight - viewHeight + yInView) {
                        yInScreen = screenHeight - viewHeight + yInView;
                    }
                    //手指移动更新悬浮球位置
                    updateViewPosition();
                    break;
                case MotionEvent.ACTION_UP:
                    if (Math.abs(xDownInScreen - xInScreen) < 50 && Math.abs(yDownInScreen - yInScreen) < 50) {
                        openMenuWindow();
                    } else {
                        float xInScreenStart = xInScreen;
                        float xInScreenEnd;
                        if (xInScreen < screenWidth / 2) {
                            xInScreenEnd = xInView;
                        } else {
                            xInScreenEnd = screenWidth - viewWidth + xInView;
                        }
                        ValueAnimator animator = ValueAnimator.ofFloat(xInScreenStart, xInScreenEnd);
                        animator.setDuration(200);
                        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                xInScreen = ((float) animation.getAnimatedValue());
                                updateViewPosition();
                            }
                        });
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                startHideViewTask();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animator.start();
                    }
                    break;
                default:
                    break;
            }
            return true;
        } else {
            return false;
        }
    }
}
