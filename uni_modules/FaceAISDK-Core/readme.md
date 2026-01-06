## HelloKitty-FaceAI
人脸识别，活体检测UTS API插件，支持iOS，Android 双端。
后面我们会支持主题色定制等功能，更多可根据原生工程项目修改升级插件

**原生工程：**  
iOS SDK： https://github.com/FaceAISDK/FaceAISDK_iOS
Android： https://github.com/FaceAISDK/FaceAISDK_Android

![demo](https://i.postimg.cc/LXfzpb4z/da-da165b6781-fu-ben.png)

注：iOS 插件使用MacOS 开发,真机iPhone系统26（Window有问题联系FaceAISDK.Service@gmail.com）

## 使用方法
  如果你是第一次运行UTS插件工程/引入UTS API插件，你应先安装官方说明配置好基础环境 [基础环境](https://doc.dcloud.net.cn/uni-app-x/plugin/uts-plugin.html) 

  - 1. 下载到对应的本地项目(你也可以先跑同单独的API插件工程)

  - 2. 按照文档 -》把插件引入项目（即 import { FaceAI } from '@/uni_modules/HelloKitty-FaceAI' 需要先引入），

  - 3. 运行-》运行到手机或模拟器 -》制作自定义调试基座 -》打包 等基座制作完成
    ![制作自定义调试基座](https://i.postimg.cc/QVZFgycd/1.png)

  - 4. 运行 -》 运行到手机或模拟器-》运行到iOS/Android基座-》选择使用自定义基座运行-》选择手机-》运行
    ![运行到手机](https://i.postimg.cc/QdwtZM60/2.png)
  若之前手机安装过基座需要先卸载之前的基座，iOS 可能会提示你安装好后杀死应进程后重新启动(可以点击几个其他应用加快彻底杀死重启)
  注：只支持真机调试，需要用到硬件摄像头



## 常见错误与解决方法
 - 1. iOS 自定义基座首次运行找不到iOS原生SDK FaceAISDK_Core
   ```
   Analyzing dependencies
   CocoaPods could not find compatible versions for pod "FaceAISDK_Core":
   in Podfile:
   FaceAISDK_Core (= 2025.12.31)
   None of your spec sources contain a spec satisfying the dependency: `FaceAISDK_Core (= 2025.12.31)`.
   ```
    基本重新运行就可以了，在线打包机器有时候会无法科学上网有问题无法访问GitHub  
	
 - 2. iOS 基座安装到手机后很久都是白屏/黑屏幕
  ```
   控制台输出
   项目 [FaceAI_API_Plugin] 已启动。请点击手机/模拟器的运行基座App（uni-app x）查看效果。
   如应用未更新，请在手机上杀掉基座进程重启
  ```
   根据提示杀掉基座进程重启，然后点击启动2个其他App后再重新启动基本就没问题了，本情况只会在第一次安装新基座出现
   
 - 3. 炫彩活体提示光线太亮导致失败
   这个基本上只能规避强光环境了，或引导用户用手遮住强烈光线，让手机彩色光能照到脸部
   
 - 4. 改动原生swift/kotlin 代码导致基座不能正常运行
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