package com.leezp.universalinterface.interfaces.functions;

/**
 * 无参数、有返回值的方法封装
 *
 * @param <T> 返回值
 */
public abstract class FunctionNoParamHasResult<T> extends Function {

    public FunctionNoParamHasResult(String functionName) {
        super(functionName);
    }

    public abstract T function();
}
