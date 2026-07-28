<div align="center">

# 🎭 FaceSDK 人脸识别 UTS API 插件  

**高性能 1:1 人脸识别 · 离线活体检测 · 多端统一**  
 
[![Platform](https://img.shields.io/badge/Platform-iOS%20%7C%20Android-blue.svg)](https://ext.dcloud.net.cn/)  
[![Vue](https://img.shields.io/badge/Vue-Vue2%20%7C%20Vue3%20%7C%20uvue-42b883.svg)](https://uniapp.dcloud.net.cn/)  
[![GitHub Issues](https://img.shields.io/github/issues/FaceAISDK/FaceAISDK_uniapp_UTS)](https://github.com/FaceAISDK/FaceAISDK_uniapp_UTS/issues)  
 
[快速上手](#-快速上手) • [常见问题与解决方案](#-常见问题与解决方案) • [状态码说明](#-状态码说明) • [社区与支持](#-社区与支持)  
 
---

</div>

## 📌 插件简介

**FaceAISDK UTS 插件** 是专为 uni-app / uni-app x 打造的离线人脸识别与活体检测解决方案。

*   **完全离线**：所有计算均在终端本地处理，无需依赖后台 API 服务，保护用户隐私并降低运营成本。
*   **高性能无依赖**：原生轻量封装，无需依赖任何第三方 SDK。
*   **多平台兼容**：完美支持 **iOS**、**Android**，兼容 **Vue2**、**Vue3** 以及全新的 **uvue** 渲染引擎。

---

## 🚀 快速上手

在引入和使用 UTS 插件前，请确保你已阅读并配置好基础环境：[DCloud 官方 UTS 插件基础环境配置指南](https://doc.dcloud.net.cn/uni-app-x/plugin/uts-plugin.html)。

### 1️⃣ 运行示例项目
> 💡 **建议**：请先下载并运行最新示例项目，在熟悉功能和接口后再集成到你的主业务项目中。

---

### 2️⃣ 制作自定义调试基座
在 HBuilderX 中依次点击：  
`运行` ➔ `运行到手机或模拟器` ➔ `制作自定义调试基座` ➔ `打包`

> ⚠️ **注意**：制作基座期间请勿随意改动原生代码。

<div align="center">
  <img src="https://i.postimg.cc/QVZFgycd/1.png" alt="制作自定义调试基座" />
</div>

---

### 3️⃣ 使用自定义基座运行
打包完成后，依次点击：  
`运行` ➔ `运行到 Android/iOS 基座` ➔ 选择 **使用自定义基座运行** ➔ 选择 **本地基座** ➔ 点击 **运行**

<div align="center">
  <img src="https://i.postimg.cc/QdwtZM60/2.png"  alt="使用自定义基座运行" />
</div>

---

### 4️⃣ 集成到你的主项目
在需要使用人脸识别的页面中引入插件 API：

```typescript
import { faceVerify /* , ...其他方法 */ } from "@/uni_modules/FaceAISDK-Core";
```

> 💡 **核心提示**：请务必遵循先打包自定义调试基座，再运行项目的流程。若偶遇云打包服务繁忙失败，请尝试重新提交打包。

---

## ❓ 常见问题与解决方案

### 1. UI 交互效果不符合业务需求？

目前 UTS 插件版本仅支持自定义字体与主题颜色。如果你需要深入修改页面 UI、布局或交互逻辑，建议拉取原生 SDK 自行二次开发并封装插件：

* **iOS SDK**: [FaceAISDK_iOS](https://github.com/FaceAISDK/FaceAISDK_iOS)
* **Android SDK**: [FaceAISDK_Android](https://github.com/FaceAISDK/FaceAISDK_Android)

### 2. 炫彩活体提示光线太亮导致失败？

炫彩活体需要通过手机屏幕发射彩光投射到脸部进行感应。

* **应对方案**：引导用户使用手掌遮挡强光或移至遮阳处；
* **场景建议**：在室外强光或日光直射环境下，推荐改用 **“动作活体 + 静默活体”** 组合方案。

### 3. 改动原生代码后基座不能正常运行？

自定义基座生成后，原生的 Kotlin/Swift 编译产物已经固化。如修改了 `native` 目录下的原生代码，**必须重新制作自定义调试基座** 才能生效。

### 4. App 体积裁剪与优化

Android 动态库默认包含了针对 32 位老旧设备的兼容。若仅针对现代主流手机，可在 `build.gradle` 中过滤 SO 库，仅保留 `arm64-v8a` 架构，可大幅降低生成 APK 的体积。

---

## 🔢 状态码说明

SDK 在识别或活体检测过程中通过回调返回的状态码定义如下：

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


---

## 📱 Android 原生 SDK & 体验 Demo

如果你需要更高级的功能（如 **UVC 协议外接摄像头支持**、**从相册批量导入** 等），可下载原生体验包进行体验：

  <div align=center>
     <img src="https://www.pgyer.com/app/qrcode/faceVerify" width = 15%   alt="扫一扫下载Demo"/>
  </div>


## 🤝 社区与支持

如果您在开发过程中遇到问题，欢迎随时联系我们！请提供尽可能详细的背景信息（包括但不限于 **HBuilderX 版本**、**Vue 版本**、**测试机型平台** 以及 **报错日志/使用场景**），这有助于我们更快为你解决问题。

* 🐛 **提交 Issue**: [GitHub Issues](https://github.com/FaceAISDK/FaceAISDK_uniapp_UTS/issues)
* 📧 **EMAIL**: [FaceAISDK.Service@gmail.com](https://www.google.com/search?q=mailto%3AFaceAISDK.Service%40gmail.com)
* 💬 **WeChat**: `FaceAISDK`

---