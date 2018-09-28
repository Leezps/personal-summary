package com.leezp.abstarctFactory.factory;

import com.leezp.abstarctFactory.color.Blue;
import com.leezp.abstarctFactory.color.Color;
import com.leezp.abstarctFactory.color.Green;
import com.leezp.abstarctFactory.color.Red;
import com.leezp.abstarctFactory.shape.Shape;

public class ColorFactory extends AbstractFactory {

	@Override
	public Color getColor(String color) {
		if (color == null) {
			return null;
		}
		if (color.equalsIgnoreCase("RED")) {
			return new Red();
		} else if (color.equalsIgnoreCase("GREEN")) {
			return new Green();
		} else if (color.equalsIgnoreCase("BLUE")) {
			return new Blue();
		}
		return null;
	}

	@Override
	public Shape getShape(String shape) {
		return null;
	}

}
