package com.leezp.ioc_demo;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.leezp.ioc_demo.base.BaseActivity;
import com.leezp.library.annotation.ContentView;
import com.leezp.library.annotation.InjectView;
import com.leezp.library.annotation.OnClick;

// setContentView(R.layout.activity_main);
@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @InjectView(R.id.tv)
    private TextView tv;

    @InjectView(R.id.btn)
    private Button btn;

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(this, btn.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.tv, R.id.btn})   //可能多控件点击，业务一样
    public void onClick(View btn) {
        switch (btn.getId()) {
            case R.id.tv:
                Toast.makeText(this, "TextView 被点击了", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn:
                Toast.makeText(this, "Button 被点击了", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
