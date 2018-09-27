## Activity的生命周期 ##

#### 正常情况下Activity的生命周期流程 ####

> 两个Activity之间的跳转，分别是MainActivity与SecondActivity，MainActivity是启动界面，SecondActivity是跳转界面。

MainActivity跳转到SecondActivity:

> MainActivity.onPause -> SecondActivity.onCreate -> SecondActivity.onStart -> SecondActivity.onResume -> MainActivity.onStop

SecondActivity点击返回键跳转到MainActivity:

> SecondActivity.onPause -> MainActivity.onRestart -> MainActivity.onStart -> MainActivity.onResume -> SecondActivity.onStop -> SecondActivity.onDestroy

SecondActivity点击跳转按钮MainActivity，并且MainActivity的启动模式是singleTask

> SecondActivity.onPause -> **MainActivity.onNewIntent** ->MainActivity.onRestart -> MainActivity.onStart -> MainActivity.onResume -> SecondActivity.onStop -> SecondActivity.onDestroy

手机黑屏

> onPause -> onStop

唤醒屏幕

> onRestart -> onStart -> onResume

**从上方生命周期可以看出，我们不能在onPause中做重量级的操作，因为onPause执行完成新的Activity才能Resume,虽然onPause与onStop都不能执行耗时操作，但是两者相对而言，我们应尽量将操作放到onStop中，从而使得新的Activity尽快的显示出来并切换到前台。**

#### 非正常情况下Activity的生命周期流程 ####

**1. 横竖屏切换**

> onSaveInstanceState运行在onStop之前，onRestoreInstanceState运行在onStart之后，当一个Activity切换到另一个Activity时，onSaveInstanceState也会被调用，但是切换回来时onRestoreInstanceState不会被调用。onSaveInstanceState被调用在SecondActivity的Resume之后，MainActivity的onStop之前。

**2. 资源内存不足，回收优先级比较低的Activity**

> Activity按照优先级从高到低，可分为如下三种：
> 1. 前台Activity——正在和用户交互的Activity，优先级最高
> 2. 可见但非前台Activity——弹出对话框，导致Activity可见但是无法与用户直接交互
> 3. 后台Activity——已经被暂停的Activity，比如执行了onStop，优先级最低

当系统内存不足时，系统会按照上述优先级去杀死目标Activity所在的进程，并在后续通过onSaveInstanceState和onRestoreInstanceState来保存和恢复数据。如果一个进程中四大组件都不在执行，这样的进程很容易被杀死，比较好的方法是将后台工作放入Service中从而保证进程有一定的优先级，这样就不会轻易的被系统杀死。

> 通常设置Activity横竖屏切换时不重新创建，通过在AndroidManifest中的Activity添加如下属性：

```
android:configChanges="orientation"
//如果想添加更多的属性，可以通过"|"进行拼接
```

表1-1 configChanges的项目和含义

|项目|含义|
|:--:|:--:|
|mcc|SIM卡唯一标识IMSI（国际移动用户识别码）中的国家代码，由三位数字组成，中国为460，此项标识mcc代码发生了改变|
|mnc|SIM卡唯一标识IMSI（国际移动用户识别码）中的运营商代码，由两位数字组成，中国移动TD系统为00，中国联通01，中国电信为03，此项标识mnc发生改变|
|locale|设备的本地位置发生了改变，一般指切换了系统语言|
|touchscreen|触摸屏发生了改变，这个很费解，正常情况下无法发生，可以忽略它|
|keyboard|键盘类型发生了改变，比如用户使用了外插键盘|
|keyboaedHidden|键盘的可访问性发生了改变，比如用户掉出了键盘|
|navigation|系统导航方式发生了改变，比如采用了轨迹球导航，这个有点费解，很难发生，可以忽略它|
|scrennLayout|屏幕布局发生了改变，很可能是用户激活了另外一个显示设备|
|fontScale|系统字体缩放比例发生了改变，比如用户选择了一个新字号|
|uiMode|用户界面模式发生了改变，比如是否开启了夜间模式(API8新添加)|
|orientation|屏幕方向发生了改变，这个是最常用的，比如旋转了手机屏幕|
|screenSize|当屏幕的尺寸信息发生了改变，当旋转设备屏幕时，屏幕尺寸会发生变化，这个选项比较特殊，它和编译选项有关，当编译选项中的minSdkVersion和targetSdkVersion均低于13时，此选项不会导致Activity重启，否则会导致Activity重启(API 13新添加)|
|smallestScreenSize|设备的物理屏幕尺寸发生改变，这个项目和屏幕的方向没关系，仅仅表示在实际的物理屏幕的尺寸改变的时候发生，比如用户切换到了外部的显示设备，这个选项和screenSize一样，当编译选项中的minSdkVersion和targetSdkVersion均低于13时，此选项不会导致Activity重启，否则会导致Activity重启(API 13新添加)|
|layoutDirection|当局部方向发生变化，这个属性用的比较少，正常情况下无需修改布局的layoutDirection属性（API 17新添加）|

**由上可知，只要minSdkVersion或targetSdkVersion中有一个在API 13以上，防止横竖屏切换，应该添加如下代码：**

```
android:configChanges="orientation|screenSize"
```

**添加上面代码之后，Activity不在调用onSaveInstanceState和onRestoreInstanceState来存储与恢复数据，取而代之是系统调用Activity的onConfigurationChanged方法，这个时候我们就可以做一些自己的特殊处理。**