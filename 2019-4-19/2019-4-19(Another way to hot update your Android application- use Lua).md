## 另一种热更新方式更新Android应用——使用Lua ##

### 1.背景 ###

> &emsp;&emsp;公司准备将自己的项目的实现逻辑总结一下，写成对应的文档，给后面的开发人员研读，让我对项目进行一些总结，我在研究项目的时候，最大的收获就是发现了另一种热更新的方式，并且这种热更新方式在Android/IOS平台都能实现，而且苹果官方以及Android官方都支持这种热更新方式，不过这种热更新方式对应用的效率运行有一定的影响，所以如果你的应用对运行效率要求很高的话，建议不要考虑这种方式了。

### 2.需求 ###

> &emsp;&emsp;应用不需要重新安装，通过修改服务器的文件就能修改客户端的逻辑以及新增对应的功能。

### 3.方案 ###

> Android中接入Lua语言开发

> 不做具体的详述了，网上对这种方式有了很多很多的资料与文档了，Google或百度吧！这里介绍有这种方式，其中原理就是Lua语言可以编写程序的逻辑，并且它的文件是存在资源目录如assets、raw中的，不需要打包到dex文件中，这样的话，我们可以在程序启动时，对比服务器版本号以及客户端当前版本号，如果不同，下载服务器上的Lua文件到客户端，覆盖掉上一个版本的Lua文件，就这样实现修改程序逻辑

> 问题来了——怎么这个Lua和 html 好相似啊？

> 1. html每次打开都需要动态加载，然而Lua文件是存在于本地的，每次打开只需比较一次版本号，通过网络流量消耗就知道它们的不一样。

> 2. 如果你想说html、js文件这些我也可以存在本地，然后每次和Lua一样，也比较下版本号就可以运行了，其实其中Lua与html的不同还存在一个地方，就是Lua的编译虚拟机是基于C语言的，并且代码只有200多行，而如果使用html，我们还需考虑js，这样我们需要将html、js的解释器保存到我们的应用中，然而这个解释器的代码量就有些大了，相当于我们内置了个浏览器的内核，至少10多MB,从这方面就看出了Lua比html更优秀的一个地方

### 4.资料 ###

1.<a href="http://nightfarmer.github.io/2016/08/10/luabridge/">有关Android热更新/热补丁的一种新的解决方案</a>
2.<a href="https://www.jianshu.com/p/908a1ac893bb">Android Lua 相互调用</a>
3.<a href="https://yongyu.itscoder.com/2018/04/16/yongyu_20180416_lua_android_one/">Lua 在 Android 中应用上,如何引入 Lua</a>

> 引入下其他知识——如Unity引擎和Android

4.<a href="https://blog.csdn.net/crazy1235/article/details/46733221">Android与Unity交互研究</a>

> Unity引擎是以C#为编程语言的引擎，在此引入下Lua与C#之间的互调

5.<a href="https://www.jianshu.com/p/b6b24cb910ed">对Lua ，C，C#互相调用的理解</a>
6.<a href="https://www.kancloud.cn/digest/luanote/119933">Lua中调用C函数(lua-5.2.3)</a>
7.<a href="https://blog.csdn.net/u013625451/article/details/78813422">C语言中调用LUA（1）</a>
8.<a href="http://www.voidcn.com/article/p-ssuodwzc-qh.html">C#调用C语言生成的DLL示例</a>
9.<a href="https://www.cnblogs.com/HappyEDay/p/7742890.html">C#委托实现C++ Dll中的回调函数</a>
10.<a href="https://blog.uwa4d.com/archives/USparkle_Lua.html">用好Lua+Unity，让性能飞起来——Lua与C#交互篇</a>

### 5.样例 ###

无