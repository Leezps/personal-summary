package com.leezp.universalinterface.interfaces.functions;

/**
 * 有参数、无返回值的方法封装
 *
 * @param <P>   参数
 */
public abstract class FunctionHasParamNoResult<P> extends Function {

    public FunctionHasParamNoResult(String functionName) {
        super(functionName);
    }

    public abstract void function(P p);
}
