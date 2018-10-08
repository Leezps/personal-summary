package com.leezp.facade;

import com.leezp.facade.base.Shape;
import com.leezp.facade.base.impl.Circle;
import com.leezp.facade.base.impl.Rectangle;
import com.leezp.facade.base.impl.Square;

public class ShapeMaker {
	private Shape circle;
	private Shape rectangle;
	private Shape square;
	
	public ShapeMaker() {
		circle = new Circle();
		rectangle = new Rectangle();
		square = new Square();
	}
	
	public void drawCircle() {
		circle.draw();
	}
	
	public void drawRectangle() {
		rectangle.draw();
	}
	
	public void drawSquare() {
		square.draw();
	}
}
