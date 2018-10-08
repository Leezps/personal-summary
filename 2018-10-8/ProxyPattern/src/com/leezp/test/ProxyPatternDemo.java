package com.leezp.test;

import com.leezp.proxy.Image;
import com.leezp.proxy.impl.ProxyImage;

public class ProxyPatternDemo {
	public static void main(String[] args) {
		Image image = new ProxyImage("test_10mb.jpg");
		
		//图像将从磁盘加载
		image.display();
		System.out.println("");
		//图像不需要从磁盘加载
		image.display();
	}
}
