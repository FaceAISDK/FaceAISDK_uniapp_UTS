## FaceAISDK UTS Plugin
```
 从HBuilderX 3.9起，支持uni-app x项目。详见uni-app x

 也就是说，uts可以在uni-app中使用，也可以在uni-app x中使用。

 在uni-app中，主编程语言是js。uts可以开发原生插件，包括API插件和组件插件。
 在uni-app x中，主编程语言是uts。不管是应用逻辑还是扩展插件，均使用uts编程。仅在Web平台和iOS的js驱动模式下可以使用js。  
 
 More：https://doc.dcloud.net.cn/uni-app-x/uts/	
```
根据官方描述FaceAISDK开发出UTS插件以便在uniApp和uniAppX中使用；目前Android UTS插件全部功能开发完毕。  
 
iOS UTS插件已经定义好API接口，需要能在uniApp以及uniAppX中使用，插件原生部分全部采用kotlin,Swift6.1 编写

建议开发人员先熟悉官方关于UTS 插件基础知识，跑成功本Demo 工程后再接入到你的主工程。


## 关于FaceAISDK
FaceAI SDK is on_device Offline Android Face Detection 、Recognition 、Liveness Detection Anti Spoofing and 1:N/M:N Face Search SDK

FaceAI SDK是设备端可离线不联网Android 人脸识别、动作及近红外IR活体检测、人脸图质量检测以及人脸搜索（1:N和M:N）SDK，可快速集成实现人脸识别，人脸搜索功能。

原生工程如下，大佬可以根据原生工程自行封装拓展完善UTS插件
iOS SDK： https://github.com/FaceAISDK/FaceAISDK_iOS  
Android： https://github.com/FaceAISDK/FaceAISDK_Android

第一次运行需要配置相关原生环境，参考官网
[Android UTS插件编译运行配置](https://uniapp.dcloud.net.cn/tutorial/run/uts-development-android.html)

## 关于插件使用
iOS为还在内测阶段，预计0915全部正式开放上线，正式使用需要你发送邮件到FaceAISDK.Service@gmail.com
描述内容包含插件基本使用场景简介，Android 包名和签名SHA1，iOS Bundle ID.  

各位大佬，我们只懂原生SDK封装，uniApp相关知识欠缺，请各位提出宝贵建议以便更加完善插件
如果工程师技术栈和业务方便，强烈建议先使用原生平台版本开发，高效便捷不需要中间层插件

## 外包FaceAISDK之UTS插件(uni-app兼容模式组件)开发

外包说明参考：[外包FaceAISDK之UTS插件(uni-app兼容模式组件)开发](FaceAISDK%E4%B9%8BUTS%E6%8F%92%E4%BB%B6%28uni-app%E5%85%BC%E5%AE%B9%E6%A8%A1%E5%BC%8F%E7%BB%84%E4%BB%B6%29%E5%BC%80%E5%8F%91.md)  