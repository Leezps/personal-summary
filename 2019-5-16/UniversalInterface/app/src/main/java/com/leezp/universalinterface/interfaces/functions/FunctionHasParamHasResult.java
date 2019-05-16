package com.leezp.universalinterface.interfaces.functions;

/**
 * 有参数、有返回值的方法封装
 *
 * @param <T>   返回值
 * @param <P>   参数
 */
public abstract class FunctionHasParamHasResult<T,P> extends Function {

    public FunctionHasParamHasResult(String functionName) {
        super(functionName);
    }

    public abstract T function(P p);
}
