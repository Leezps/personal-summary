package com.leezp.floatball.listener;

import android.view.View;
import android.widget.Toast;

import com.leezp.floatball.view.FloatBallView;

public class FloatBallMenuListener implements View.OnClickListener{
    private FloatBallView mFloatBallView;

    public FloatBallMenuListener(FloatBallView mFloatBallView) {
        this.mFloatBallView = mFloatBallView;
    }

    @Override
    public void onClick(View v) {
        switch (Integer.valueOf((String) v.getTag())) {
            case 10000:
                Toast.makeText(mFloatBallView.getContext(), "点击了游戏中心按钮", Toast.LENGTH_SHORT).show();
                break;
            case 10001:
                Toast.makeText(mFloatBallView.getContext(), "点击了论坛按钮", Toast.LENGTH_SHORT).show();
                break;
            case 10002:
                Toast.makeText(mFloatBallView.getContext(), "点击了帮助按钮", Toast.LENGTH_SHORT).show();
                break;
            case 10003:
                Toast.makeText(mFloatBallView.getContext(), "点击了客服中心按钮", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
