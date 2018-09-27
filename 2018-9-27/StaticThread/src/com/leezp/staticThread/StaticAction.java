package com.leezp.staticThread;

/**
 * @author leezp
 * @description ֤����java�����в����ڶ�ȫ�ֱ����޸ĵ�����£����κη���(������̬����)�����Բ������̳߳�ͻ��
 */
public class StaticAction {
	public static int num = 0;
	
	/**
	 * @description �ⲿ�������̵߳��ô˷������ͻᷢ��ִ�н����ʾ���̶߳Ծ�̬�����ķ���ʱ����ִ�еģ����ǽ���ִ�в�û��Ӱ��sumֵ�ļ��㡣
	 * 				Ҳ����˵���ڴ˹�����û��ʹ��ȫ�ֱ����ľ�̬�����ڶ��߳����ǰ�ȫ�ģ���̬�����Ƿ������̰߳�ȫ������Ҫ���þ�̬�����Ƿ��
	 * 				ȫ�ֱ��������޸Ĳ�����
	 */
	public static void print() {
		int sum = 0;
		for(int i=0; i<10; ++i) {
			System.out.println(Thread.currentThread().getName() + " is running --" + i);
			sum += i;
		}
		if(sum != 45) {
			System.out.println(Thread.currentThread().getName() + " Thread error!");
			System.exit(0);
		}
		System.out.println(Thread.currentThread().getName() + " sum is " + sum);
	}
	
	/**
	 * @description �ⲿ�������̵߳��ô˷���֮��StaticAction.num��ֵ��ӡ�������Ҷ��ִ�б����̣���ᷢ��StaticAction��ֵ������ġ�
	 */
	public static void incValue() {
		int temp = StaticAction.num;
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		temp++;
		StaticAction.num = temp;
	}
	
	/**
	 * @description ����synchronized�ؼ��ֵľ�̬������Ϊͬ����̬�������˷������ڲ���incValue�������ڲ�һ�£���������ִ�б����̣�
	 * 				��ᷢ��ÿ�εõ���ֵ����50�������ͽ���˶��̷߳��ʾ�̬�����к��жԾ�̬�����޸ĵĲ����Ķ��̳߳�ͻ�����⡣
	 */
	public static synchronized void incValueSync() {
		int temp = StaticAction.num;
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		temp++;
		StaticAction.num = temp;
	}
}
