package com.leezp.universalinterface.interfaces.functions;

/**
 * 无参数、无返回值的方法封装
 */
public abstract class FunctionNoParamNoResult extends Function {

    public FunctionNoParamNoResult(String functionName) {
        super(functionName);
    }

    public abstract void function();
}
