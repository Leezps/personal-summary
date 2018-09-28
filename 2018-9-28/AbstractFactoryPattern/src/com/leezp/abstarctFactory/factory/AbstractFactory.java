package com.leezp.abstarctFactory.factory;

import com.leezp.abstarctFactory.color.Color;
import com.leezp.abstarctFactory.shape.Shape;

public abstract class AbstractFactory {
	public abstract Color getColor(String color);
	public abstract Shape getShape(String shape);
}
