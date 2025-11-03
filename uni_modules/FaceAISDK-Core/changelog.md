## 2025.11.01（2025-10-31）
- 更新相机管理CameraX（为做炫彩活体做准备），解除人脸区域占比大小限制
- 动作活体支持自由组合1-2种（ 1.张张嘴 2.微笑 3.眨眨眼 4.摇头 5.点头）
- 设备硬件配置检测并分为高中低3类
- 添加本地人脸缓存清除接口，以便相关合规整改
- 去除多人脸检测回调提醒，自动取最大的人脸分析
##  V2025.11.01
- 更新相机管理CameraX（为做炫彩活体做准备），解除人脸区域占比大小限制
- 动作活体支持自由组合1-2种（ 1.张张嘴 2.微笑 3.眨眨眼 4.摇头 5.点头）
- 设备硬件配置检测并分为高中低3类
- 添加本地人脸缓存清除接口，以便相关合规整改
- 去除多人脸检测回调提醒，自动取最大的人脸分析
- 升级工程Android Studio到Narwhal4 和AGP8.13等，以便更好的使用AI辅助以及调试Bitmap
  更多：https://mp.weixin.qq.com/s/048q5A1D3U_bdJY6tfsAwQ


##  V2025.10.21
- 去除Debug模式的弹窗调试信息
- 近距离但人脸完整不提示过近
- 人脸搜索中 提示优化
- UVC协议默认分辨率不支持情况处理
- 完善返回给三方插件交互code message
- 
## 1.8.0（2025-10-16）
1. 添加英文文案（软件翻译可能词不达意）
2. SDK支持切换使用3种相机类型
3. setCameraType API 更改为FaceAICameraType类型（SYSTEM,UVC_RGB,UVC_RGB_IR）
4. 优化人脸过小，未检测到人脸判断
5. 优化交互过程的提示错误
6. 
## 1.7.0（2025-09-29）
1.完善插件各种参数传递
2.添加人脸可选择精确模式或快速模式
3.完善不同场景使用，解决bug
4.修复低配设备摄像头画面卡顿问题

## 1.6.0（2025-09-22）
1.Google Play 上架合规问题处理
2.完善录入人脸角度处理
3.脸部光线判断
4.相机等级判断

## 1.5.0（2025-09-05）
1.外包FaceAISDK之UTS插件(uni-app兼容模式组件) 开发 
 https://github.com/FaceAISDK/FaceAISDK_Android/blob/publish/FaceAISDK%E4%B9%8BUTS%E6%8F%92%E4%BB%B6(uni-app%E5%85%BC%E5%AE%B9%E6%A8%A1%E5%BC%8F%E7%BB%84%E4%BB%B6)%E5%BC%80%E5%8F%91.md

## 1.4.0（2025-08-27）
1. uniApp 接入报错可以先使用老版本UniApp插件： https://github.com/FaceAISDK/UniPlugin-FaceAISDK
2. 我们将完善uniApp接入UTS 版本插件问题,本插件接入Demo https://github.com/FaceAISDK/FaceAISDK_uniapp_UTS

## 1.3.0（2025-08-25）
UTS 人脸识别API插件，先运行成功Demo 项目 https://github.com/FaceAISDK/FaceAISDK_uniapp_UTS 
1. 工信部安全合规要求以及Google Play上架合规
2. 低配设备性能优化
3. 更新Android原生SDK
4. 添加相机权限基础管理

## 1.2.0（2025-07-31）
1. 完整实现Android 平台人脸识别，活体检测和人脸录入
2. iOS 搭建了基础API 协议,0815 发布正式版本
3. 更新Android 原生FaceAISDK 插件版本

## 1.1.0（2025-07-05）
1. 实现Android 平台人脸识别，活体检测和人脸录入
2. 优化完善uniApp 环境使用

