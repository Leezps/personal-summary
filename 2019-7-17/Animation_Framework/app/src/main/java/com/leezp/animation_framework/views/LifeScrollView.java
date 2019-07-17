package com.leezp.animation_framework.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.leezp.animation_framework.views.interfaces.DiscrollInterface;

public class LifeScrollView extends ScrollView {
    LifeLinearLayout mContent;
    public LifeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = (LifeLinearLayout) getChildAt(0);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int scrollviewHeight = getHeight();
        // 监听滑动状态--->childView从下面冒出来多少/childView.getHeight() = 动画的执行百分比ratio
        // 拿到里面每个子控件，让他们按照ratio动起来
        for (int i = 0; i < mContent.getChildCount(); i++) {
            View child = mContent.getChildAt(i);
            if (child instanceof LifeFrameLayout) {
                DiscrollInterface discrollInterface = (DiscrollInterface) child;
                // child 离 parent 顶部的高度
                int childTop = child.getTop();
                int childHeight = child.getHeight();
                // t:滑出的高度
                // child 离屏幕顶部的高度
                int absoluteTop = (childTop - t);
                if (absoluteTop <= scrollviewHeight) {
                    // child 浮现的高度 = scrollview的高度 - child离屏幕的高度
                    int visibleGap = scrollviewHeight - absoluteTop;
                    // 百分比 = child 浮现的高度/child的高度
                    float ratio = visibleGap/(float)childHeight;
                    // clamp 确保 ratio 在0-1这个范围
                    discrollInterface.onDiscroll(clamp(ratio,1f,0f));
                } else {
                    discrollInterface.onResetDiscroll();
                }
            }
        }
    }

    // 求三个数的中间大小的一个数
    private float clamp(float value, float max, float min) {
        return Math.max(Math.min(value,max),min);
    }

}
