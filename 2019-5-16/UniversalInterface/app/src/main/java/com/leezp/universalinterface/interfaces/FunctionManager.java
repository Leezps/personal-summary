package com.leezp.universalinterface.interfaces;

import android.text.TextUtils;
import android.util.Log;

import com.leezp.universalinterface.interfaces.functions.FunctionHasParamHasResult;
import com.leezp.universalinterface.interfaces.functions.FunctionHasParamNoResult;
import com.leezp.universalinterface.interfaces.functions.FunctionNoParamHasResult;
import com.leezp.universalinterface.interfaces.functions.FunctionNoParamNoResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 所有方法的管理类，外部只需和此类交互，就可实现方法的执行
 */
public class FunctionManager {
    private static FunctionManager instance = new FunctionManager();
    private Map<String, FunctionNoParamNoResult> mNoParamNoResultMap;
    private Map<String, FunctionNoParamHasResult> mNoParamHasResultMap;
    private Map<String, FunctionHasParamNoResult> mHasParamNoResultMap;
    private Map<String, FunctionHasParamHasResult> mHasParamHasResultMap;

    public FunctionManager() {
        mNoParamNoResultMap = new HashMap<>();
        mNoParamHasResultMap = new HashMap<>();
        mHasParamNoResultMap = new HashMap<>();
        mHasParamHasResultMap = new HashMap<>();
    }

    public static FunctionManager getInstance() {
        return instance;
    }

    // 将需要储存的方法进行添加
    // 添加没有返回值 也没有参数的方法
    public void addFunction(FunctionNoParamNoResult function) {
        mNoParamNoResultMap.put(function.functionName, function);
    }

    // 执行没有返回值也没有参数的方法
    public void invokeFunction(String functionName) {
        if (TextUtils.isEmpty(functionName)) {
            return;
        }
        if (mNoParamNoResultMap != null) {
            FunctionNoParamNoResult f = mNoParamNoResultMap.get(functionName);
            if (f != null) {
                f.function();
            } else {
                Log.e("FunctionManager", "没有找到该方法");
            }
        }
    }

    // 添加有返回值，没有参数的方法
    public void addFunction(FunctionNoParamHasResult function) {
        mNoParamHasResultMap.put(function.functionName, function);
    }

    // 执行有返回值，没有参数的方法
    public <T> T invokeFunction(String functionName, Class<T> t) {
        if (TextUtils.isEmpty(functionName)) {
            return null;
        }
        if (mNoParamHasResultMap != null) {
            FunctionNoParamHasResult f = mNoParamHasResultMap.get(functionName);
            if (f != null) {
                if (t != null) {
                    return t.cast(f.function());
                }
            } else {
                Log.e("FunctionManager", "没有找到该方法");
            }
        }
        return null;
    }

    // 添加没有返回值，有参数的方法
    public void addFunction(FunctionHasParamNoResult function) {
        mHasParamNoResultMap.put(function.functionName, function);
    }

    // 执行没有返回值，有参数的方法
    public <P> void invokeFunction(String functionName, P p) {
        if (TextUtils.isEmpty(functionName)) {
            return;
        }
        if (mHasParamNoResultMap != null) {
            FunctionHasParamNoResult f = mHasParamNoResultMap.get(functionName);
            if (f != null) {
                f.function(p);
            } else {
                Log.e("FunctionManager", "没有找到该方法");
            }
        }
    }

    // 添加有返回值，有参数的方法
    public void addFunction(FunctionHasParamHasResult function) {
        mHasParamHasResultMap.put(function.functionName, function);
    }

    // 执行有返回值，有参数的方法
    public <T, P> T invokeFunction(String functionName, P p, Class<T> t) {
        if (TextUtils.isEmpty(functionName)) {
            return null;
        }
        if (mHasParamHasResultMap != null) {
            FunctionHasParamHasResult f = mHasParamHasResultMap.get(functionName);
            if (f != null) {
                if (t != null) {
                    return t.cast(f.function(p));
                }
            } else {
                Log.e("FunctionManager", "没有找到该方法");
            }
        }
        return null;
    }

    /**
     * 为什么要使用此方法，因为其实添加进来的方法对于组件（Activity）来说，
     * 这些方法都是匿名内部类，他们都持有组件（Activity）的引用，
     * 所以组件（Activity）释放的时候，我们需在这将方法释放掉，组件（Activity）的内存才能释放，反之，则会造成内存泄漏
     *
     * @param functionName  方法名
     */
    public void removeFunction(String functionName) {
        if (mNoParamNoResultMap.containsKey(functionName)) {
            mNoParamNoResultMap.remove(functionName);
        } else if (mNoParamHasResultMap.containsKey(functionName)) {
            mNoParamHasResultMap.remove(functionName);
        } else if (mHasParamNoResultMap.containsKey(functionName)) {
            mHasParamNoResultMap.remove(functionName);
        } else if (mHasParamHasResultMap.containsKey(functionName)) {
            mHasParamHasResultMap.remove(functionName);
        } else {
            Log.e("FunctionManager", "不存在该方法");
        }
    }

    /**
     * 双重保险，释放掉内存,此方法应该在 Application 的 onStop 中调用
     */
    public void dispose() {
        if (mNoParamNoResultMap != null) {
            mNoParamNoResultMap.clear();
            mNoParamNoResultMap = null;
        }
        if (mNoParamHasResultMap != null) {
            mNoParamHasResultMap.clear();
            mNoParamHasResultMap = null;
        }
        if (mHasParamNoResultMap != null) {
            mHasParamNoResultMap.clear();
            mHasParamNoResultMap = null;
        }
        if (mHasParamHasResultMap != null) {
            mHasParamHasResultMap.clear();
            mHasParamHasResultMap = null;
        }
    }
}