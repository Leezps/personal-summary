package com.leezp.builder.Item;

import com.leezp.builder.pack.Packing;
import com.leezp.builder.pack.impl.Wrapper;

public abstract class Burger implements Item {
	@Override
	public Packing packing() {
		return new Wrapper();
	}
	
	@Override
	public abstract float price();
}
