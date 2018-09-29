package com.leezp.builder.Item;

import com.leezp.builder.pack.Packing;
import com.leezp.builder.pack.impl.Bottle;

public abstract class ColdDrink implements Item {

	@Override
	public Packing packing() {
		return new Bottle();
	}

	@Override
	public abstract float price();
}
