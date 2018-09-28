package com.leezp.abstarctFactory;

import com.leezp.abstarctFactory.factory.AbstractFactory;
import com.leezp.abstarctFactory.factory.ColorFactory;
import com.leezp.abstarctFactory.factory.ShapeFactory;

public class FactoryProducer {
	public static AbstractFactory getFactory(String choice) {
		if (choice.equalsIgnoreCase("SHAPE")) {
			return new ShapeFactory();
		} else if (choice.equalsIgnoreCase("COLOR")) {
			return new ColorFactory();
		}
		return null;
	}
}
