package com.leezp.variousadapter.adapter;

import android.widget.TextView;

import com.leezp.library.holder.RViewHolder;
import com.leezp.library.listener.RViewItem;
import com.leezp.variousadapter.R;
import com.leezp.variousadapter.bean.UserInfo;

public class DItem implements RViewItem<UserInfo> {
    @Override
    public int getItemLayout() {
        return R.layout.item_d;
    }

    @Override
    public boolean openClick() {
        return true;
    }

    @Override
    public boolean isItemView(UserInfo entity, int position) {
        return entity.getType() == 4;
    }

    @Override
    public void convert(RViewHolder holder, UserInfo entity, int position) {
        TextView textView = holder.getView(R.id.mtv);
        textView.setText(entity.getAccount());
    }
}
