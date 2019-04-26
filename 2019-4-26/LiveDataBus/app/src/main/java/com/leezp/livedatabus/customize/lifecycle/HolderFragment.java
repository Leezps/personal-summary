package com.leezp.livedatabus.customize.lifecycle;

import android.content.Context;
import android.support.v4.app.Fragment;

public class HolderFragment extends Fragment {
    private int contextCode;
    private LifecycleListener lifecycleListener;

    public void setLifecycleListener(LifecycleListener lifecycleListener) {
        this.lifecycleListener = lifecycleListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        contextCode = context.hashCode();
        if (lifecycleListener != null) {
            lifecycleListener.onCreate(contextCode);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (lifecycleListener != null) {
            lifecycleListener.onStart(contextCode);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (lifecycleListener != null) {
            lifecycleListener.onDetach(contextCode);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (lifecycleListener != null) {
            lifecycleListener.onPause(contextCode);
        }
    }
}
