##compileSdkVersion##

> 1、compileSdkVersion仅仅是告诉Gradle使用哪个版本的SDK编译应用，不会被包含到apk中，完全不影响应用的运行结果； 

> 2、既然完全无影响，那为什么还要关注compileSdkVersion版本呢？
 
> ——①应用想兼容新版本、使用了新版本API，此时就必须使用新版本及以上版本编译，否则就会编译报错； 
> ——②如果使用了新版本的Support Library，此时也必须使用新版本及以上版本编译； 
> ——③推荐使用最新版本编译，用新的编译检查，可以看到很多新版本相关的警告，提前预研新版本开发；

##minSdkVersion##

> 1、minSdkVersion表明此应用兼容的最低版本，在低于该版本的手机上安装时会报错，无法安装； 

> 2、如果最低版本设置为19，在代码中使用了API 23中的API，就会有警告。使用运行时检查系统版本的方式可解决； 

> 3、如果你使用的某个Support Library的最低版本为7，那minSdkVersion就必须大于等于7了，否则该Support Library在低于7的手机中就要报错了；

##targetSdkVersion##

> 1、如果targetSdkVersion为19（对应为Android4.4），应用运行时，最高只能使用API 19的新特性。即使代码中使用了API 23的新特性，实际运行时，也不会使用该新特性；

> 2、同样的API，比如AlarmManger的set()和get()方法，在API 19和之前的效果是不一样的，如果targetSdkVersion为18，无论运行手机是什么版本，都是旧效果；如果targetSdkVersion为19，那么在4.4以上的手机上运行时，就是新效果了。

##总结##

> 综上所诉，理想状态下，minSdkVersion应该是最低兼容的版本，尽可能覆盖更多手机。targetSdkVersion和compileSdkVersion应该是最新版本，让新版本手机用户获得更好的体验。