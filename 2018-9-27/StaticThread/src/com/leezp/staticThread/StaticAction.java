package com.leezp.staticThread;

/**
 * @author leezp
 * @description 证明在java方法中不存在对全局变量修改的情况下，对任何方法(包括静态方法)都可以不考虑线程冲突。
 */
public class StaticAction {
	public static int num = 0;
	
	/**
	 * @description 外部创建多线程调用此方法，就会发现执行结果显示各线程对静态方法的访问时交叉执行的，但是交叉执行并没有影响sum值的计算。
	 * 				也就是说，在此过程中没有使用全局变量的静态方法在多线程中是安全的，静态方法是否引起线程安全问题主要看该静态方法是否对
	 * 				全局变量进行修改操作。
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
	 * @description 外部创建多线程调用此方法之后将StaticAction.num的值打印出来并且多次执行本工程，你会发现StaticAction的值是随机的。
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
	 * @description 加入synchronized关键字的静态方法称为同步静态方法，此方法的内部与incValue函数的内部一致，但是你多次执行本工程，
	 * 				你会发现每次得到的值都是50，这样就解决了多线程访问静态方法中含有对静态变量修改的操作的多线程冲突的问题。
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
