package com.leezp.universalinterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.leezp.universalinterface.bean.UserBean;
import com.leezp.universalinterface.interfaces.FunctionManager;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById(R.id.send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FunctionManager.getInstance().invokeFunction("MainActivity_NoParamNoResult");

//                UserBean user = FunctionManager.getInstance().invokeFunction("MainActivity_NoParamHasResult", UserBean.class);
//                Log.e("SecondActivity","user object from MainActivity—"+user.getmUserName()+"  "+user.getmPassWord());

//                FunctionManager.getInstance().invokeFunction("MainActivity_HasParamNoResult",new UserBean("柳下惠","789123"));

                UserBean userBean = FunctionManager.getInstance().invokeFunction("MainActivity_HasParamHasResult", new UserBean("唐伯虎", "123654"), UserBean.class);
                Log.e("SecondActivity","user object from MainActivity—"+userBean.getmUserName()+"  "+userBean.getmPassWord());
            }
        });
    }
}
