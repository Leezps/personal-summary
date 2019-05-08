package com.leezp.library;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.leezp.library.base.RViewAdapter;
import com.leezp.library.listener.RViewCreate;

import java.util.List;

public class RViewHelper<T> {
    private SwipeRefreshLayout swipeRefresh;    //  下拉控件
    private SwipeRefreshHelper swipeRefreshHelper;  //  下拉刷新的工具类
    private RecyclerView recyclerView;  //  RecyclerView
    private RViewAdapter<T> adapter;    //  适配器
    private RecyclerView.LayoutManager layoutManager;   //  布局管理
    private RecyclerView.ItemDecoration itemDecoration; //  条目分割
    private int startPageNumber;    //  开始页码
    private boolean isSupportPaging;    //  是否支持加载更多
    private SwipeRefreshHelper.SwipeRefreshListener listener;   //  下拉刷新，加载更多监听
    private int currentPageNum; //  当前页数

    private RViewHelper(Builder<T> builder) {
        this.swipeRefresh = builder.create.createSwipeRefresh();
        this.recyclerView = builder.create.createRecyclerView();
        this.adapter = builder.create.createRecyclerViewAdapter();
        this.layoutManager = builder.create.createLayoutManager();
        this.itemDecoration = builder.create.createItemDecoration();
        this.startPageNumber = builder.create.startPageNumber();
        this.isSupportPaging = builder.create.isSupportPaging();
        this.listener = builder.listener;

        this.currentPageNum = this.startPageNumber;
        int[] colorRes = builder.create.colorRes();
        if (swipeRefresh != null) {
            if (colorRes == null) {
                swipeRefreshHelper = SwipeRefreshHelper.createSwipeRefreshHelper(swipeRefresh);
            } else {
                swipeRefreshHelper = SwipeRefreshHelper.createSwipeRefreshHelper(swipeRefresh, colorRes);
            }
        }
        init();
    }

    private void init() {
        //  RecyclerView 初始化
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (itemDecoration != null) recyclerView.addItemDecoration(itemDecoration);

        // 下拉刷新的操作
        if (swipeRefreshHelper != null) {
            swipeRefreshHelper.setSwipeRefreshListener(new SwipeRefreshHelper.SwipeRefreshListener() {
                @Override
                public void onRefresh() {
                    //  下拉刷新是否 == 重新加载？
                    currentPageNum = startPageNumber;   //重置页码
                    dismissSwipeRefresh();
                    if (listener != null) listener.onRefresh();
                }
            });
        }
    }

    private void dismissSwipeRefresh() {
        if (swipeRefresh != null && swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    public void notifyAdapterDataSetChanged(List<T> datas) {
        //首次加载或者下拉刷新后都要重置页码
        if (currentPageNum == startPageNumber) {
            adapter.updateDatas(datas);
        } else {
            adapter.addDatas(datas);
        }
        recyclerView.setAdapter(adapter);

        //省略功能---加载更多、最后一条、空布局
        if (isSupportPaging) {
            Log.e("Leezp >>> ", "更多功能待补充……");
        }
    }

    public static class Builder<T> {
        private RViewCreate<T> create;  //  初始化接口
        private SwipeRefreshHelper.SwipeRefreshListener listener;   //  下拉监听

        public Builder(RViewCreate<T> create, SwipeRefreshHelper.SwipeRefreshListener listener) {
            this.create = create;
            this.listener = listener;
        }

        public RViewHelper build() {
            //  参数的校验
            return new RViewHelper<>(this);
        }
    }
}
