## eclipse修改Android默认签名文件(debug.keystore) ##

### 1、需求 ###

&emsp;&emsp;接入第三方开发时，经常可能遇到先要将apk需要的签名文件上传至第三方，第三方平台才会分配你相关的apk配置信息，并且apk发布时的签名文件必须和你上传的签名文件一致，否则无法通过审核以及使用第三方的服务。
&emsp;&emsp;从第三方平台下载下来的用例，经常需要使用第三方平台下载下来的签名文件进行签名，才可以在手机上正常运行该用例。**那如何实现将第三方平台下载的签名文件替换掉eclipse的签名文件？？？**

### 2、方案 ###

1. dos命令安装apk文件到手机上
2. 直接替换到eclipse中的签名文件

### 3、资料 ###

<a href="https://blog.csdn.net/liu537192/article/details/41788355">eclipse修改Android默认签名文件(debug.keystore)</a>

> 最近这段时间，在做公司的游戏SDK的时候，碰到一个蛋疼的问题。因为SDK集成了微信支付的功能，但是在使用微信支付的时候，会校验App所使用的签名文件，而我们在开发的时候使用：Run----->Android Application 的方式话，用的是默认的签名文件，这个默认的签名文件可以通过：Window---->Preferences----->Android----->Build找到。我起初的解决办法是，每次都使用eclipse的Export功能来打包，然后在dos环境下使用：adb install xxx.apk来运行，但是这样太影响效率了。其实可以把默认的签名文件替换成我们需要的签名文件，在Window---->Preferences----->Android----->Build中我们可以找到Custome debug keystore，如果我们配置了这个，那么在Run----->Android Application的时候使用的就是我们配置的签名文件。

> 但是在配置这个签名文件的时候出现了问题，总是提示：Keystore was tampered with, or password was incorrect.这句话的意思是我们在配置自定义的签名文件的时候，需要保证自定义的签名文件和默认的签名文件是一样的密码、一样的别名、一样的别名密码。

> 所以，一般情况下，我们需要修改自定义签名文件的密码、别名、别名密码。如何修改呢？

> 默认签名文件的信息如下：

> Keystore name: “debug.keystore”
Keystore password: “android”
Key alias: “androiddebugkey”
Key password: “android”
CN: “CN=Android Debug,O=Android,C=US”

> 1、 首先当然是先复制一份正式证书出来作为要修改为的临时调试证书。

> 2、 修改keystore密码的命令(keytool为JDK带的命令行工具，只要java环境变量配置正确了，使用这个工具就没有问题)，DOS环境下进入签名文件所在目录：

    keytool -storepasswd -keystore my.keystore

> 其中，my.keystore是复制出来的证书文件，执行后会提示输入证书的当前密码，和新密码以及重复新密码确认。这一步需要将密码改为android。

> 3、 修改keystore的alias：

    keytool -changealias -keystore my.keystore -alias my_name -destalias androiddebugkey

> 这一步中，my_name是证书中当前的alias，-destalias指定的是要修改为的alias，这里按规矩来，改为androiddebugkey！这个命令会先后提示输入keystore的密码和当前alias的密码。

> 4、 修改alias的密码：

    keytool -keypasswd -keystore my.keystore -alias androiddebugkey

> 这一步执行后会提示输入keystore密码，alias密码，然后提示输入新的alias密码，同样，按规矩来，改为android！

> 经过以上步骤，签名文件的密码、别名、别名密码就改好了，我们就可以在Custome debug keystore中配置这个签名文件。配好签名文件后，之后我们每次通过Run------>Android Application运行程序的时候使用的就是我们自己的签名文件了。

### 4、样例 ###

无