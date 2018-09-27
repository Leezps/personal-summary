## Activity的启动模式（具体详情可参考Android开发艺术探索） ##

#### standard:标准模式 ####

> 每次启动一个Activity都会重新创建一个新的实例，不管这个实例是否已经存在。被创建的实例的生命周期符合典型情况下Activity的生命周期。 

*在标准模式下，谁启动了这个Activity，那么这个Activity就运行在启动他的那个Activity所在的栈中。比如Acticity A启动了ActivityB（B是标准模式），那么B就会进入到A所在的栈中。*

> 平时我们可能用ApplicationContext去启动standard模式的Activity,它就会报错，报android.util.AndroidRuntimeException:……FLAG_ACTIVITY_NEW_TASK……

>这是因为standard模式的Activity默认会进入启动它的Activity所属的任务栈中，但是由于非Activity类型的Context没有所谓的任务栈，所以通过ApplicationContext启动就有问题了。

> 解决问题的方法就是待启动Activity时，指定FLAG_ACTIVITY_NEW_TASK标记位，这样启动的时候就会为它创建一个新的任务栈，这时候启动的Activity实际上是singleTask模式

#### singleTop:栈顶复用模式 ####

> 在这种模式下，如果新的Activity已经位于任务栈的栈顶，那么此Activity不会被重新创建，同时它的onNewIntent方法会被回调，通过此方法的参数我们可以取出当前请求的信息。

示例展示：

	ABCD---startActivity D---->ABCD
	AB----startActivity D---->ABD
	ABDC---startActivity D---->ABDCD

#### singleTask:栈内复用模式 ####

> 这是一种单实例模式，在这种模式下，只要Activity在一个栈中存在，那么多次启动此Activity都不会重新创建实例，和singleTop一样，系统也会回调其onNewIntent。

示例展示：

	任务栈相同：
	ABC---startActivity D--->ABCD
	ADBC---startActivity D--->AD

	任务栈不同：
	ABC--->startActivity D--->新建一个任务栈--->TASK_2:D   TASK_1:ABC

#### singleInstance:单实例模式 ####

> 这是一种加强的singleTask模式，它除了具有singleTask模式的所有特性之外，还加强了一点，那就是具有此种模式的Activity只能只能单独的位于一个任务栈中，话句话说，比如Activity A是singleInstance模式，当A启动后，系统会为它创建一个新的任务栈，然后A独自在这个新的任务栈中，由于栈内复用的特性，后续的请求均不会创建新的Activity，除非这个独特的任务栈被系统销毁了

启动模式上有两种方式实现，一种就是在AndroidManifest中静态注册，另一种就是通过代码启动的时候设置上标示位去启动，第二种的优先级要高于第一种，当两种同时存在时，以第二种传递的标示位为准。

#### 代码中设置标示位达到启动模式的效果 ####

**Activity的Flags**

- **FLAG_ACTIVITY_NEW_TASK**

> 这个标示位的作用和在AndroidManifest中为Activity指定“singleTask”启动模式一样

- **FLAG_ACTIVITY_SINGLE_TOP**

> 这个标示位的作用和在AndroidManifest中为Activity指定“singleTop”启动模式一样

- **FLAG_ACTIVITY_CLEAR_TOP**

> 具有此标示位的Activity,当它启动时，在同一个任务栈中所有位于它上面的Activity都要出栈。这个模式一般需要和FLAG_ACTIVITY_NEW_TASK配合使用，在这种情况下，被启动Activity的实例如果已经存在，那么系统就会调用它的onNewIntent。如果被启动的Activity采用standard模式启动，那么它连同他之上的Activity都要出栈，系统会创建新的Activity实例并放入栈顶，singleTask启动模式默认就具有此标示位的效果

- **FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS**

> 具有这个标示的Activity不会出现在历史Activity的列表中，当某些情况下我们不希望用户通过历史列表回到我们的Activity的时候这个标识比较有用。它等同于在AndroidManifest中指定Activity的属性android:excludeFeomRecents="true"

#### IntentFilter的匹配规则 ####

通常启动Activity有两种方式，分别是显示启动与隐式启动，显示启动需传入包名与类名，隐式启动则不需要明确指定组件的信息。原则上不存在一个Intent既是显示调用，又是隐式调用，如果两者都存在以显示调用为主。

> - 一个Activity中可以有多个intent-filter
> - 一个intent-filter可以有多个action、category、data

如下：

```
<activity android:name="ShareActivity">
	<intent-filter>
		<action android:name="android.intent.action.SEND"/>
		<category android:name="android.intent.category.DEFAULT"/>
		<data android:mimeType="text/plain"/>
	</intent-filter>

	<intent-filter>
		<action android:name="android.intent.action.SEND"/>
		<action android:name="android.intent.action.SEND_MULTIPLE"/>
		<category android:name="android.intent.category.DEFAULT"/>
		<data android:mimeType="application/vnd.google.panorama360+jpg"/>
		<data android:mimeType="image/*"/>
		<data android:mimeType="video/*"/>
	</intent-filter>
</activity>
```

当匹配的过程中，只要Intent能匹配任何一组intent-filter即可成功启动对应的Activity

- action的匹配规则

> &emsp;&emsp;一个过滤规则可以有多个action,action的匹配要求：Intent中的action必须存在且必须和过滤规则中的其中一个action相同，action区分大小写，大小写不同字符串相同的action会匹配失效

- category的匹配规则

> &emsp;&emsp;一个过滤规则可以有多个category，category的匹配要求：(含有默认的category)Intent中的category可以不存在，只要action匹配成功就可以启动，但是如果Intent存在category，那就必须属于过滤规则中规定了的category，否则无法匹配成功。

- data的匹配规则

> &emsp;&emsp;一个过滤规则可以有多个data，data的匹配要求：如果过滤规则中有data，则Intent中必须要有data（具体可参考Android开发艺术探索p31）

