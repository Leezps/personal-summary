package com.leezp.builder.Item.impl;

import com.leezp.builder.Item.ColdDrink;

public class Pepsi extends ColdDrink {

	@Override
	public String name() {
		return "Pepsi";
	}

	@Override
	public float price() {
		return 35.0f;
	}
}
