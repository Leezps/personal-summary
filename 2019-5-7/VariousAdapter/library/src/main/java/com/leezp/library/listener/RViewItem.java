package com.leezp.library.listener;

import com.leezp.library.holder.RViewHolder;

/**
 * 某一类的item对象
 */
public interface RViewItem<T> {

    // 获取item的布局
    int getItemLayout();

    // 是否开启点击
    boolean openClick();

    // 是否为当前的item布局
    boolean isItemView(T entity, int position);

    // 将item的控件与需要显示数据绑定
    void convert(RViewHolder holder, T entity, int position);
}
