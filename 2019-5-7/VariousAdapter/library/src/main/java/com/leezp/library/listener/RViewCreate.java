package com.leezp.library.listener;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.leezp.library.base.RViewAdapter;

/**
 * 创建RViewHelper所需要的数据，它的实现类很方便创建RViewHelper对象
 */
public interface RViewCreate<T> {
    /** 创建 SwipeRefresh 下拉 */
    SwipeRefreshLayout createSwipeRefresh();
    /** SwipeRefresh 下拉颜色 */
    int[] colorRes();
    /** 创建RecyclerView */
    RecyclerView createRecyclerView();
    /** 创建RecyclerView.Adapter */
    RViewAdapter<T> createRecyclerViewAdapter();
    /** 创建RecyclerView布局管理器 */
    RecyclerView.LayoutManager createLayoutManager();
    /** 创建RecyclerView分割线 */
    RecyclerView.ItemDecoration createItemDecoration();
    /** 开始页码 */
    int startPageNumber();
    /** 是否支持分页 */
    boolean isSupportPaging();
}
