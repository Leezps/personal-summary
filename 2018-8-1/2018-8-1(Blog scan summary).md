## 浏览博客之后的知识总结 ##

	1. 自定义实现拉伸或扭曲动画的View，通过调用Android的Canvas类中的drawBitmapMesh方法
	
```
drawBitmapMesh(Bitmap bitmap,	//需要实现动画的源位图
					int meshWidth,	//源位图在横向上划分为多少格
					int meshHeight,	//源位图在纵向上划分为多少格
					float[] verts,	//每个格子的顶点，顶点数为（meshWidth+1）*(meshHeight+1)，但是要存储x\y两个坐标，所以还需乘以2
					int vertOffset,	//从第几个verts中的数组元素开始扭曲
					int[] colors,	//颜色
					int colorffset,	//颜色变换
					Paint paint)	//画笔
```

**详细过程请点击**

- <a href="https://juejin.im/post/5b5ec836e51d4519155724bc">自定义View-扭曲动效</a>——该项目的<a href="https://github.com/ChestnutPlus/ModulesUi">源代码</a>


	2.hash算法总结，hash算法又称散列算法，优秀的hash算法需要具备以下几点：
- 正向快速计算出hash值
- 逆向解析非常困难或者基本不可能
- 输入的原始信息与hash值有很大的不同
- 非常困难找出两段明文不同，hash值一致的问题（避免hash冲突）

通过取模以及异或实现一个简单的不可逆算法

**详细过程请点击**
- <a href="https://blog.csdn.net/asdzheng/article/details/70226007">Hash算法总结</a>
