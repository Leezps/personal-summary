package com.leezp.test;

import com.leezp.state.Context;
import com.leezp.state.StartState;
import com.leezp.state.StopState;

public class StatePatternDemo {
	public static void main(String[] args) {
		Context context = new Context();
		
		StartState startState = new StartState();
		startState.doAction(context);
		
		System.out.println(context.getState().toString());
		
		StopState stopState = new StopState();
		stopState.doAction(context);
		
		System.out.println(context.getState().toString());
	}
}
