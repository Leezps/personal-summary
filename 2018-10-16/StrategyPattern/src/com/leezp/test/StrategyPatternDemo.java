package com.leezp.test;

import com.leezp.strategy.Context;
import com.leezp.strategy.OperationAdd;
import com.leezp.strategy.OperationMultiply;
import com.leezp.strategy.OperationSubstract;

public class StrategyPatternDemo {
	public static void main(String[] args) {
		Context context = new Context(new OperationAdd());
		System.out.println("10 + 5 = "+context.executeStrategy(10, 5));
		
		context = new Context(new OperationSubstract());
		System.out.println("10 - 5 = "+context.executeStrategy(10, 5));
		
		context = new Context(new OperationMultiply());
		System.out.println("10 * 5 = "+context.executeStrategy(10, 5));
	}
}
