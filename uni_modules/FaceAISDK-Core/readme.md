 <br>
 <br>

# FaceAISDK人脸识别UTS API插件简介

**1:1人脸识别，活体检测插件**，支持iOS，Android，支持vue2，vue3和uvue。   
所有功能无需后台API服务可完全离线运行，高性能无依赖其他三方SDK服务

**感谢点赞收藏**，如有问题可 [到GitHub提issues](https://github.com/FaceAISDK/FaceAISDK_uniapp_UTS/issues) , [邮件联系](mailto:FaceAISDK@gmail.com) 或 Wv：FaceAISDK  
详细的信息有助于我们更好更快解决问题（Hbuilder版本，Vue版本，平台，功能和场景等）  
 <br>

# 使用方法
  如果你是第一次运行UTS插件工程/引入UTS API插件，你应先安装官方说明配置好基础环境 [基础环境](https://doc.dcloud.net.cn/uni-app-x/plugin/uts-plugin.html) 
  
  ## 1. 下载最新示例项目到HbuilderX先跑通；熟悉后再参考文档集成到你的主项目  
   <br>
   
   

  ## 2. 运行 -》 运行到手机或模拟器 -》**制作自定义调试基座** -》打包 （期间不要修改原生代码） 
   <br>
    ![制作自定义调试基座](https://i.postimg.cc/QVZFgycd/1.png)  
   <br>
   <br>
  ## 3. 运行 -》运行到iOS/Android基座 -》**使用自定义基座运行** -》本地基座 -》运行 
   <br>
    ![运行到手机](https://i.postimg.cc/QdwtZM60/2.png)  
   <br>
   <br>
	
  ## 4. 把插件引入到你的主项目（即 import {faceVerify,**等方法} from "@/uni_modules/本插件名称ID";）
  <br>
  **一定一定要先制作自定义调试基准，然后运行的时候使用自定义基准-本地基座，请看图片步骤引导说明**  
  <br>


   <br>
# 常见错误与解决方法
   **出现错误可以把错误信息发给AI Agent分析，常见可能的错误与解决方法如下**  

 ## 1.iOS 基座安装到手机后很久都是白屏/黑屏
  ```
   控制台输出
   项目已启动。请点击手机/模拟器的运行基座App（uni-app x）查看效果。
   如应用未更新，请在手机上杀掉基座进程重启
  ```
   老旧手机根据提示确保杀死基座进程，稍后重启App就可以了
   
   
 ## 2.炫彩活体提示光线太亮导致失败
   这个基本上只能规避强光环境了，或引导用户用手遮住强烈光线，让手机彩色光能照到脸部
   室外强光环境建议使用动作活体+静默活体检测
   
   
 ## 3.改动原生Kotlin/Swift 代码导致基座不能正常运行
   自定义基座打好后不能再改动原生Kotlin/Swift代码，改动后需重新打包

   <br>
# 人脸识别，活体检测状态码  
  
  **人脸识别，活体检测状态码含义**  
  
```
    let DEFAULT = 0                  // 0   初始化状态，流程没有开始
    let VERIFY_SUCCESS = 1           // 1   人脸识别对比成功大于设置的threshold
    let VERIFY_FAILED = 2            // 2   人脸识别对比识别小于设置的threshold
    let MOTION_LIVENESS_SUCCESS = 3  // 3   动作活体检测成功（基本不用，还有后续动作）
    let MOTION_LIVENESS_TIMEOUT = 4  // 4   动作活体超时
    let NO_FACE_MULTI = 5            // 5   多次没有检测到人脸
    let NO_FACE_FEATURE = 6          // 6   没有对应的人脸特征值
    let COLOR_LIVENESS_SUCCESS = 7   // 7   炫彩活体成功
    let COLOR_LIVENESS_FAILED = 8    // 8   炫彩活体失败  
    let COLOR_LIVENESS_LIGHT_TOO_HIGH = 9 // 9   炫彩活体失败，光线亮度过高  
    let ALL_LIVENESS_SUCCESS = 10    // 10  所有的活体检测完成(包括动作和炫彩)  
    let SILENT_LIVENESS_FAILED = 11  // 11  静默活体检测失败  
    let NO_BASE_FACE_FEATURE = 12    // 12  没有录入人脸信息  

```


  Powered by FaceAISDK Copyright©2026. 商用联系FaceAISDK@gmail.com  
  另：**1:N人脸搜索识别** 插件应用市场地址：https://ext.dcloud.net.cn/plugin?id=26467   
  


# Android 完整版本SDK API Demo：
  完整版本支持VUC协议摄像头，从相册导入人脸等更多功能  
  
  <div align=center>
     <img src="https://www.pgyer.com/app/qrcode/faceVerify" width = 15%   alt="扫一扫下载Demo"/>
  </div>
  

