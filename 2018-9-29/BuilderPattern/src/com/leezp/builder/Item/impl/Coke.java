package com.leezp.builder.Item.impl;

import com.leezp.builder.Item.ColdDrink;

public class Coke extends ColdDrink {

	@Override
	public String name() {
		return "Coke";
	}

	@Override
	public float price() {
		return 30.0f;
	}
}
