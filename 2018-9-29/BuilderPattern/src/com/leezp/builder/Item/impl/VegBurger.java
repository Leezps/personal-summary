package com.leezp.builder.Item.impl;

import com.leezp.builder.Item.Burger;

public class VegBurger extends Burger {

	@Override
	public String name() {
		return "Veg Burger";
	}

	@Override
	public float price() {
		return 25.0f;
	}
}
