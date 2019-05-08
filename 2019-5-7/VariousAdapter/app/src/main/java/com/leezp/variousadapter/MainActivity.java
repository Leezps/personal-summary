package com.leezp.variousadapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.leezp.library.base.RViewAdapter;
import com.leezp.library.listener.ItemListener;
import com.leezp.variousadapter.adapter.MultiAdapter;
import com.leezp.variousadapter.base.BaseRViewActivity;
import com.leezp.variousadapter.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseRViewActivity {

    private List<UserInfo> datas = new ArrayList<>();
    private MultiAdapter adapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recyclerview);
        super.onCreate(savedInstanceState);

        context = this;
        initDatas();
        listener();
    }

    private void listener() {
        adapter.setItemListener(new ItemListener<UserInfo>() {

            @Override
            public void onItemClick(View view, UserInfo entity, int position) {
                Toast.makeText(context, "条目点击 >>> " + position, Toast.LENGTH_SHORT).show();
                Log.e("Leezp >>> ", "有效点击");
            }

            @Override
            public boolean onItemLongClick(View view, UserInfo entity, int position) {
                Toast.makeText(context, "条目长按 >>> " + position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initDatas() {
        new Thread(() -> {
            if (datas.isEmpty()) {
                for (int i = 0; i < 15; i++) {
                    for (int j = 1; j <= 15; j++) {
                        UserInfo user = new UserInfo();
                        if (j % 15 == 1) {
                            user.setType(1);
                            user.setAccount("Leezp >>>>>>>>>>>>>> 1111111 >>>>>>>>>>>>>");
                        } else if (j % 15 == 2 || j % 15 == 3) {
                            user.setType(2);
                            user.setAccount("Leezp >>>>>>>>>> 2222222 >>>>>>>>>");
                        } else if (j % 15 == 4 || j % 15 == 5 || j % 15 == 6) {
                            user.setType(3);
                            user.setAccount("Leezp >>>>>>>> 3333333 >>>>>>>");
                        } else if (j % 15 == 7 || j % 15 == 8 || j % 15 == 9 || j % 15 == 10) {
                            user.setType(4);
                            user.setAccount("Leezp >>>>>> 4444444 >>>>>");
                        } else {
                            user.setType(5);
                            user.setAccount("Leezp >>>> 5555555 >>>");
                        }
                        datas.add(user);
                    }
                }
            }
            notifyAdapterDataSetChanged(datas);
        }).start();
    }

    @Override
    public void onRefresh() {
        initDatas();
    }

    @Override
    public RViewAdapter createRecyclerViewAdapter() {
        adapter = new MultiAdapter(null);
        return adapter;
    }
}
