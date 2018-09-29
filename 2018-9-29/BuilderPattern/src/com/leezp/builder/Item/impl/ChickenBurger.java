package com.leezp.builder.Item.impl;

import com.leezp.builder.Item.Burger;

public class ChickenBurger extends Burger {

	@Override
	public String name() {
		return "Chicken Burger";
	}

	@Override
	public float price() {
		return 50.5f;
	}
}
