package com.leezp.animation_framework.views;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.leezp.animation_framework.views.interfaces.DiscrollInterface;

public class LifeFrameLayout extends FrameLayout implements DiscrollInterface {
    // 平移的自定义属性
    /**
     * xml文件中的top|left 其实是top与left进行或预算
     * 0001 | 0100 = 0101
     * 0101 & 0001 = 0001 通过与运算，提取出top值
     */
    public static final int TRANSLATION_FROM_TOP = 0x01;
    public static final int TRANSLATION_FROM_BOTTOM = 0x02;
    public static final int TRANSLATION_FROM_LEFT = 0x04;
    public static final int TRANSLATION_FROM_RIGHT = 0x08;

    // 颜色估值器
    private static ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();

    private boolean mDiscrollveAlpha; //是否需要透明度动画
    private boolean mDiscrollveScaleX; //是否需要x轴方向缩放
    private boolean mDiscrollveScaleY; //是否需要y轴方向缩放
    private int mDiscrollveTranslation; //平移值
    private int mDiscrollveFromBgColor; //背景颜色变化开始值
    private int mDiscrollveToBgColor; //背景颜色变化结束值
    private int mHeight; //view的高度
    private int mWidth; //view的宽度

    public void setmDiscrollveAlpha(boolean mDiscrollveAlpha) {
        this.mDiscrollveAlpha = mDiscrollveAlpha;
    }

    public void setmDiscrollveScaleX(boolean mDiscrollveScaleX) {
        this.mDiscrollveScaleX = mDiscrollveScaleX;
    }

    public void setmDiscrollveScaleY(boolean mDiscrollveScaleY) {
        this.mDiscrollveScaleY = mDiscrollveScaleY;
    }

    public void setmDiscrollveTranslation(int mDiscrollveTranslation) {
        this.mDiscrollveTranslation = mDiscrollveTranslation;
    }

    public void setmDiscrollveFromBgColor(int mDiscrollveFromBgColor) {
        this.mDiscrollveFromBgColor = mDiscrollveFromBgColor;
    }

    public void setmDiscrollveToBgColor(int mDiscrollveToBgColor) {
        this.mDiscrollveToBgColor = mDiscrollveToBgColor;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    public LifeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDiscroll(float ratio) {
        // 判断是否有动画的属性，开启动画
        if (mDiscrollveAlpha) {
            setAlpha(ratio);
        }
        if (mDiscrollveScaleX) {
            setScaleX(ratio);
        }
        if (mDiscrollveScaleY) {
            setScaleY(ratio);
        }
        // 判断平移 0代表view本该处于的位置
        if (isTranslationFrom(TRANSLATION_FROM_BOTTOM)) {
            setTranslationY(mHeight*(1-ratio));
        }
        if (isTranslationFrom(TRANSLATION_FROM_TOP)) {
            setTranslationY(-mHeight*(1-ratio));
        }
        if(isTranslationFrom(TRANSLATION_FROM_LEFT)) {
            setTranslationX(-mWidth*(1-ratio));
        }
        if (isTranslationFrom(TRANSLATION_FROM_RIGHT)) {
            setTranslationX(mWidth*(1-ratio));
        }
        // 颜色渐变
        if (mDiscrollveFromBgColor != -1 && mDiscrollveToBgColor != -1) {
            setBackgroundColor((int) sArgbEvaluator.evaluate(ratio, mDiscrollveFromBgColor,mDiscrollveToBgColor));
        }
    }

    private boolean isTranslationFrom(int translationMask) {
        if (mDiscrollveTranslation == -1) {
            return false;
        }
        return (mDiscrollveTranslation & translationMask) == translationMask;
    }

    @Override
    public void onResetDiscroll() {
        // 控件重置
        int ratio = 0;
        if (mDiscrollveAlpha) {
            setAlpha(ratio);
        }
        if (mDiscrollveScaleX) {
            setScaleX(ratio);
        }
        if (mDiscrollveScaleY) {
            setScaleY(ratio);
        }
        // 判断平移 0代表view本该处于的位置
        if (isTranslationFrom(TRANSLATION_FROM_BOTTOM)) {
            setTranslationY(mHeight*(1-ratio));
        }
        if (isTranslationFrom(TRANSLATION_FROM_TOP)) {
            setTranslationY(-mHeight*(1-ratio));
        }
        if(isTranslationFrom(TRANSLATION_FROM_LEFT)) {
            setTranslationX(-mWidth*(1-ratio));
        }
        if (isTranslationFrom(TRANSLATION_FROM_RIGHT)) {
            setTranslationX(mWidth*(1-ratio));
        }
    }
}
