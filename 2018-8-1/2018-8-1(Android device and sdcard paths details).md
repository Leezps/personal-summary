## Android文件存储路径详解 ##

#### 直奔主题 ####

> 因为Android 7.0之后，将文件的Uri地址由file://变成了content://,我们有可能使用原来的代码在Android 7.0之后编译运行会出现FileUriExposedException异常，为了解决这个问题，官方给我们提供了通过FileProvider的方式解决该问题，而在FileProvider中我们需要配置我们的存储路径配置文件，通常命名为file_paths.xml文件。

> file_paths.xml文件中可以设定你想访问的内存空间，下面给个file_paths.xml文件的例子：

```
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schems.android.com/apk/res/android">
	<!-- App专属文件：随着app删除而一起删除 -->

	<!-- 设备根目录internal storage(设备中) new File("/") -->
	<root-path
		name="root"
		path=""/>

	<!-- App专属文件区域，存在internal storage(设备中)，存储资源小并且比较重要的配置文件 ，默认只能被你的应用访问，内部空间十分有限 context.getFileDir() -->
	<files-path
		name="files"
		path=""/>

	<!-- 缓存，存储暂时并资源小的文件（设备中）context.getCacheDir() -->
	<cache-path
		name="cache"
		path=""/>

	<!-- SD Card的根目录 Environment.getExternalStorageDirectory() -->
	<external-path
		name="external"
		path=""/>

	<!-- App专属文件区域，存储在external storage(SD Card——16/32G)，存储资源大的文件 context.getExternalFileDirs()-->
	<external-files-path
		name="external_file_path"
		path=""/>

	<!-- 缓存，存储暂时并资源大的文件（SD Card）getExternalCacheDirs() -->
	<external-cache-path
		name="external_cache_path"
		path=""/>

	<!-- 现在很多手机都没有可移动SD Card了，一般都是厂商将SD Card内置到手机中，用户无法取出 -->
</paths>
```

有时候我们可能遇到下面这种情况，发现别人通过getExternalStoragePublicDirectory获取文件路径，emmmmmmmm，这个怎么没有对应的path与之对应呢？其实从表面上看，我们发现它与getExternalStorageDirectory很相近，我们来看看他们之间有什么区别？

**getExternalStorageDirectory()**与**getExternalStoragePublicDirectory()**的区别：

- getExternalStorageDirectory()一般用于获取外部存储的根目录，而getExternalStoragePublicDirectory()用于获取外部存储的公共区域，通常我们会传递一个参数进去获取对应的存储对应文件格式的文件夹，有如下参数：

> DIRECTORY_ALARMS //警报的铃声
DIRECTORY_DCIM //相机拍摄的图片和视频保存的位置
DIRECTORY_DOWNLOADS //下载文件保存的位置
DIRECTORY_MOVIES //电影保存的位置， 比如 通过google play下载的电影
DIRECTORY_MUSIC //音乐保存的位置
DIRECTORY_NOTIFICATIONS //通知音保存的位置
DIRECTORY_PICTURES //下载的图片保存的位置
DIRECTORY_PODCASTS //用于保存podcast(博客)的音频文件
DIRECTORY_RINGTONES //保存铃声的位置

- getExternalStorageDirectory().toString()显示为/storage/emulated/0，而getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()显示为/storage/emulated/0/DCIM，当然有些手机的根目录不一样，例如三星手机的外部根目录是/mnt/sdcard，但是他们都是在外部根目录下。

**为什么说getExternalFileDirs()是App专属文件区域？**

	App专属文件区域：随着App删除而一起删除，并且是本应用的私有文件，对其他应用是没有任何访问价值的。

	getExternalStoragePublicDirectory是创建公共文件，而getExternalFileDirs()是创建私有文件，它创建的文件目录形式是/Android/data/<package_name>/，所以它的文件可随着应用的删除而一起删除。

*其中有可能还有些知识未补充完整，以后遇到了在在这进行添加*

#### 参考 ####
<a href="http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2013/0923/1557.html">android中的文件操作详解以及内部存储和外部存储</a>