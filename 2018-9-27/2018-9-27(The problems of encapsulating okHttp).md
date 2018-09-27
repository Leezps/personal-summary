## OkHttp3的封装 ##

#### 异常 ####

> 网上到处都是AndroidStudio关于OkHttp3的导入方式，Eclipse的很少，一不注意有什么jar包没导入，就会报错，而且出错之后你就只能去根据提示去猜缺什么东西！

- **java.lang.NoClassDefFoundError: Failed resolution of: Lokio/Buffer;**

> &emsp;缺okio.jar包，对应的版本要根据对应okHttp去寻找。文件夹中我也会给出okio-2.1.0版本的包，它是okhttp-3.11.0的包所对应的jar包。jar_files.zip给出的更老版本的okHttp3以及okio，给这个是因为下面这个异常！

- **java.lang.NoClassDefFoundError: Failed resolution of: Lkotlin/text/Charsets;**

> &emsp;从异常信息可以看出是因为缺少kotlin中的一个类，这说明okhttp3或okio中使用了kotlin导致给出的jar包不含kotlin文件出现的问题，所以我们需要导入一个kotlin的包，也是本文件夹给出的kotlin-stdlib-1.1.3-2.jar。将其导入你的工程问题就解决了。但是老版本没这个问题，因为老版本没有用kotlin进行开发，这也给我们一个警示！

#### 如何封装呢？ ####

**参考：**

<a href="https://blog.csdn.net/my_rabbit/article/details/70213975">OkHttp3封装参考</a>

<a href="https://github.com/wiggins9629/OkHttp3/tree/master/app/src/main/java/com/wiggins/okhttp3/http">上面参考对应的示例</a>

#### 测试 ####

- **多线程异步数据请求**

> 看示例可以发现当我们多线程异步处理OkHttp中的同步get请求时，请求的函数是静态函数，那异步请求会不会导致多线程冲突呢（即线程不安全）?

> 然后根据<a href="https://blog.csdn.net/alinshen/article/details/77905727">这篇博客</a>写了一个demo，可以从中发现如果在java方法中不存在对全局变量修改的情况下，对任何方法(包括静态方法)都可以不考虑线程冲突。

```
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
```

> 调用StaticAction的类：

```
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

```
