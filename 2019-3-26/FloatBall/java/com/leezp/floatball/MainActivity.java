package com.leezp.floatball;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.leezp.floatball.utils.FloatBallWindowManager;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button openFloatBall = findViewById(R.id.activity_main_open_float_ball);
        Button hideFloatBall = findViewById(R.id.activity_main_hide_float_ball);

        openFloatBall.setOnClickListener(this);
        hideFloatBall.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_open_float_ball:
                FloatBallWindowManager.getInstance().createFloatBallWindow(this);
                break;
            case R.id.activity_main_hide_float_ball:
                FloatBallWindowManager.getInstance().removeFloatBallWindow(this);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        FloatBallWindowManager.getInstance().release(this);
        super.onDestroy();
    }
}
