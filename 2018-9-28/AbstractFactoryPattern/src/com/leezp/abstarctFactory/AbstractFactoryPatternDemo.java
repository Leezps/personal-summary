package com.leezp.abstarctFactory;

import com.leezp.abstarctFactory.color.Color;
import com.leezp.abstarctFactory.factory.AbstractFactory;
import com.leezp.abstarctFactory.shape.Shape;

public class AbstractFactoryPatternDemo {
	public static void main(String[] args) {
		//获取形状工厂
		AbstractFactory shapeFactory = FactoryProducer.getFactory("SHAPE");
		//获取形状为Circle的对象
		Shape shape1 = shapeFactory.getShape("CIRCLE");
		//调用Circle的draw方法
		shape1.draw();
		//获取形状为Rectangle的对象
		Shape shape2 = shapeFactory.getShape("RECTANGLE");
		//调用ReCtangle的对象
		shape2.draw();
		//获取形状为Square的对象
		Shape shape3 = shapeFactory.getShape("SQUARE");
		//调用Square的draw方法
		shape3.draw();
		//获取颜色工厂
		AbstractFactory colorFactory = FactoryProducer.getFactory("COLOR");
		//获取颜色为Red的对象
		Color color1 = colorFactory.getColor("RED");
		//调用Red的fill方法
		color1.fill();
		//获取颜色为Green的对象
		Color color2 = colorFactory.getColor("Green");
		//调用Green的fill方法
		color2.fill();
		//获取颜色为Blue的对象
		Color color3 = colorFactory.getColor("BLUE");
		//调用Blue的fill方法
		color3.fill();
	}
}
