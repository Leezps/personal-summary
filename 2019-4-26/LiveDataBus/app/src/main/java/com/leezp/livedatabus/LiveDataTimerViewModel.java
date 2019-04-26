package com.leezp.livedatabus;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.Timer;
import java.util.TimerTask;

public class LiveDataTimerViewModel extends ViewModel {
    static int i;
    //消息通道
    private MutableLiveData<String> mTimer = new MutableLiveData<>();

    public LiveDataTimerViewModel() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                i++;
                mTimer.postValue("--------->LiveDataTimerViewModel"+i);
            }
        },1000,1000);
    }

    public MutableLiveData<String> getmTimer() {
        return mTimer;
    }
}
