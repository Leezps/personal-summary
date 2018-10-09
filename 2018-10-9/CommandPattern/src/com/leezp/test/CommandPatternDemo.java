package com.leezp.test;

import com.leezp.command.Broker;
import com.leezp.command.BuyStock;
import com.leezp.command.SellStock;
import com.leezp.command.Stock;

public class CommandPatternDemo {
	public static void main(String[] args) {
		Stock abcStock = new Stock();
		
		BuyStock buyStockOrder = new BuyStock(abcStock);
		SellStock sellStockOrder = new SellStock(abcStock);
		
		Broker broker = new Broker();
		broker.takeOrder(buyStockOrder);
		broker.takeOrder(sellStockOrder);
		
		broker.placeOrders();
	}
}
