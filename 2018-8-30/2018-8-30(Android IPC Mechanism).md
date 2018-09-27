## Android的IPC机制 ##

#### Android中的多进程模式 ####

这里顺带提一下前面章节开启不同的Activity任务栈可以在AndroidManifest中指定Activity的一个属性就可以开启多任务栈的模式了，就是android:taskAffinity="XXXXX"

**开启多进程模式**

- 多个应用就是多进程
- 在AndroidManifest中的四大组件指定android:process属性
- 通过JNI在native层去fork一个新的进程

> 多进程造成的问题：
> 
> 1. 静态成员和单例模式完全失效：因为内存不共享，每个进程都有自己单独的内存空间，也相当于进程各创建各自的静态成员以及单例。
> 
> 2. 线程同步机制完全失效：因为对象不是同一个，所以锁住的对象就无法实现同步机制。
> 
> 3. SharedPreferences的可靠性下降：SharedPreferences不支持两个进程同时去执行写操作，否则会导致一定几率的数据丢失。
> 
> 4. Application会多次创建：一个组件跑到一个新的进程中的时候，由于系统要在创建新的进程同时分配独立的虚拟机，所以这个过程其实就是启动一个应用的过程。

#### IPC基础概念介绍 ####

**Serializable接口**

> Serializable是Java所提供的一个序列化接口，为对象提供标准的序列化和反序列化操作，使用Serializable只需在类中声明实现Serializable接口并声明一个serialVersionUID即可，当然serialVersionUID不是必需的，但是如果不声明会影响反序列化过程。（具体详情可见：Android开发艺术探索43-45）

**Parcelable接口**

> 序列化由writeToParcel方法来完成
> 反序列化由CREATOR来完成

Parcelable的方法说明：

|方法|功能|标记位|
|:--:|:--:|:--:|
|createFromParcel(Parcel in)|从序列化后的对象中创建院士对象|无|
|newArray(int size)|创建指定长度的原始对象数组|无|
|User(Parcel in)|从序列化后的对象中创建原始对象|无|
|writeToParcel(Parcel out, int flags)|将当前对象写入序列化结构中，其中flags标识有两种值：0或1（参见右侧标记位），为1时表示当前对象需要作为返回值返回，不能立即释放资源，几乎所有情况都为0|PARCELABLE_WRITE_RETURN_VALUE|
|describeContents|返回当前对象的内容描述，如果含有文件描述符，返回1（参见右侧标记位），否则返回0，几乎所有情况都返回0|CONTENTS_FILE_DESCRIPTOR|

**Binder**

> Binder是Android中的一个类，它实现了IBinder接口。从IPC角度来说，Binder是Android中的一种跨进程通信方式，Binder还可以理解为一种虚拟的物理设备，它的设备驱动是/dev/binder，该通信方式在Linux中没有。

> 从Android Framework角度来说，Binder是ServiceManager连接各种Manager（ActivityManager、WindowManager,等等）和相应ManagerService的桥梁

> 从Android应用层来说，Binder是客户端和服务端进行通信的媒介，当bindService的时候，服务端会返回一个包含了服务端业务调用的Binder对象，通过这个Binder对象，客户端就可以获取服务端提供的服务或者数据，这里的服务包括普通服务和基于AIDL的服务。

AIDL详情可见（Android开发艺术探索 p57-60）

Binder设置死亡代理的案例：
（Android开发艺术探索 p60-61）

```
package com.example.testdemo;

public class MainActivity extends Activity {

    private static final String TAG = "frank";
    private ServiceConnection mCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.example.deathrecipientdemo","com.example.deathrecipientdemo.TestService"));
        intent.setAction("com.frank.test");
        final DeathRecipient deathHandle = new DeathRecipient() {

            @Override
            public void binderDied() {
                // TODO Auto-generated method stub
                Log.i(TAG, "binder is died");
            }
        };

        mCon = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                // TODO Auto-generated method stub
                Log.i(TAG, "onServiceDisconnected "+name.toShortString());

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
                try {
                    Log.i(TAG, "onServiceConnected "+name.toShortString()+"  "+service.getInterfaceDescriptor());
                    service.linkToDeath(deathHandle, 0);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };

        bindService(intent,mCon,Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unbindService(mCon);
    }
}
```
<a href="https://blog.csdn.net/briblue/article/details/51035412">该案例来源链接在此</a>

#### Android中的IPC方式 ####

- **使用Bundle**

> Intent传递Bundle数据，我们传递的数据必须能够被序列化

- **使用文件共享**

> 共享文件是一种不错的进程间通信方式，两个进程通过读/写同一个文件来交换数据。文件共享适合对数据同步要求不是很高的进程之间进行通信。

- **使用Messenger**

> Messenger可以翻译为信使，顾名思义，通过它可以在不同进程中传递Message对象，在Message中放入我们需要传递的数据，就可以轻松的实现数据的进程间传递了。

实现步骤：

&emsp;1. 服务端进程

&emsp;在服务端创建一个Service来处理客户端的连接请求，同时创建一个Handler并通过它来创建一个Messenger对象，然后在Service的onBind中返回这个Messenger对象底层的Binder即可。

&emsp;2. 客户端进程

&emsp;客户端进程中，首先要绑定服务端的Service，绑定成功后用服务端返回的IBinder对象创建一个Messenger，通过这个Messenger就可以向服务器发送消息了，发消息类型为Message对象。如果需要服务端能够回应客户端，就和服务端一样，我们还需要创建一个Handler并创建一个新的Messenger，并把这个Messenger对象通过Message的replyTo参数传递给服务端，服务端通过这个replyTo参数就可以回应客户端。
（Android开发艺术探索 p66-70）

- **使用AIDL**

Messenger因为它是串行处理消息（因为MessageQueue它将消息存放到队列中，要操作完上一个Message，才会取出下一个Message进行操作）以及无法实现跨进程调用服务端的方法这些弊端，所以我们可以使用AIDL来实现跨进程通信

&emsp;1. 服务端

&emsp;&emsp;首先创建一个Service监听客户端的连接请求，然后创建一个AIDL文件，将暴露给客户端的接口在这个AIDL文件中声明，最后在Service中实现这个AIDL接口即可。

&emsp;2. 客户端

&emsp;&emsp;客户端所要做事情就稍微简单点，首先需要绑定服务端的Service，绑定成功后，将服务端返回的Binder对象转成AIDL接口所属的类型，接着就可以调用AIDL中的方法了。

&emsp;3. AIDL接口的创建

&emsp;&emsp;AIDL文件支持的数据类型：

&emsp;&emsp;&emsp;(1) 基本数据类型(int、long、char、boolean、double等);

&emsp;&emsp;&emsp;(2) String和CharSequence;

&emsp;&emsp;&emsp;(3) List: 只支持ArrayList，里面的每个元素都必需被AIDL支持；

&emsp;&emsp;&emsp;(4) Map: 只支持HashMap，里面的每个元素都必需被AIDL支持，包括key和value；

&emsp;&emsp;&emsp;(5) Parcelable: 所有实现了Parcelable接口的对象

&emsp;&emsp;&emsp;(6) AIDL: 所有的AIDL接口本身也可以在AIDL文件中使用。

> AIDL中除了基本数据类型，其他类型的参数必须标上方向: in、out或者inout，in表示输入型参数，out表示输出型参数，inout表示输入输出型参数

*注意：AIDL接口中只支持方法，不支持声明静态常量，这一点区别于传统的接口*

（看完这节之后脑壳晕，温习的时候这节估计还要画点脑力）

- **使用ContentProvider**

> ContentProvider的底层实现是Binder

> 继承ContentProvider的6个必须实现的抽象方法：
> 1. onCreate（主线程）
> 2. query（Binder线程）
> 3. update（Binder线程）
> 4. insert（Binder线程）
> 5. delete（Binder线程）
> 6. getType

- **使用Socket**

> Socket也成为“套接字”，是网络通信中的概念，他分别流式套接字和用户数据报套接字两种，分别对应于网络的传输控制层中的TCP和UDP协议。

#### Binder连接池 ####

> 通常进层之间最常用的通信方式是AIDL

#### 选择合适的IPC方式 ####

|名称|优点|缺点|适用场景|
|:--:|:--:|:--:|:--:|
|Bundle|简单易用|只能传输Bundle支持的数据类型|四大组件间的进程间通信|
|文件共享|简单易用|不适合高并发场景，并且无法做到进程间的即时通信|无并发访问情形，交换简单的数据实时性不高的场景|
|AIDL|功能强大，支持一对多并发通信，支持实时通信|使用稍复杂，需要处理好线程同步|一对多通信且有RPC需求|
|Messenger|功能一般，支持一对多串行通信，支持实时通信|不能很好处理高并发情形，不支持RPC，数据通过Message进行传输，因此只能传输Bundle支持的数据类型|低并发的一对多即时通信，无RPC需求，或者无需要返回结果的RPC需求|
|ContentProvider|在数据源访问方面功能强大，支持一对多并发数据共享，可通过Call方法扩展其他操作|可以理解为受约束的AIDL，主要提供数据源的CRUD操作|一对多的进程间的数据共享|
|Socket|功能强大，可以通过网络传输字节流，支持一对多并发实时通信|实现细节稍微有点繁琐，不支持直接的RPC|网络数据交换|