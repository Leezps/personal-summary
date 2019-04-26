package com.leezp.livedatabus.customize;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.leezp.livedatabus.customize.lifecycle.HolderFragment;
import com.leezp.livedatabus.customize.lifecycle.LifecycleListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//消息通道 LiveData 架构
public class LiveData<T> {
    //组件的地址
    private HashMap<Integer, Observer<T>> map = new HashMap<>();
    //不活跃时的缓冲区
    private HashMap<Integer, List<T>> mPendingDelayList = new HashMap<>();
    //当异步线程发送事件
    Handler handler = new Handler(Looper.getMainLooper());

    private LifecycleListener lifecycleListener = new LifecycleListener() {
        @Override
        public void onCreate(int contextCode) {
            map.get(contextCode).setState(Observer.STATE_INIT);
        }

        @Override
        public void onStart(int contextCode) {
            map.get(contextCode).setState(Observer.STATE_ACTIVE);
            if (mPendingDelayList.get(contextCode) == null || mPendingDelayList.get(contextCode).size() == 0) {
                return;
            }
            for (T t : mPendingDelayList.get(contextCode)) {
                map.get(contextCode).onChanged(t);
            }
            mPendingDelayList.get(contextCode).clear();
        }

        @Override
        public void onPause(int contextCode) {
            map.get(contextCode).setState(Observer.STATE_ONPAUSE);
        }

        @Override
        public void onDetach(int contextCode) {
            map.get(contextCode).setState(Observer.STATE_DESTORY);
            map.remove(contextCode);
            mPendingDelayList.get(contextCode).clear();
        }
    };

    public void observe(Context context, Observer<T> observer) {
        if (context instanceof Activity) {
            FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
            HolderFragment current = (HolderFragment) fm.findFragmentByTag("com.leezp.livedata");
            if (current == null) {
                current = new HolderFragment();
                fm.beginTransaction().add(current,"com.leezp.livedata").commitAllowingStateLoss();
            }
            current.setLifecycleListener(lifecycleListener);
        }
        map.put(context.hashCode(),observer);
    }

    public void setValue(T value) {
        ArrayList<Integer> destoryList = new ArrayList<>();
        for (Map.Entry<Integer, Observer<T>> entry : map.entrySet()) {
            Observer<T> observer = entry.getValue();
            Integer contextCode = entry.getKey();
            //活跃的时候通过观察者发出
            if (observer.getState() == Observer.STATE_ACTIVE) {
                observer.onChanged(value);
            }
            //不活跃
            if (observer.getState() == Observer.STATE_ONPAUSE) {
                if (mPendingDelayList.get(contextCode) == null) {
                    mPendingDelayList.put(contextCode, new ArrayList<T>());
                }
                if (!mPendingDelayList.get(contextCode).contains(value)) {
                    mPendingDelayList.get(contextCode).add(value);
                }
            }
            //销毁掉的
            if (observer.getState() == Observer.STATE_DESTORY) {
                destoryList.add(contextCode);
            }
        }
        for (Integer item : destoryList) {
            map.remove(item);
        }
    }

    //因为这是其他线程，我们需要用到Handler
    public void postValue(final T value) {
        synchronized (this) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    setValue(value);
                }
            });
        }
    }
}
