package com.leezp.single;

public class SingleObject {
	//����SingleObject��һ������
	private static SingleObject instance = new SingleObject();
	
	//�ù��캯��Ϊprivate����������Ͳ��ᱻ�ⲿʵ����
	private SingleObject() {}
	
	//��ȡΨһ���õĶ���
	public static SingleObject getInstance() {
		return instance;
	}
	
	public void showMessage() {
		System.out.println("Hello World!");
	}
}
