package com.leezp.library.listener;

import android.view.View;

/**
 * item点击、长按监听接口
 */
public interface ItemListener<T> {

    /** item点击事件监听 */
    void onItemClick(View view,T entity,int position);

    /** item长按事件监听 */
    boolean onItemLongClick(View view,T entity,int position);
}
