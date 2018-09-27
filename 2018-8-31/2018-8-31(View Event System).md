## View的事件体系 ##

#### View基础知识 ####

- **什么是View**

> View是一种界面层的控件的一种抽象，它代表了一个控件

> ViewGroup，即控件组，ViewGroup继承了View，这意味着View本身就可以是单个控件也可以是由多个控件组成的一组控件，通过这种关系就形成了View树的结构。

- **View的位置参数**

> View位置由它的四个顶点来决定，分别对应于View的四个属性:top、left、right、bottom，这些坐标都是相对于View的父容器来说

> width = right-left
> height = bottom-top

获取方式:

> left = getLeft();
> right = getRight();
> top = getTop();
> bottom = getBottom();

从Android 3.0开始，View增加了额外的几个参数：x、y、translationX和translationY，其中x和y是View右上角的坐标，而translationX和translationY是View左上角对应于父容器的偏移量。这八个参数View为他们提供了get/se方法。

- **MotionEvent和TouchSlop**

&emsp;1. MotionEvent

&emsp;&emsp;在手指接触屏幕后所产生的一系列事件中，典型的事件类型有如下几种：

&emsp;&emsp;&emsp;- ACTION_DOWN——手指刚接触屏幕
&emsp;&emsp;&emsp;- ACTION_MOVE——手指在屏幕上移动
&emsp;&emsp;&emsp;- ACTION_UP——手指从屏幕上松开的一瞬间

> 正常情况下，手机触摸屏幕会出现如下几种情况：

> 1. 点击屏幕后离开松开，事件序列为DOWN->UP;

> 2. 点击屏幕滑动一会再松开，事件序列为DOWN->MOVE->……->MOVE->UP

> 系统提供了两组方法：getX/getY和getRawX/getRawY，getX/getY返回的是相对于当前View左上角的x和y坐标，而getRawX/getRawY返回的是相对于手机屏幕左上角的x和y坐标


- **TouchSlop**

> TouchSlop是系统所能识别出的被认为是滑动的最小距离。当手指在屏幕上滑动时，如果两次滑动之间的距离小于这个常量，那么系统就不认为你是在进行滑动操作。


> 通过如下方式可获得这个常量：
> ViewConfiguration.get(getContext()).getScaledTouchSlop()

> 当我们处理滑动时，可以利用这个常量来做一些过滤，比如当两次滑动事件的滑动距离小于这个值，我们就可以认为未达到滑动距离的临界值，因此就可以认为它们不是滑动。

- **VelocityTracker、GestureDetector和Scroller**

&emsp;1. VelocityTracker

> 速度追踪，用于追踪手指在滑动过程中的速度，包括水平和竖直方向的速度。

示例：
```
VelocityTracker velocityTracker = VelocityTracker.obtain();
velocityTracker.addMovement(event);
velocityTracker.computeCurrentVelocity(1000);
//获取x方向上的速度
int xVelocity = (int) velocityTracker.getXVelocity();
//获取y方向上的速度
int yVelocity = (int) velocityTracker.getYVelocity();
//回收内存
velocityTracker.clear();
velocityTracker.recycle();
```

*注意：*

**(1)**获取速度之前必须先计算速度，即在getXVelocity和getYVelocity之前要调用computeCurrentVelocity

**(2)**这里的速度是指一段时间内手指所滑动的像素数。在computeCurrentVelocity设置1000，即1000ms所移动的x方向与y方向的距离

&emsp;2. GestureDetector

> 手势检测，用于辅助检测用户的单击、滑动、长按、双击等行为（Android开发艺术探索 p127-128）

&emsp;3. Scroller

> 弹性滑动对象，用于实现View的弹性滑动。当使用View的scrollTo/scrollBy方法来进行滑动时，其过程是瞬间完成的，这个没有过渡效果的滑动用户体验不好。这个时候就可以使用Scroller来实现有过渡效果的滑动，其过程不是瞬间完成的，而是在一定时间间隔内完成的。

示例：
```
Scroller scroller = new Scroller(mContext);

//缓慢滚动到指定位置
private void smoothScrollTo(int destX, int destY) {
	int scrollX = getScrollX();
	int delta = destX - scrollX;
	//1000ms 内滑向destX，效果就是慢慢滑动
	mScroller.startScroll(scrollX, 0, delta, 0, 1000);
	invalidate();
}

@Override
public void computeScroll() {
	if(mScroller.computeScrollOffset()) {
		scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
		postInvalidate();
	}
}
```

#### View的滑动 ####

> 通过三种方式可以实现View的滑动：
> 1. 通过View本身提供的scrollTo/scrollBy方法来实现滑动
> 2. 通过动画给View施加平移效果来实现滑动
> 3. 通过改变View的LayoutParams使得View重新布局从而实现滑动

- **使用scrollTo/scrollBy**
(Android 开发艺术探索 p129-131)

> 在滑动过程中，mScrollX的值总是等于View左边缘和View内容左边缘在水平方向的距离，在mScrollY的值总是等于View上边缘和View内容上边缘在竖直方向的距离。View边缘是指View的位置，有四个顶点组成，而View内容边缘是指View中的内容的边缘，scrollTo和scrollBy只能改变View内容的位置，而不能改变View在布局中的位置。当View左边缘在View内容左边缘的右边时，mScrollX为正值，反之为负值；当View上边缘在View内容上边缘的下边时，mScrollY为正值，反之为负值。换句话说，如果从左向右滑动，那么mScrollX为负值，反之为负值；如果从上往下滑动，那么mScrollY为负值，反之为正值。

- **使用动画**

> 动画需要用到View的translationX和translationY属性，既可以采用传统的View动画，也可以采用属性动画。

补间动画示例(向右下角分别移动100个像素)：

```
<?xml version="1.0" enciding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android"
android:fillAfter="true"
android:zAdjustment="normal">

	<translate
		android:duration="100"
		android:fromXDelta="0"
		android:fromYDelta="0"
		android:interpolator="@android:anim/linear_interpolator"
		android:toXDelta="100"
		android:toYDelta="100"/>
		
</set>
```

属性动画示例(X方向向右移动100个像素)：

```
ObjectAnimator.ofFloat(targetView, "translationX", 0, 100).setDuration(100).start();
```

- **改变布局参数**

> 改变布局参数，即改变LayoutParams

示例：

> 1. 把Button向右平移100px，我们只需要将这个Button的LayoutParams里的marginLeft参数的值增加100px即可
> 2. 在Button的左边放置一个空的View，这个空View的默认宽度为0，当我们需要向右移动Button时，只需要重新设置空View的宽度即可。

```
MarginLayoutParams params = (MarginLayoutParams)mButton1.getLayoutParams();
params.width += 100;
params.leftMargin += 100;
mButton1.requestLayout();
//或者 mButton1.setLayoutParams(params);
```

- **滑动方式的对比**

&emsp;1. scrollTo/scrollBy: 操作简单，适合对View内容的滑动
&emsp;2. 动画: 操作简单，主要适用于没有交互的View和实现复杂的动画效果
&emsp;3. 改变布局参数: 操作稍微复杂，适用于有交互的View

#### 弹性滑动 ####

- **使用Scroller**

代码示例：

```
Scroller scroller = new Scroller(mContext);

//缓慢滚动到指定位置
private void smoothScrollTo(int destX, int destY) {
	int scrollX = getScrollX();
	int deltaX = destX - scrollX;
	// 1000ms 内滑向 destX，效果就是慢慢滑动
	mScroller.startScroll(scrollX, 0,deltaX, 0, 1000);
	invalidate();
}

@Override
public void computeScroll() {
	if(mScroller.computeScrollOffset()) {
		scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
		postInvalidate();
	}
}
```

> 上面是Scroller典型的使用方法，当我们构造一个Scroller对象并且调用它的startScroll方法时，调用如下(只是对数据做了一下保存)：

```
public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	mMode = SCROLL_MODE;
	mFinished = false;
	mDuration = duation;
	mStartTime = AnimationUtils.currentAnimationTimeMillis();
	mStartX = startX;
	mStartY = startY;
	mFinalX = startX + dx;
	mFinalY = startY + dy;
	mDeltaX = dx;
	mDeltaY = dy;
	mDurationReciprocal = 1.0f / (float) mDuration;
}
```

> Scroller到底如何让View弹性滑动的呢？

> 就是通过startScroll下面的invalidate方法，invalidate方法会导致View重绘，在View的draw方法中又会去调用computeScroll方法，computeScroll方法在View中是一个空实现，因此需要我们自己去实现。

> postInvalidate方法会进行第二次重绘，然后继续会调用computeScroll方法，如此反复，直到整个滑动过程结束。

computeScrollOffset方法的实现：
```
/**
 * Call this when you want to know the new location. If it returns true,
 * the animation is not yet finished.
 * /
public boolean computeScrollOffset() {
	...
	int timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - mStartTime);

	if(timePassed < mDuration) {
		switch(mMode) {
		case SCROLL_MODE:
			final float x = mInterpolator.getInterpolation(timePassed * mDurationReciprocal);
			mCurrX = mStartX + Math.round(x * mDeltaX);
			mCurrY = mStartY + Math.round(x * mDeltaY);
			break;
		...
		}
	}
	return true;
}
```

- **通过动画**

> 动画本身就是一种渐进的过程，因此它天然就具有弹性效果，但是我们可以利用动画的特性来使用scrollTo来达到弹性动画的效果。

示例展示：

```
final int startX = 0;
final int deltaX = 100;
ValueAnimator animator = ValueAnimator.ofInt(0, 1).setDuration(1000);
animator.addUpdateListener(new AnimatorUpdateListener() {
	@Override
	public void onAnimationUpdate(ValueAnimator animator) {
		float fraction = animator.getAnimatedFraction();
		mButton1.scrollTo(startX + (int) (deltaX * fraction), 0);
	}
});
```

**注意：**
scrollTo是将View的内容移动，而不是将View本身移动。

- **使用延时策略**

> 通过发送一系列延时消息从而达到一种渐进式的效果，具体来说，可以使用Handler或View的postDelayed方法，也可以使用线程的sleep方法。

Handler示例：
```
private static final int MESSAGE_SCROLL_TO = 1;
private static final int FRAME_COUNT = 30;
private static final int DELAYED_TIME = 33;

private int mCount = 0;

@SuppressLint("HandlerLeak")
private Handler mHandler = new Handler() {
	public void handleMessage(Message msg) {
		switch(msg.what) {
			case MESSAGE_SCROLL_TO: {
				mCount++;
				if(mCount <= FRAME_COUNT) {
					float fraction = mCount / (float) FRAME_COUNT;
					int scrollX = (int) (fraction * 100);
					mButton1.scrollTo(scrollX, 0);
					mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
				}
				break;
			}
			default:
				break;
		}
	}
}
```

#### View事件的分发机制 ####

- **点击事件的传递规则**

> 点击事件的分发过程由三个很重要的方法来共同完成:dispatchTouchEvent、onInterceptTouchEvent和onTouchEvent。

	public boolean dispatchTouchEvent(MotionEvent ev)

> 用来进行事件的分发。如果事件能够传递给当前View，那么此方法一定会被调用，返回结果受当前View的onTouchEvent和下级View的dispatchTouchEvent方法的影响，表示是否消耗当前事件。

	public boolean onInterceptTouchEvent(MotionEvent event)

> 在上述方法内部调用，用来判断是否拦截某个事件，如果当前View拦截了某个事件，那么在统一个时间序列当中，此方法不会被再次调用，返回结果表示是否拦截当前事件。

	public boolean onTouchEvent(MotionEvent event)

> 在dispatchTouchEvent方法中调用，用来处理点击事件，返回结果表示是否消耗当前事件，如果不消耗，则在同一个事件序列中，当前View无法再次接收到事件。

上述三个方法它们的关系可以用如下伪代码表示：
```
public boolean dispatchTouchEvent(MotionEvent ev) {
	boolean consume = false;
	if(onInterceptTouchEvent(ev)) {
		consume = onToucheEvent(ev);
	} else {
		consume = child.dispatchTouchEvent(ev);
	}

	return consume;
}
```

> 当一个View需要处理事件时，如果它设置了OnTouchListener，那么OnTouchListener中的onTouch方法会被回调。这时事件如何处理还要看onTouch的返回值，如果返回false，则当前View的onTouchEvent方法会被调用；如果返回true，那么onTouchEvent方法将不会调用。由此可见，给View设置的OnTouchListener，其优先级比onTouchEvent要高。在onTouchEvent方法中，如果当前设置的有OnClickListener，那么它的onClick方法会被调用。可以看出，平时我们常用的OnClickListener，其优先级最低，即处于事件传递的尾端。

事件传递过程中遵循如下顺序：Activity->Window->View

**结论**

> (1) 同一个事件序列是指从手指接触屏幕的那一刻起，到手指离开屏幕的那一刻结束，在这个过程中所产生的一系列事件，这个事件序列以down事件开始，中间含有数量不定的move事件，最终以up事件结束。

> (2) 正常情况下，一个事件序列只能被一个View拦截且消耗。这一条的原因可以参考，因为一旦一个元素拦截了某此事件，那么同一个事件序列内的所有事件都会直接交给它来处理，因此同一个事件序列中的事件不能分别由两个View同时处理，但是通过特殊手段可以做到，比如一个View将本该自己处理的事件通过onTouchEvent强行传递给其他View处理。

> (3) 某个View一旦决定拦截，那么这一个事件序列都只能由它来处理(如果事件序列能够传递给它的话)，并且它的onInterceptTouchEvent不会再被调用。这条也很好理解，就是说当一个View决定拦截一个事件后，那么系统会把同一个事件序列内的其他方法都直接交给它来处理，因此就不用再调用这个View的onInterceptTouchEvent去询问它是否要拦截了。

> (4) 某个View一旦开始处理事件，如果它不消耗ACTION_DOWN事件(onTouchEvent返回false)，那么同一事件序列中的其他事件都不会再交给它来处理，并且事件将重新交由它的父元素去处理，即父元素的onTouchEvent会被调用。意思就是事件一旦交给一个View处理，那么它就必须消耗掉，否则同一事件序列中剩下的事件就不再交给它来处理了，这就好比上级交给程序员一件事，如果这件事没有处理好，短期内上级就不敢再把事情交给这个程序员做了，二者是类似的道理。

> (5) 如果View不消耗除ACTION_DOWN以外的其他事件，那么这个点击事件会消失，此时父元素的onTouchEvent并不会被调用，并且当前View可能持续收到后续的事件，最终这些消失的点击事件会传递给Activity处理。

> (6) ViewGroup默认不拦截任何事件。Android源码中ViewGroup的onInterceptTouchEvent方法默认返回false.

> (7) View没有onInterceptTouchEvent方法，一旦有点击事件传递给它，那么它的onTouchEvent方法就会被调用。

> (8) View的onTouchEvent默认都会消耗事件(返回true)，除非它是不可点击的(clickable和longClickable同时为false)。View的longClickable属性默认都为false，clickable属性要分情况，比如Button的clickable属性默认为true，而TextView的clickable属性默认为false。

> (9) View的enable属性不影响onTouchEvent的默认返回值。哪怕一个View是disable状态的，只要它的clickable或者longClickable有一个true，那么它的onTouchEvent就返回true。

> (10) onClick会发生的前提是当前View是可点击的，并且它收到了down和up的事件。

> (11) 事件传递过程是由外向内的，即事件总是先传递给父元素，然后再由父元素分发给子View，通过requestDisallowInterceptTouchEvent方法可以在子元素中干预父元素的事件分发过程，但是ACTION_DOWN事件除外。

- **事件分发的源码解析**

**源码：Activity#dispatchTouchEvent**

```
public boolean dispatchTouchEvent(MotionEvent ev) {
	if(ev.getAction() == MotionEvent.ACTION_DOWN) {
		onUserInteraction();
	}
	if(getWindow().superDispatchTouchEvent(ev)) {
		return true;
	}
	return onTouchEvent(ev);
}
```

**源码：PhoneWindow#superDispatchTouchEvent**
```
public boolean superDispatchTouchEvent(MotionEvent event) {
	return mDecor.superDispatchTouchEvent(event);
}
```

mDecor是DecorView
```
private final class DecorView extends FrameLayout implements RootViewSurfaceTaker

//This is the top-level view of the window, containing the window decor
```

**源码：PhoneWindow关于mDecor的声明**
```
private DecorView mDecor;

@Override
public final View getDecorView() {
	if(mDecor == null) {
		installDecor();
	}
	return mDecor;
}
```
(具体详情可见 Android开发艺术探索 p144-154)

