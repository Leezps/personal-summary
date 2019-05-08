package com.leezp.variousadapter.adapter;

import com.leezp.library.base.RViewAdapter;
import com.leezp.variousadapter.bean.UserInfo;

import java.util.List;

public class MultiAdapter extends RViewAdapter<UserInfo> {
    public MultiAdapter(List<UserInfo> datas) {
        super(datas);
        addItemStyles(new AItem());
        addItemStyles(new BItem());
        addItemStyles(new CItem());
        addItemStyles(new DItem());
        addItemStyles(new EItem());
    }
}
