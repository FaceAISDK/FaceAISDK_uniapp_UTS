## FaceAISDK-UTS API 插件

1:1人脸识别，活体检测UTS API插件，支持iOS，Android，支持uniappX和uniapp。    
所有功能都是设备端侧AI能力可开飞行模式体验，无需后台API服务可完全离线运行，高性能无依赖

**感谢大家收藏与点赞**，如有问题可描述你的使用场景说明发送邮件到FaceAISDK.Service@gmial.com  
或提issues 到本插件GitHub托管工程：https://github.com/FaceAISDK/FaceAISDK_uniapp_UTS

快速体验完整人脸识别功能可以下载Demo App：
<div align=center>
<img src="https://www.pgyer.com/app/qrcode/faceVerify" width = 19%   alt="扫一扫下载Demo"/>
</div>

1:N 人脸搜索插件应用市场地址：https://ext.dcloud.net.cn/plugin?id=26467  
1:1人脸识别+活体检测插件地址：https://ext.dcloud.net.cn/plugin?id=23881

## 使用方法
  如果你是第一次运行UTS插件工程/引入UTS API插件，你应先安装官方说明配置好基础环境 [基础环境](https://doc.dcloud.net.cn/uni-app-x/plugin/uts-plugin.html) 
 
  #### 1. 下载[Demo工程](https://github.com/FaceAISDK/FaceAISDK_uniapp_UTS)先跑通；熟悉后参考文档集成到主项目

  #### 2. 运行 -》 运行到手机或模拟器 -》**制作自定义调试基座** -》打包 等基座制作完成  
   .
    ![制作自定义调试基座](https://i.postimg.cc/QVZFgycd/1.png)

  #### 3. 运行 -》运行到iOS/Android基座-》**使用自定义基座运行**-》选择手机-》运行 
   .
    ![运行到手机](https://i.postimg.cc/QdwtZM60/2.png)
	
  #### 4. 把插件引入到你的主项目（即 import {faceVerify,**等方法} from "@/uni_modules/FaceAISDK-Core";）
	
	
  **一定要先制作自定义调试基准，然后运行的时候使用自定义基准，看图片步骤引导**
	
  若之前手机安装过基座需要先卸载之前的基座，iOS 可能会提示你安装好后杀死应进程后重新启动(可以点击几个其他应用加快彻底杀死重启)
  注：只支持真机调试，需要用到硬件摄像头。**不可用于金融场景**


## 常见错误与解决方法
 #### 1.iOS自定义基座相比Android很容易打包失败
   ```
   Analyzing dependencies
   CocoaPods could not find compatible versions for pod "FaceAISDK_Core":
   in Podfile:
   FaceAISDK_Core (= 2026.01.04)
   None of your spec sources contain a spec satisfying the dependency: `FaceAISDK_Core (= 2026.01.04)`.
   ```
    - 1 **尽量在云服务器不忙的时候打包**   
    - 2 增加版本号并手动清除缓存   
    - 3 切换网络环境，尽量用Mac电脑打包（作者HbuilderX 版本4.8.7）   
	    
	
  #### 2.下载依赖TensorFlowLiteSwift超时出错了(高峰期打包容易超时)
   ```
	[!] Error installing TensorFlowLiteSwift
	[!] /usr/bin/git clone https://github.com/tensorflow/tensorflow.git /var/folders/ft/7c temp
	Cloning into '/var/folders/ft/7cxjq5ss2094sj67mbhnzjrc0000gn/T/d20260113-17932-1xwealt'...
	error: RPC failed; curl 18 transfer closed with outstanding read data remaining
	error: 3926 bytes of body are still expected
	fetch-pack: unexpected disconnect while reading sideband packet
	fatal: early EOF
   ```
   
	这表明：
    1. 仓库过大：TensorFlowLiteSwift 的源仓库（TensorFlow）非常庞大。
    2. 网络中断：编译环境（无论是本地还是云端）连接GitHub的速度不够快，高峰期打包发生超时，导致在下载完成前连接被切断。
	如果是本地 Mac 编译 
    如果你是在自己的 Mac 上运行 HBuilderX 进行打包，可以通过修改 Git 配置来解决：
    增加 Git 缓存大小（这是最直接的修复方法，将缓存设为 1GB）：
    git config --global http.postBuffer 1048576000
    git config --global https.postBuffer 1048576000

    开启 VPN/代理：确保终端走了代理流量，因为 TensorFlow 的服务器在海外。
    设置完后，重新在 HBuilderX 中点击打包。TensorFlowLiteSwift只要成功同步一次后就好了
	
 #### 3.iOS 基座安装到手机后很久都是白屏/黑屏幕
  ```
   控制台输出
   项目 [FaceAI_API_Plugin] 已启动。请点击手机/模拟器的运行基座App（uni-app x）查看效果。
   如应用未更新，请在手机上杀掉基座进程重启
  ```
   根据提示杀掉基座进程重启，然后点击启动2个其他App后再重新启动基本就没问题了，本情况只会在第一次安装新基座出现
   
 #### 4.炫彩活体提示光线太亮导致失败
   这个基本上只能规避强光环境了，或引导用户用手遮住强烈光线，让手机彩色光能照到脸部
   
 #### 5.改动原生swift/kotlin 代码导致基座不能正常运行
   只能重新制作自定义调试基座，UTS API插件使用方如果不需要修改插件底层实现尽量不用改原生代码
   

## 人脸识别，活体检测状态码
   人脸识别，活体检测状态码含义
```
    public static let DEFAULT = 0                  // 0   初始化状态，流程没有开始
    public static let VERIFY_SUCCESS = 1           // 1   人脸识别对比成功大于设置的threshold
    public static let VERIFY_FAILED = 2            // 2   人脸识别对比识别小于设置的threshold
    public static let MOTION_LIVENESS_SUCCESS = 3  // 3   动作活体检测成功（基本不用，还有后续动作）
    public static let MOTION_LIVENESS_TIMEOUT = 4  // 4   动作活体超时
    public static let NO_FACE_MULTI = 5            // 5   多次没有检测到人脸
    public static let NO_FACE_FEATURE = 6          // 6   没有对应的人脸特征值
    public static let COLOR_LIVENESS_SUCCESS = 7   // 7   炫彩活体成功
    public static let COLOR_LIVENESS_FAILED = 8    // 8   炫彩活体失败
    public static let COLOR_LIVENESS_LIGHT_TOO_HIGH = 9 // 9   炫彩活体失败，光线亮度过高
    public static let ALL_LIVENESS_SUCCESS = 10    // 10  所有的活体检测完成(包括动作和炫彩)
```

  Copyright © 2026 FaceAISDK. All rights reserved。 FaceAISDK.Service@gmail.com 
