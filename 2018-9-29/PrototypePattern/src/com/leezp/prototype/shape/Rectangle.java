package com.leezp.prototype.shape;

public class Rectangle extends Shape{
	
	public Rectangle() {
		type = "Rectangle";
	}

	@Override
	void draw() {
		System.out.println("Inside Rectangle::draw() method.");
	}
}
