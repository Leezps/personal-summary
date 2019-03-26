## 作为第三方，如何在Android中正确使用R资源文件 ##

### 1、需求 ###

&emsp;&emsp;作为第三方，经常会提供一些资源文件给其他用户方使用，而这些资源文件涉及到java、res、assets、libs等文件，但是其中有一个问题就是我们的java文件可能会使用到res中的资源，java文件是通过R.java文件去引用对应的资源，这就会涉及到R文件找不到导致的一些问题。

### 2、方案 ###

- 第三方提供的是Library(依赖库)，这样就不会涉及到R文件找不到对应资源的问题，java文件可以直接使用依赖库的R文件

- 第三方提供的是jar包，此时的java文件获取对应的资源需要通过getResources().getIdentifier()的方式去获取(其实这种方式也就是通过Java的反射机制去获取的，我们也可以自己去实现对应的反射原理去获取资源文件)

### 3、资料 ###

- <a href="https://blog.csdn.net/wxx614817/article/details/50921194">Android含资源文件引用的项目打包成jar包</a>

### 4、样例 ###

> 样例就是当前文件目录中的Samples工程，其中iap5helper就是需要依赖的库，我们可以将其打包成jar文件给用户方使用。主工程(app)目录下的libs中的Samsung_IAP_v5.1.0.jar就是iap5helper打成的jar包