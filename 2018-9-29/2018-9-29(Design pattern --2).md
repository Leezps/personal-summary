## 设计模式学习（二） ##

> 下面所学的两个模式依旧是创建型模式，它们分别是建造者模式和原型模式。

#### 建造者模式 ####

&emsp;**意图：**将一个复杂的构建与其表示相分离，使得同样的构建过程可以创建不同的表示。
&emsp;**主要解决：**主要解决在软件系统中，有时候面领着“一个复杂对象”的创建工程，其通常由各个部分的子对象用一定的算法构成；由于需求的变化，这个复杂对象的各个部分经常面临着剧烈的变化，但是将它们组合在一起的算法却相对稳定。
&emsp;**何时使用：**一些基本部件不会变，而其组合经常变化的时候。
&emsp;**如何解决：**将变与不变分离开。

&emsp;<img src="./builder_pattern_uml_diagram.jpg" alt="建造者模式类图" width="500px"/>

&emsp;样例就是本文件夹下的BuilderPattern工程

**/\*\*------------------------------ 来自jade的分享 ------------------------------\*\*/**
建造者模式，又称生成器模式：将一个复杂的构建与其表示相分离，使得同样的构建过程可以创建不同的表示。
三个角色：**建造者**、**具体的建造者**、**监工**、**使用者（严格来说不算）**
- **建造者角色**：定义生成实例所需要的所有方法；
- **具体的建造者角色**：实现生成实例所需要的所有方法，并且定义获取最终生成实例的方法；
- **监工角色**：定义使用建造者角色中的方法来生成实例的方法；
- **使用者**：使用建造者模式

> 注意： 定义中"将一个复杂的构建工程逾期标示相分离"，表示并不是由建造者负责一切，而是由监工负责控制（定义）一个复杂的构建过程，由各个不同的建造者分别负责实现构建过程中所用到的所有构建步骤。不然，就无法做到"使得同样的构建过程可以创建不同的表示"这一目标。

**建造者角色：**
```
public abstract class Builder {
	public abstract void buildPart1();
	public abstract void buildPart2();
	public abstarct void buildPart3();
}
```
**监工角色：**
```
//将一个复杂的构建过程与其表示相分离
public class Director {
	private Builder builder; //针对接口编程，而不是针对实现编程
	public Director(Builder builder) {
		this.builder = builder;
	}
	public void setBuilder(Builder builder) {
		this.builder = builder;
	}

	public void construct() {	//控制（定义）一个复杂的构建过程
		builder.buildPart1();
		for(int i=0; i<5; ++i) {	//提示：如果想在运行过程中替换构建算法，可以考虑结合策略模式
			builder.buildPart2();
		}
		builder.buildPart3();
	}
}
```
**具体的建造者角色：**
```
/**
  * 此处实现了建造纯文本文档的具体建造者。
  * 可以考虑再实现一个建造HTML文档、XML文档，或者其他什么文档的具体建造者。
  * 这样，就可以使得同样的构建过程可以创建不同的表示
  */
public class ConcreteBuilder1 extends Builder {
	//假设buffer.toString()就是最终生成的产品
	private StringBuffer buffer = new StringBuffer();

	@Override
	public void buildPart1() {	//实现构建最终实例需要的所有方法
		buffer.append("Builder1 : Part1\n");
	}

	@Override
	public void buildPart2() {
		buffer.append("Builder1 : Part2\n");
	}

	@Override
	public void buildPart3() {
		buffer.append("Builder1 : Part3\n");
	}

	public String getResult() {	//定义获取最终生成实例的方法
		return buffer.toString();
	}
}
```
**客户角色：**
```
public class Client {
	public void testBuilderPattern() {
		ConcreteBuilder1 b1 = new ConcreteBuilder1();	//建造者
		Director director = new Director(b1);	//监工
		director.construct();	//建造实例（监工负责监督，建造者实际建造）
		String result = b1.getResult();	//获取最终生成结果
		System.out.printf("the result is :%n%s", result);
	}
}
```
**/\*\*------------------------------ 来自jade的分享结束 ------------------------------\*\*/**

#### 原型模式 ####

&emsp;**意图：**用原型实例指定创建对象的种类，并且通过拷贝这些原型创建新的对象。
&emsp;**主要解决：**在运行期建立和删除原型。
&emsp;**何时使用：**1、当一个系统应该独立于它的产品创建，构成和表示时。2、当要实例化的类是在运行时刻指定时，例如，通过动态装载。3、为了避免创建一个与产品类层次平行的工厂类层次时。4、当一个类的实例只能由几个不同状态组合中的一种时。建立相应数目的原型并克隆它们可能比每次用合适的状态手工实例化该类更方便一些。
&emsp;**如何解决：**利用已有的一个原型对象，快速地生成和原型对象一样的实例。
&emsp;<img src="./prototype_pattern_uml_diagram.jpg" alt="原型模式的类图" width="500px"/>

&emsp;样例就是本文件夹下的PrototypePattern工程

> 以下学习的设计模式则是结构型模式

#### 适配器模式 ####

&emsp;**意图：**将一个类的接口转换成客户希望的另外一个接口。适配器模式使得原本由于接口不兼容而不能一起工作的那些类可以一起工作。
&emsp;**主要解决：**主要解决在软件系统中，常常要将一些“现存的对象”放到新的环境中，而新环境要求的接口实现对象不能满足的。
&emsp;**何时使用：**1、系统需要使用现有的类，而此类的接口不符合系统的需求。2、想要建立一个可以重复使用的类，用于与一些彼此之间没有太大关联的一些类，包括一些可能在将来引进的类一起工作，这些原类不一定有一致的接口。3、通过接口转换，将一个类插入另一个类系中。
&emsp;**如何解决：**继承或依赖

&emsp;<img src="./adapter_pattern_uml_diagram.jpg" alt="适配器模式类图" width="500px"/>

&emsp;样例就是本文件夹下的AdapterPattern工程