package com.leezp.facade.base.impl;

import com.leezp.facade.base.Shape;

public class Rectangle implements Shape {

	@Override
	public void draw() {
		System.out.println("Rectangle::draw()");
	}
}
