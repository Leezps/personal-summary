package com.leezp.networkokhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.leezp.networkokhttp.okhttp.listener.IJsonDataListener;
import com.leezp.networkokhttp.okhttp.utils.NWOkhttp;

public class MainActivity extends AppCompatActivity {
    private String url = "http://api.map.baidu.com/telematics/v3/weather?location=常州&output=json";
//    private String url = "http://xxxxxxxx";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendJsonRequest();
    }

    private void sendJsonRequest() {
        NWOkhttp.sendJsonRequest(null, url, ResponseClass.class, new IJsonDataListener<ResponseClass>() {
            @Override
            public void onSuccess(ResponseClass m) {
                Log.e("==========>", m.toString());
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
