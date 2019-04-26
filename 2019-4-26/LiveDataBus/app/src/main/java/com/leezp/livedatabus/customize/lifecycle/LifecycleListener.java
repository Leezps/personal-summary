package com.leezp.livedatabus.customize.lifecycle;

public interface LifecycleListener {
    void onCreate(int contextCode);
    void onStart(int contextCode);
    void onPause(int contextCode);
    void onDetach(int contextCode);
}
