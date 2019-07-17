package com.leezp.animation_framework.views.interfaces;

public interface DiscrollInterface {
    /**
     * 当滑动的时候调用该方法，用来控制里面的控件执行动画
     * @param ratio 完成动画的百分比
     */
    void onDiscroll(float ratio);

    /**
     * 重置动画
     */
    void onResetDiscroll();
}
