## 布局渲染流程与原理——笔记 ##

### 1、需求 ###

&emsp;&emsp;完成软件需求之后，为了提升软件的用户体验以及响应效率，就会对软件布局进行优化。

### 2、流程与原理 ###

&emsp;**CPU 与 GPU 的区别**

&emsp;&emsp;<img src="./layout_2_1.jpg" alt="2_1" width="600px"/>

&emsp;Android 中布局渲染更新以16毫秒为一个间隔，**为什么会以16ms为间隔呢？**

&emsp;&emsp;<img src="./layout_2_2.jpg" alt="2_2" width="600px"/>

&emsp;因此手机应用卡顿的原理是：

&emsp;&emsp;<img src="./layout_2_3.jpg" alt="2_3" width="600px"/>

&emsp;通过卡顿原理分析得出：

&emsp;&emsp;<img src="./layout_2_4.jpg" alt="2_4" width="600px"/>

&emsp;**如何解决卡顿？**

&emsp;&emsp;<img src="./layout_2_5.jpg" alt="2_5" width="600px"/>

&emsp;**GPU的过度绘制**

&emsp;&emsp;<img src="./layout_2_6.jpg" alt="2_6" width="600px"/>

&emsp;**如何查看应用是否过度绘制？**

&emsp;&emsp;<img src="./layout_2_7.jpg" alt="2_7" width="600px"/>