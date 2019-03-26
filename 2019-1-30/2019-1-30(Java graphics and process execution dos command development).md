## Java图形以及在进程中执行dos命令开发 ##

### 1、需求 ###

> 本日期的另一篇文章有讲使用ANT编写工程的脚本文件，但是脚本文件的运行是通过dos命令去执行的，所以对于其他开发者来说不是很友好，所以公司内部建议创建一个可视化的图形界面去执行运行ANT脚本文件的dos命令。

### 2、资源 ###

图形界面开发：
- <a href="https://blog.csdn.net/xietansheng/article/details/72814492">Java Swing 图形界面开发（目录）</a>
- <a href="https://blog.csdn.net/xietansheng/article/details/72814531">Java Swing 图形界面开发简介</a>

进程调用外部程序：
- <a href="https://blog.csdn.net/c315838651/article/details/72085739">Java进程Runtime、Process、ProcessBuilder调用外部程序</a>
- <a href="https://xdwangiflytek.iteye.com/blog/1595686">Java中Process的waitFor()阻塞问题</a>

附加：
- <a href="https://blog.csdn.net/YoungStar70/article/details/75116227">Java项目打包成exe的详细教程</a>

### 3、样例 ###

当前文件夹目录下的 **ChannelsANT** 工程，该工程实现了一个图形界面以及进程执行dos命令调用外部程序的功能。