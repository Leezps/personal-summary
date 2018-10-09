package com.leezp.test;

import com.leezp.interpreter.AndExpression;
import com.leezp.interpreter.Expression;
import com.leezp.interpreter.OrExpression;
import com.leezp.interpreter.TerminalExpression;

public class InterpreterPatternDemo {
	//规则：Robert和John是男性
	public static Expression getMaleExpression() {
		Expression robert = new TerminalExpression("Robert");
		Expression john = new TerminalExpression("John");
		return new OrExpression(robert, john);
	}
	
	//规则：Julie是一个已婚的女性
	public static Expression getMarriedWomanExpression() {
		Expression julie = new TerminalExpression("Julie");
		Expression married = new TerminalExpression("Married");
		return new AndExpression(julie, married);
	}
	
	public static void main(String[] args) {
		Expression isMale = getMaleExpression();
		Expression isMarriedWoman = getMarriedWomanExpression();
		
		System.out.println("John is male?"+isMale.interpret("John"));
		System.out.println("Julie is a married women?"+isMarriedWoman.interpret("Married Julie"));
	}
}
