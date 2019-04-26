package com.leezp.livedatabus.customize;

import android.support.annotation.Nullable;

public abstract class Observer<T> {
    /**
     * 活跃状态
     */
    static final int STATE_ACTIVE = 1;
    /**
     * 暂停状态
     */
    static final int STATE_ONPAUSE = 2;
    /**
     * 销毁状态
     */
    static final int STATE_DESTORY = 3;
    /**
     * 初始化状态
     */
    static final int STATE_INIT = 0;

    private int state = STATE_INIT;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    //组件状态
    public abstract void onChanged(@Nullable T t);
}
