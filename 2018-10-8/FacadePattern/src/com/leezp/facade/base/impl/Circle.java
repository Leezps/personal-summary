package com.leezp.facade.base.impl;

import com.leezp.facade.base.Shape;

public class Circle implements Shape {

	@Override
	public void draw() {
		System.out.println("Circle::draw()");
	}
}
