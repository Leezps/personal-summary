package com.leezp.test;

import com.leezp.proxy.Image;
import com.leezp.proxy.impl.ProxyImage;

public class ProxyPatternDemo {
	public static void main(String[] args) {
		Image image = new ProxyImage("test_10mb.jpg");
		
		//ͼ�񽫴Ӵ��̼���
		image.display();
		System.out.println("");
		//ͼ����Ҫ�Ӵ��̼���
		image.display();
	}
}
