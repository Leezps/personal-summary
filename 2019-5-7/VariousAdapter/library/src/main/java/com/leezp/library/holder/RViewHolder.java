package com.leezp.library.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RViewHolder extends RecyclerView.ViewHolder {

    //所有子控件的集合
    private SparseArray<View> mViews;
    //当前View对象
    private View mConvertView;

    private RViewHolder(@NonNull View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    // 创建 RViewHolder 对象
    public static RViewHolder createViewHolder(Context context, ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new RViewHolder(itemView);
    }

    // 获取当前View
    public View getmConvertView() {
        return mConvertView;
    }

    // 通过R.id.XXX获取某个控件
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            // 键：R.id.xxx   值：TextView ImageView
            mViews.put(viewId,view);
        }
        return (T) view;
    }
}
