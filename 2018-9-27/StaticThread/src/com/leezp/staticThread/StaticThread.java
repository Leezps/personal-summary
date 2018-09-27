package com.leezp.staticThread;

public class StaticThread implements Runnable{

	@Override
	public void run() {
//		StaticAction.print();
//		StaticAction.incValue();
		StaticAction.incValueSync();
	}
	
	public static void main(String[] args) {
		for(int i=0; i<50; ++i) {
			new Thread(new StaticThread()).start();
		}
		
		/**----------------- 以下部分属于调用StaticAction.incValue()调用需要的代码部分 -----------------**/
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(StaticAction.num);
	}
}
