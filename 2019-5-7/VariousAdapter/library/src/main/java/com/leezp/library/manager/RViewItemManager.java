package com.leezp.library.manager;

import android.support.v4.util.SparseArrayCompat;

import com.leezp.library.holder.RViewHolder;
import com.leezp.library.listener.RViewItem;

//  多类型、多样式item管理器
public class RViewItemManager<T> {

    // key: viewType value: RViewItem
    private SparseArrayCompat<RViewItem<T>> styles = new SparseArrayCompat<>();

    //  获取所有item样式到底多少种
    public int getItemViewStyleCount() {
        return styles.size();
    }

    //  加入新的item样式（每次加入放置末尾）
    public void addStyles(RViewItem<T> item) {
        if (item != null) {
            styles.put(styles.size(), item);
        }
    }

    // 根据数据源和位置返回某item类型的viewType(从styles获取key)
    public int getItemViewType(T entity, int position) {
        for (int i = styles.size() - 1; i >= 0; i--) {  //  样式倒序循环，避免增删集合抛出异常
            //比如第1个位置（索引0），第1类条目样式
            RViewItem<T> item = styles.valueAt(i);
            if (item.isItemView(entity, position)) {    //  是否为当前样式显示，由外面实现
                //  获得集合key, viewType
                return styles.keyAt(i);
            }
        }
        throw new IllegalArgumentException("位置：" + position + "，该item没有匹配的RViewItem类型");
    }

    /**
     * 根据显示的viewType返回RViewItem对象（获取value）
     *
     * @param viewType 布局类型
     * @return RViewItem对象
     */
    public RViewItem getRViewItem(int viewType) {
        return styles.get(viewType);
    }

    //  视图与数据源绑定显示
    public void convert(RViewHolder holder, T entity, int position) {
        for (int i = 0; i < styles.size(); i++) {
            RViewItem<T> item = styles.valueAt(i);
            if (item.isItemView(entity, position)) {
                //视图与数据源绑定显示，有外面实现（参数赋值，外面用）
                item.convert(holder, entity, position);
                return;
            }
        }
        throw new IllegalArgumentException("位置：" + position + "，该item没有匹配的RViewItem类型");
    }
}
