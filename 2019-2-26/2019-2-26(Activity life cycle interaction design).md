## Activity生命周期交互设计思想 ##

### Activity切换的生命周期函数调用顺序 ###
一个应用有两个界面，分别是ActivityA、ActivityB，其中ActivityA为启动界面

> ActivityA -> ActivityB（ActivityA启动并跳转到ActivityB的过程）

|顺序|ActivityA|ActivityB|
|:--:|:--:|:--:|
|1|onCreate|-|
|2|onStart|-|
|3|onResume|-|
|4|onPause|-|
|5|-|onCreate|
|6|-|onStart|
|7|-|onResume|
|8|onStop|-|

> ActivityB -> ActivityA（ActivityB返回到ActivityA的过程）

|顺序|ActivityA|ActivityB|
|:--:|:--:|:--:|
|1|-|onPause|
|2|onRestart|-|
|3|onStart|-|
|4|onResume|-|
|5|-|onStop|
|6|-|onDestroy|

### 总结 ###

从生命周期函数的调用顺序，我们可以看出Activity切换时，都是先调用上一个Activity的onPause函数，然后再调用下一个Activity的onCreate/onRestart、onStart、onResume函数，最后才调用上一个Activity的onStop函数，**为什么要如此设计Activity的生命周期函数调用顺序？**

1. 如果先调用下一个Activity的onCreate/onRestart、onStart、onResume方法，然后接着调用上一个Activity的onPause、onStop方法，这时可能会出现假如你正在看视频，但是一个电话打入进来，电话的界面已经接入，但是视频现在依旧在播放，所以onPause调用在前的设计的思想是希望开发人员在onPause方法中暂停此应用的所有操作（如暂停当前视频播放）
2. 如果先调用上一个Activity的onPause、onStop方法，然后接着调用下一个Activity的onCreate/onRestart、onStart、onResume方法，这时可能会出现如果下一个Activity因为一些原因导致闪退，但是上一个Activity已经调用了onStop方法将界面隐藏了，这样用户体验就不是很好，所以onStop方法要在调用下一个Activity的onResume方法之后调用。