package com.leezp.library.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.leezp.library.holder.RViewHolder;
import com.leezp.library.listener.ItemListener;
import com.leezp.library.listener.RViewItem;
import com.leezp.library.manager.RViewItemManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 加入条目点击阻塞
 */
public class RViewAdapter<T> extends RecyclerView.Adapter<RViewHolder> {

    private RViewItemManager<T> itemStyle;  //  item类型管理器
    private ItemListener<T> itemListener;   //  item条目点击监听
    private List<T> datas;  //  数据源
    private final static long QUICK_EVENT_TIME_SPAN = 1000; // 阻塞事件
    private long lastClickTime;

    //单样式
    public RViewAdapter(List<T> datas) {
        if (datas == null) {
            this.datas = new ArrayList<>();
        } else {
            this.datas = datas;
        }
        itemStyle = new RViewItemManager<>();
    }

    //多样式
    public RViewAdapter(List<T> datas, RViewItem<T> item) {
        if (datas == null) {
            this.datas = new ArrayList<>();
        } else {
            this.datas = datas;
        }
        itemStyle = new RViewItemManager<>();
        //  将item类型加入
        addItemStyles(item);
    }

    /**
     * 添加数据集合
     */
    public void addDatas(List<T> datas) {
        if (datas == null) return;
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 修改整个数据集合
     */
    public void updateDatas(List<T> datas) {
        if(datas == null) return;
        this.datas = datas;
        notifyDataSetChanged();
    }

    //判断当前Adapter是否是多样式
    private boolean hasMultiStyle() {
        return itemStyle.getItemViewStyleCount() > 0;
    }

    /**
     * 根据position获取当前Item布局类型
     *
     * @param position 当前位置
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        //如果是多样式，需要判断
        if (hasMultiStyle()) {
            return itemStyle.getItemViewType(datas.get(position), position);
        }
        return super.getItemViewType(position);
    }

    //根据布局类型创建不同Item的ViewHolder
    @NonNull
    @Override
    public RViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RViewItem item = itemStyle.getRViewItem(viewType);
        int layoutId = item.getItemLayout();
        RViewHolder holder = RViewHolder.createViewHolder(parent.getContext(), parent, layoutId);

        //点击监听
        if (item.openClick()) {
            setListener(holder);
        }
        return holder;
    }

    //  将数据绑定到Item的ViewHolder控件
    @Override
    public void onBindViewHolder(@NonNull RViewHolder holder, int position) {
        convert(holder, datas.get(position));
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void addItemStyles(RViewItem<T> item) {
        itemStyle.addStyles(item);
    }

    public void setItemListener(ItemListener<T> itemListener) {
        this.itemListener = itemListener;
    }

    private void setListener(RViewHolder holder) {
        //阻塞事件
        holder.getmConvertView().setOnClickListener(v -> {
            if (itemListener != null) {
                int position = holder.getAdapterPosition();

                //阻塞事件添加
                long timeSpan = System.currentTimeMillis() - lastClickTime;
                if(timeSpan < QUICK_EVENT_TIME_SPAN) {
                    Log.e("Leezp >>> ", "点击阻塞，防止误点："+timeSpan);
                    return;
                }
                lastClickTime = System.currentTimeMillis();

                itemListener.onItemClick(v, datas.get(position), position);
            }
        });

        holder.getmConvertView().setOnLongClickListener(v -> {
            if (itemListener != null) {
                int position = holder.getAdapterPosition();
                return itemListener.onItemLongClick(v, datas.get(position), position);
            }
            return false;
        });
    }

    private void convert(RViewHolder holder, T entity) {
        itemStyle.convert(holder, entity, holder.getAdapterPosition());
    }
}
