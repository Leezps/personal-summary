package com.leezp.test;

import com.leezp.visitor.Computer;
import com.leezp.visitor.ComputerPart;
import com.leezp.visitor.ComputerPartDisplayVisitor;

public class VisitorPatternDemo {
	public static void main(String[] args) {
		ComputerPart computer = new Computer();
		computer.accept(new ComputerPartDisplayVisitor());
	}
}
