package com.leezp.abstarctFactory.factory;

import com.leezp.abstarctFactory.color.Color;
import com.leezp.abstarctFactory.shape.Circle;
import com.leezp.abstarctFactory.shape.Rectangle;
import com.leezp.abstarctFactory.shape.Shape;
import com.leezp.abstarctFactory.shape.Square;

public class ShapeFactory extends AbstractFactory {

	@Override
	public Color getColor(String color) {
		return null;
	}

	@Override
	public Shape getShape(String shape) {
		if (shape == null) {
			return null;
		}
		if (shape.equalsIgnoreCase("CIRCLE")) {
			return new Circle();
		} else if (shape.equalsIgnoreCase("RECTANGLE")) {
			return new Rectangle();
		} else if (shape.equalsIgnoreCase("SQUARE")) {
			return new Square();
		}
		return null;
	}

}
