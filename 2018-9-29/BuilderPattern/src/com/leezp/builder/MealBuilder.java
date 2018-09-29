package com.leezp.builder;

import com.leezp.builder.Item.impl.ChickenBurger;
import com.leezp.builder.Item.impl.Coke;
import com.leezp.builder.Item.impl.Pepsi;
import com.leezp.builder.Item.impl.VegBurger;

public class MealBuilder {
	public Meal prepareVegMeal() {
		Meal meal = new Meal();
		meal.addItem(new VegBurger());
		meal.addItem(new Coke());
		return meal;
	}
	
	public Meal prepareNonVegMeal() {
		Meal meal = new Meal();
		meal.addItem(new ChickenBurger());
		meal.addItem(new Pepsi());
		return meal;
	}
}
